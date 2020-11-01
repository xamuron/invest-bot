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

    private Scraper scraper = new Scraper();

    @Override
    public String getBotToken() {
        return System.getProperty("token");
    }

    public void onUpdateReceived(Update update) {
        Message inputMessage = update.getMessage();

        try {
            //проверяем есть ли сообщение и текстовое ли оно
            if (update.hasMessage() && update.getMessage().hasText())
                handleTextMessage(inputMessage);
            else
                if (update.hasCallbackQuery())
                    handleCallback(update);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void handleTextMessage(Message inputMessage) throws TelegramApiException {
        SendMessage outputMessage = new SendMessage();
        String userName = inputMessage.getFrom().getUserName();
        System.out.println("User " + userName + " send message: " + inputMessage.getText());

        if (inputMessage.getText().equals("/start")) {
            outputMessage
                    .setChatId(inputMessage.getChatId())
                    .setText("Hello! It's simple invest bot\n Press the button to continue... \n " +
                            "... or Enter stock market index name")
                    .setReplyMarkup(setInlineDefaultKeyboard());

            execute(outputMessage);
        }
        else {
            String price;
            Element index;
            outputMessage.setChatId(inputMessage.getChatId());
            try {
                index = scraper.searchIndexElementByName(inputMessage.getText());
                price = scraper.getPrice(index);
                outputMessage.setText(inputMessage.getText() + " price: " + price + "$");
            }
            catch (IndexNotFoundException e) {
                outputMessage.setText("index called " + inputMessage.getText() + " is not found");
                e.printStackTrace();
            }
            execute(outputMessage);
        }
    }

    public void handleCallback(Update update) {
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

    public String getBotUsername() {
        return "simple_fat_invest_bot";
    }
}
