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

    @Override
    public String getBotToken() {
        return System.getProperty("token");
    }

    public void onUpdateReceived(Update update) {
        Message inputMessage = update.getMessage();

        Scraper scraper = new Scraper();
        SendMessage message = new SendMessage();

        try {
            //проверяем есть ли сообщение и текстовое ли оно
            if (update.hasMessage() && update.getMessage().hasText()) {
                String userName = inputMessage.getFrom().getUserName();
                System.out.println("User " + userName + " send message: " + inputMessage.getText());

                if (inputMessage.getText().equals("/start")) {
                    message
                            .setChatId(inputMessage.getChatId())
                            .setText("Hello! It's simple invest bot\n Press the button to continue... \n " +
                                    "... or Enter stock market index name")
                            .setReplyMarkup(setInlineDefaultKeyboard());

                    execute(message);
                }
                else {
                    String price;
                    Element index;
                    message.setChatId(inputMessage.getChatId());
                    try {
                        index = scraper.searchIndexElementByName(inputMessage.getText());
                        price = scraper.getPrice(index);
                        message.setText(inputMessage.getText() + " price: " + price + "$");
                    }
                    catch (IndexNotFoundException e) {
                        message.setText("index called " + inputMessage.getText() + " is not found");
                        e.printStackTrace();
                    }
                    execute(message);
                }
            } else
                if (update.hasCallbackQuery()) {
                    String callbackId = update.getCallbackQuery().getId();
                    String msg1 = "wow, this is button1";
                    String msg2 = "wow, this is button2";

                    String clb = update.getCallbackQuery().getData();
                    if (clb.equals("button_1"))
                        answerCallbackQuery(callbackId, msg1);
                    else
                        answerCallbackQuery(callbackId, msg2);

                    String userName = update.getCallbackQuery().getFrom().getUserName();
                    System.out.println("Callback: " + userName + " click on " + clb);
                }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        List<InlineKeyboardButton> buttons3 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText("Button1").setCallbackData("button_1"));
        buttons1.add(new InlineKeyboardButton().setText("Button2").setCallbackData("button_2"));
        buttons2.add(new InlineKeyboardButton().setText("Button3").setCallbackData("button_3"));
        buttons2.add(new InlineKeyboardButton().setText("Button4").setCallbackData("button_4"));
        buttons2.add(new InlineKeyboardButton().setText("Button4").setCallbackData("button_4"));
        buttons3.add(new InlineKeyboardButton().setText("Button5").setCallbackData("button_5"));
        buttons.add(buttons1);
        buttons.add(buttons2);
        buttons.add(buttons3);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    public String getBotUsername() {
        return "simple_fat_invest_bot";
    }
}
