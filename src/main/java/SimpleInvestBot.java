import easter_eggs.tic_tac_toe.controller.GameManager;
import easter_eggs.tic_tac_toe.model.TicTacToeGame;
import enums.Mode;
import org.jsoup.nodes.Element;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import scraping_module.Scraper;
import scraping_module.exceptions.IndexNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class SimpleInvestBot extends TelegramLongPollingBot {

    private Mode mode;

    private boolean isGameSetupStage;

    public static final String START_COMMAND = "/start";
    public static final String GAME_COMMAND = "/game";
    public static final String STOP_GAME_COMMAND = "/stop_game";

    private Scraper scraper = new Scraper();
    private TicTacToeGame game;
    private GameManager gameManager;

    public SimpleInvestBot() {
        mode = Mode.ONBOARDING;
        isGameSetupStage = true;
    }

    @Override
    public String getBotToken() {
        return System.getProperty("token");
    }

    public String getBotUsername() {
        return "simple_fat_invest_bot";
    }

    public void onUpdateReceived(Update update) {
        Message inputMessage = update.getMessage();

        if (update.hasMessage()) {
            if (inputMessage.getText().equals(START_COMMAND))
                mode = Mode.ONBOARDING;
            else if (inputMessage.getText().equals(GAME_COMMAND))
                mode = Mode.GAME;
            else if (inputMessage.getText().equals(STOP_GAME_COMMAND)) {
                mode = Mode.DEFAULT;
                isGameSetupStage = false;
            }

//            else
//                mode = Mode.DEFAULT;
        }

        try {
            if (mode.equals(Mode.DEFAULT) || mode.equals(Mode.ONBOARDING)) {
                onDefaultUpdateReceive(update);
            }
            if (mode.equals(Mode.GAME)) {
                onGameUpdateReceive(update);
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup setInlineDefaultKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText("Button1").setCallbackData("button_1"));
        buttons1.add(new InlineKeyboardButton().setText("Button2").setCallbackData("button_2"));
        buttons.add(buttons1);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    private InlineKeyboardMarkup setTicTacToeFigureKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> figures = new ArrayList<>();
        figures.add(new InlineKeyboardButton().setText("X").setCallbackData("figure_x"));
        figures.add(new InlineKeyboardButton().setText("0").setCallbackData("figure_0"));
        buttons.add(figures);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    private void startGame() {

    }

    public void onGameUpdateReceive(Update update) throws TelegramApiException {
        if (isGameSetupStage) {
            if (update.hasCallbackQuery()) {
                handleSetupGameCallback(update);
                isGameSetupStage = false;
            }
            else {
                SendMessage sendMessage = new SendMessage();
                sendMessage
                        .setChatId(update.getMessage().getChatId())
                        .setText("Do you wanna play a game?... \n OK! Pick the figure")
                        .setReplyMarkup(setTicTacToeFigureKeyboard());
                execute(sendMessage);
            }
        }
        else {
            handleGameTextMessage(update.getMessage());
        }
    }

    public void handleSetupGameCallback(Update update) {
        String callbackId = update.getCallbackQuery().getId();

        String figure;
        String number;
        String message = "Your figure is $FIGURE. You go $NUMBER!\nEnter figure coordinate through a space (eg: 1 0)";

        String clb = update.getCallbackQuery().getData();

//        clb.equals("figure_x") ? answerCallbackQuery(callbackId, msg.concat("first!")) : answerCallbackQuery(callbackId, msg.concat("second!"));

        if (clb.equals("figure_x")) {
            figure = "X";
            number = "first";
        }
        else {
            figure = "0";
            number = "second";
        }

        answerCallbackQuery(
                callbackId,
                message
                        .replace("$FIGURE", figure)
                        .replace("$NUMBER", number)
        );

        String userName = update.getCallbackQuery().getFrom().getUserName();
        System.out.println("Callback: " + userName + " click on " + clb);
        game = new TicTacToeGame(userName, figure);
        gameManager = new GameManager(game);
    }

    public void handleGameTextMessage(Message message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        int userI, userJ;
        String[] coord = message.getText().split(" ");
        userI = Integer.parseInt(coord[0]);
        userJ = Integer.parseInt(coord[1]);
        sendMessage.setChatId(message.getChatId());

        gameManager.doTurn(userI, userJ);
        execute(sendMessage.setText(gameManager.getGameView()));
        if (!gameManager.winCondition()) {
            gameManager.doTurn(userI, userJ);
            sendMessage
                    .setText(gameManager.getGameView());
        }
        else {
            sendMessage
                    .setText(gameManager.getGameView() + "\n")
                    .setText("Winner: " + gameManager.getWinner());
            mode = Mode.DEFAULT;
            isGameSetupStage = true;
        }
        execute(sendMessage);
    }

    public void handleGameCallback(Update update) {

    }


    public void onDefaultUpdateReceive(Update update) throws TelegramApiException {
        Message inputMessage = update.getMessage();

        if (mode.equals(Mode.ONBOARDING)) {
            SendMessage outputMessage = new SendMessage();
            String userName = inputMessage.getFrom().getUserName();
            System.out.println("User " + userName + " send message: " + inputMessage.getText());

            outputMessage
                    .setChatId(inputMessage.getChatId())
                    .setText("Hello! It's simple invest bot\n Press the button to continue... \n " +
                            "... or Enter stock market index name")
                    .setReplyMarkup(setInlineDefaultKeyboard());
            execute(outputMessage);
            mode = Mode.DEFAULT;
        }
        else if (update.hasMessage() && update.getMessage().hasText() && !inputMessage.getText().equals(STOP_GAME_COMMAND))
            handleDefaultTextMessage(inputMessage);
        else if (update.hasCallbackQuery()) {
            handleDefaultCallback(update);
        }
    }

    private void handleDefaultTextMessage(Message inputMessage) throws TelegramApiException {
        SendMessage outputMessage = new SendMessage();
        String userName = inputMessage.getFrom().getUserName();
        System.out.println("User " + userName + " send message: " + inputMessage.getText());

        String price;
        Element index;
        outputMessage.setChatId(inputMessage.getChatId());
        try {
            index = scraper.searchIndexElementByName(inputMessage.getText());
            price = scraper.getPrice(index);
            outputMessage.setText(inputMessage.getText() + " price: " + price + "$");
        } catch (IndexNotFoundException e) {
            outputMessage.setText("index called " + inputMessage.getText() + " is not found");
            e.printStackTrace();
        }
        execute(outputMessage);
    }

    public void handleDefaultCallback(Update update) {
        String callbackId = update.getCallbackQuery().getId();
        String msg1 = "The right choice, grats!";
        String msg2 = "It's wrong button, sorry!";

        String clb = update.getCallbackQuery().getData();
        if (clb.equals("button_1"))
            answerCallbackQuery(callbackId, msg1);
        else
            answerCallbackQuery(callbackId, msg2);

        String userName = update.getCallbackQuery().getFrom().getUserName();
        System.out.println("Callback: " + userName + " click on " + clb);
    }

    public synchronized void answerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setText(message);
        answer.setShowAlert(true);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
