package telegram.bot.BelarusBorder.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.bot.BelarusBorder.config.BotConfig;
import telegram.bot.BelarusBorder.model.User;
import telegram.bot.BelarusBorder.model.UserRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    static final String ERROR_TEXT = "Error occurred: ";
    // названия кнопок
    private static final String DONATE_BUTTON = "DONATE_BUTTON";
    //страны
    static final String BELARUS_BUTTON = "BELARUS_BUTTON";
    static final String POLAND_BUTTON = "POLAND_BUTTON";
    static final String LITHUANIA_BUTTON = "LITHUANIA_BUTTON";
    static final String LATVIA_BUTTON = "LATVIA_BUTTON";
    // Беларуские пропускные пункты
    static final String BERESTOVICA_BUTTON = "BERESTOVICA_BUTTON";
    static final String KOZLOVICHY_BUTTON = "KOZLOVICHY_BUTTON";
    static final String BREST_BUTTON = "BREST_BUTTON";
    static final String PRIVALKA_BUTTON = "PRIVALKA_BUTTON";
    static final String BENYAKONI_BUTTON = "BENYAKONY_BUTTON";
    static final String KAMENNIY_LOG_BUTTON = "KAMENNIY_LOG_BUTTON";
    static final String KOTLOVKA_BUTTON = "KOTLOVKA_BUTTON";
    static final String VIDZY_BUTTON = "VIDZY_BUTTON";
    static final String LOSHA_BUTTON = "LOSHA_BUTTON";
    static final String GRIGOROVSHCHINA_BUTTON = "GRIGOROVSHCHINA_BUTTON";
    static final String URBANY_BUTTON = "URBANY_BUTTON";
    // Польские пропускные пункты
    static  final String BOBROWNIKI_BUTTON = "BOBROWNIKI_BUTTON";
    static  final String TERESPOL_BUTTON = "TERESPOL_BUTTON";
    static  final String KUKURIKI_BUTTON = "KUKURIKI_BUTTON";
    // Литовские пропускные пункты
    static final String TVYARYACHUS_BUTTON = "TVYARYACHUS_BUTTON";
    static final String RAYGARDAS_BUTTON = "RAYGARDAS_BUTTON";
    static final String SHALCHININKAI_BUTTON = "SHALCHININKAI_BUTTON";
    static final String MYADININKAI_BUTTON = "MYADININKAI_BUTTON";
    static final String LAVORISHSKES_BUTTON = "LAVORISHSKES_BUTTON";
    static final String SHUMSKAS_BUTTON = "SHUMSKAS_BUTTON";
    // Латвийские пропускные пункты
    static final String PATERNIEKI_BUTTON = "PATERNIEKI_BUTTON";
    static final String SILENE_BUTTON = "SILENE_BUTTON";
    // Ссылки на таможенные ресурсы Беларуси
    static final String linkBerestovica = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/berestovitsa/";
    static final String linkKozlovichi = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/kozlovichi/";
    static final String linkBrest = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/brest/";
    static final String linkPrivalka = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/privalka/";
    static final String linkBenyakoni = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/benekainys/";
    static final String linkKamenniyLog = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/stone_log/";
    static final String linkKotlovka = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/kotlovka/";
    static final String linkVidzy = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/vidzy/";
    static final String linkLosha = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/losha/";
    static final String linkGrigorovshchina = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/grigorov/";
    static final String linkUrbany = "https://gpk.gov.by/situation-at-the-border/punkty-propuska/urbana/";
    //Ссылки на таможенные ресурсы Литвы
    static final String linkVideoMyadininkai = "https://frame.pkpd.lt/lt/border/stream/checkpoint.medininkai/video.medininkai_pkp";
    static final String linkVideoLavorishskes = "https://frame.pkpd.lt/lt/border/stream/checkpoint.lavoriskes/video.lavoriskes";
    static final String linkVideoShumskas = "https://frame.pkpd.lt/lt/border/stream/checkpoint.sumskas/video.sumskas";
    static final String linkVideoShalchininkay1 = "https://frame.pkpd.lt/lt/border/stream/checkpoint.salcininkai/video.salcininkai";
    static final String linkVideoShalchininkay2 = "https://frame.pkpd.lt/lt/border/stream/checkpoint.salcininkai/video.salcininkai_pkp";
    static final String linkVideoRaygardas = "https://frame.pkpd.lt/lt/border/stream/checkpoint.raigardas/video.raigardas";
    //Ссылки на таможенные ресурсы Польши
    static final String linkPoland = "https://granica.gov.pl/index_wait.php?p=b&c=t&v=pl&k=w";
    final BotConfig config;
    final static String donateButtonText = "Если вам понравился этот проект и вы хотите поддержать разработчика, нажмите на кнопку *Поддержать* внизу.";
    final static String startButtonText = "Выберите страну, через пропускной пункт которой вы хотите пройти границу.";
    final static String belarusBorderButton = "Выберите пропускной пункт из списка который вас интересует.";
    final static String helpText = "Для вашего удобства выберите команду */start* в меню, " +
            "которая перенаправит вас в начало работы с ботом. С этой точки вы можете снова выбрать нужные вам страну и пропускной пункт.\n\n" +
            "Если у вас есть идеи или полезные ссылки, которые помогут улучшить работу этого бота, " +
            "напишите мне на email:\n\n" +
            "rakhmanmikalai@gmail.com\n\n";
    final static String developerText = "Меня зовут Рахман Николай. Я из Беларуси. Недавно переехал в Польшу (Варшава) " +
            "и самостоятельно изучаю язык JAVA и технологии, которые с ним связаны." +
            " Я решил написать этот бот для того, чтобы получить какой-то опыт разработки. \n\n" +
            "Ищу возможность трудоустройства в качестве интерна или джуна. Если вы можете мне с этим помочь," +
            " буду очень рад!" +
            "\n\nМои контакты: \ntel. +48 572 068 230" +
            "\nemail: rakhmanmikalai@gmail.com\n" +
            "LinkedIn: linkedin.com/in/mikalai-rakhman-7b572190";
    public TelegramBot(UserRepository userRepository, BotConfig config) {
        this.userRepository = userRepository;
        this.config=config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "перезапустить бот (начать сначала)"));
        listOfCommands.add(new BotCommand("/help", "как пользоваться ботом"));
        listOfCommands.add(new BotCommand("/developer", "информация о разработчике"));
        listOfCommands.add(new BotCommand("/donate", "поддержать проект"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list" + e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    donate(chatId);
                case "/border" :
                    border(chatId);
                    break;
                case "/belarusborder" :
                    belarusborder(chatId);
                    break;
                case "/polandborder" :
                    polandborder(chatId);
                    break;
                case "/lithuaniaborder" :
                    lithuaniaborder(chatId);
                    break;
                case "/latviaborder" :
                    latviaborder(chatId);
                    break;
                case "/help":
                    sendMessage(chatId, helpText);
                    break;
                case "/donate":
                    donateCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/developer":
                    sendMessage(chatId, developerText);
                    break;
                default: sendMessage(chatId, "Такой команды не существует. Воспользуйтесь основным меню и выберете поле перезапустить бот.");
            }
        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

                if (callBackData.equals(BELARUS_BUTTON)) {
                    belarusborder(chatId);
                } else if (callBackData.equals(POLAND_BUTTON)) {
                    polandborder(chatId);
                } else if (callBackData.equals(LITHUANIA_BUTTON)) {
                    lithuaniaborder(chatId);
                } else if (callBackData.equals(LATVIA_BUTTON)) {
                    latviaborder(chatId);
                } else if (callBackData.equals(BERESTOVICA_BUTTON)) {
                    sendMessage(chatId, "ВНИМАНИЕ! ПУНКТ ПРОПУСКА ВРЕМЕННО ЗАКРЫТ С ПОЛЬСКОЙ СТОРОНЫ!\nБЕРЕСТОВИЦА\n" + linkBerestovica);
                } else if (callBackData.equals(KOZLOVICHY_BUTTON)) {
                    sendMessage(chatId, "КОЗЛОВИЧИ\n" + linkKozlovichi);
                } else if (callBackData.equals(BREST_BUTTON)) {
                    sendMessage(chatId, "БРЕСТ\n" + linkBrest);
                } else if (callBackData.equals(PRIVALKA_BUTTON)) {
                    sendMessage(chatId, "ПРИВАЛКА\n" + linkPrivalka);
                } else if (callBackData.equals(BENYAKONI_BUTTON)) {
                    sendMessage(chatId, "БЕНЯКОНИ\n" + linkBenyakoni);
                } else if (callBackData.equals(KAMENNIY_LOG_BUTTON)) {
                    sendMessage(chatId, "КАМЕННЫЙ ЛОГ\n" + linkKamenniyLog);
                } else if (callBackData.equals(KOTLOVKA_BUTTON)) {
                    sendMessage(chatId, "КОТЛОВКА\n" + linkKotlovka);
                } else if (callBackData.equals(VIDZY_BUTTON)) {
                    sendMessage(chatId, "ВИДЗЫ\n" + linkVidzy);
                } else if (callBackData.equals(LOSHA_BUTTON)) {
                    sendMessage(chatId, "ЛОША\n" + linkLosha);
                } else if (callBackData.equals(GRIGOROVSHCHINA_BUTTON)) {
                    sendMessage(chatId, "ГРИГОРОВЩИНА\n" + linkGrigorovshchina);
                } else if (callBackData.equals(URBANY_BUTTON)) {
                    sendMessage(chatId, "УРБАНЫ\n" + linkUrbany);
                } else if (callBackData.equals(BOBROWNIKI_BUTTON)) {
                    sendMessage(chatId, "ВНИМАНИЕ! ПУНКТ ПРОПУСКА ВРЕМЕННО ЗАКРЫТ С ПОЛЬСКОЙ СТОРОНЫ!\nСИТУАЦИЯ НА ПОЛЬСКО-БЕЛАРУСКОЙ ГРАНИЦЕ\n" + linkPoland);
                } else if (callBackData.equals(TERESPOL_BUTTON)) {
                    sendMessage(chatId, "СИТУАЦИЯ НА ПОЛЬСКО-БЕЛАРУСКОЙ ГРАНИЦЕ\n" + linkPoland);
                } else if (callBackData.equals(KUKURIKI_BUTTON)) {
                    sendMessage(chatId, "СИТУАЦИЯ НА ПОЛЬСКО-БЕЛАРУСКОЙ ГРАНИЦЕ\n" + linkPoland);
                } else if (callBackData.equals(TVYARYACHUS_BUTTON)) {
                    sendMessage(chatId, "ПО ДАННОМУ ПРОПУСКНОМУ ПУНКТУ ИНФОРМАЦИЯ ОТСТУТСТВУЕТ");
                } else if (callBackData.equals(RAYGARDAS_BUTTON)) {
                    sendMessage(chatId, "КАМЕРА РАЙГАРДАС КПП\n" + linkVideoRaygardas);
                } else if (callBackData.equals(SHALCHININKAI_BUTTON)) {
                    sendMessage(chatId, "КАМЕРА ШАЛЬЧИНИНКАЙ КПП\n" + linkVideoShalchininkay1);
                    sendMessage(chatId, "КАМЕРА ШАЛЬЧИНИНКАЙ ЗОНА ОЖИДАНИЯ\n" + linkVideoShalchininkay2);
                } else if (callBackData.equals(MYADININKAI_BUTTON)) {
                    sendMessage(chatId, "КАМЕРА МЯДИНИНКАЙ ЗОНА ОЖИДАНИЯ\n" + linkVideoMyadininkai);
                } else if (callBackData.equals(LAVORISHSKES_BUTTON)) {
                    sendMessage(chatId, "КАМЕРА ЛАВОРИШСКЕС КПП\n" + linkVideoLavorishskes);
                } else if (callBackData.equals(SHUMSKAS_BUTTON)) {
                    sendMessage(chatId, "КАМЕРА ШУМСКАС КПП\n" + linkVideoShumskas);
                } else if (callBackData.equals(PATERNIEKI_BUTTON)) {
                    sendMessage(chatId, "ПО ДАННОМУ ПРОПУСКНОМУ ПУНКТУ ИНФОРМАЦИЯ ОТСТУТСТВУЕТ");
                } else if (callBackData.equals(SILENE_BUTTON)) {
                    sendMessage(chatId, "ПО ДАННОМУ ПРОПУСКНОМУ ПУНКТУ ИНФОРМАЦИЯ ОТСТУТСТВУЕТ");
                } else if (callBackData.equals(DONATE_BUTTON)) {
                    donateCommandReceived(chatId, update.getCallbackQuery().getMessage().getChat().getFirstName());
                }
        }

    }



    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }
    private void donate(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(donateButtonText);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();

        var donateButton = new InlineKeyboardButton();
        donateButton.setText("Поддержать");
        donateButton.setCallbackData(DONATE_BUTTON);

        rowInLine1.add(donateButton);
        rowsInline.add(rowInLine1);
        markupInLine.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }
    private void border(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(startButtonText);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine4 = new ArrayList<>();


        var belarusButton = new InlineKeyboardButton();
        belarusButton.setText("Беларусь");
        belarusButton.setCallbackData(BELARUS_BUTTON);

        var polandButton = new InlineKeyboardButton();
        polandButton.setText("Польша");
        polandButton.setCallbackData(POLAND_BUTTON);

        var lithuaniaButton = new InlineKeyboardButton();
        lithuaniaButton.setText("Литва");
        lithuaniaButton.setCallbackData(LITHUANIA_BUTTON);

        var latviaButton = new InlineKeyboardButton();
        latviaButton.setText("Латвия");
        latviaButton.setCallbackData(LATVIA_BUTTON);

        rowInLine1.add(belarusButton);
        rowInLine2.add(polandButton);
        rowInLine3.add(lithuaniaButton);
        rowInLine4.add(latviaButton);

        rowsInline.add(rowInLine1);
        rowsInline.add(rowInLine2);
        rowsInline.add(rowInLine3);
        rowsInline.add(rowInLine4);

        markupInLine.setKeyboard(rowsInline);

        message.setReplyMarkup(markupInLine);

        executeMessage(message);

    }

    private void belarusborder(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(belarusBorderButton);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine4 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine5 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine6 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine7 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine8 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine9 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine10 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine11 = new ArrayList<>();


        var berestovicaButton = new InlineKeyboardButton();
        berestovicaButton.setText("Берестовица (Польша)");
        berestovicaButton.setCallbackData(BERESTOVICA_BUTTON);

        var kozlovichyButton = new InlineKeyboardButton();
        kozlovichyButton.setText("Козловичи (Польша)");
        kozlovichyButton.setCallbackData(KOZLOVICHY_BUTTON);

        var brestButton = new InlineKeyboardButton();
        brestButton.setText("Брест (Польша)");
        brestButton.setCallbackData(BREST_BUTTON);

        var privalkaButton = new InlineKeyboardButton();
        privalkaButton.setText("Привалка (Литва)");
        privalkaButton.setCallbackData(PRIVALKA_BUTTON);

        var benyakoniButton = new InlineKeyboardButton();
        benyakoniButton.setText("Бенякони (Литва)");
        benyakoniButton.setCallbackData(BENYAKONI_BUTTON);

        var kamenniyLogButton = new InlineKeyboardButton();
        kamenniyLogButton.setText("Каменный Лог (Литва)");
        kamenniyLogButton.setCallbackData(KAMENNIY_LOG_BUTTON);

        var kotlovkaButton = new InlineKeyboardButton();
        kotlovkaButton.setText("Котловка (Литва)");
        kotlovkaButton.setCallbackData(KOTLOVKA_BUTTON);

        var vidzyButton = new InlineKeyboardButton();
        vidzyButton.setText("Видзы (Литва)");
        vidzyButton.setCallbackData(VIDZY_BUTTON);

        var loshaButton = new InlineKeyboardButton();
        loshaButton.setText("Лоша (Литва)");
        loshaButton.setCallbackData(LOSHA_BUTTON);

        var grigorovshchinaButton = new InlineKeyboardButton();
        grigorovshchinaButton.setText("Григоровщина (Латвия)");
        grigorovshchinaButton.setCallbackData(GRIGOROVSHCHINA_BUTTON);

        var urbanyButton = new InlineKeyboardButton();
        urbanyButton.setText("Урбаны (Латвия)");
        urbanyButton.setCallbackData(URBANY_BUTTON);


        rowInLine1.add(berestovicaButton);
        rowInLine2.add(kozlovichyButton);
        rowInLine3.add(brestButton);
        rowInLine4.add(privalkaButton);
        rowInLine5.add(benyakoniButton);
        rowInLine6.add(kamenniyLogButton);
        rowInLine7.add(kotlovkaButton);
        rowInLine8.add(vidzyButton);
        rowInLine9.add(loshaButton);
        rowInLine10.add(grigorovshchinaButton);
        rowInLine11.add(urbanyButton);


        rowsInline.add(rowInLine1);
        rowsInline.add(rowInLine2);
        rowsInline.add(rowInLine3);
        rowsInline.add(rowInLine4);
        rowsInline.add(rowInLine5);
        rowsInline.add(rowInLine6);
        rowsInline.add(rowInLine7);
        rowsInline.add(rowInLine8);
        rowsInline.add(rowInLine9);
        rowsInline.add(rowInLine10);
        rowsInline.add(rowInLine11);

        markupInLine.setKeyboard(rowsInline);

        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }
    private void polandborder(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(belarusBorderButton);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();

        var bobrownikiButton = new InlineKeyboardButton();
        bobrownikiButton.setText("Бобровники");
        bobrownikiButton.setCallbackData(BOBROWNIKI_BUTTON);

        var terespolButton = new InlineKeyboardButton();
        terespolButton.setText("Тересполь");
        terespolButton.setCallbackData(TERESPOL_BUTTON);

        var kukurikiButton = new InlineKeyboardButton();
        kukurikiButton.setText("Кукурыки");
        kukurikiButton.setCallbackData(KUKURIKI_BUTTON);

        rowInLine1.add(bobrownikiButton);
        rowInLine2.add(terespolButton);
        rowInLine3.add(kukurikiButton);

        rowsInline.add(rowInLine1);
        rowsInline.add(rowInLine2);
        rowsInline.add(rowInLine3);

        markupInLine.setKeyboard(rowsInline);

        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }
    private void lithuaniaborder(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(belarusBorderButton);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine4 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine5 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine6 = new ArrayList<>();


        var tvyaryachusButton = new InlineKeyboardButton();
        tvyaryachusButton.setText("Твярячус");
        tvyaryachusButton.setCallbackData(TVYARYACHUS_BUTTON);

        var raygardasButton = new InlineKeyboardButton();
        raygardasButton.setText("Райгардас");
        raygardasButton.setCallbackData(RAYGARDAS_BUTTON);

        var shalchininkaiButton = new InlineKeyboardButton();
        shalchininkaiButton.setText("Шальчининкай");
        shalchininkaiButton.setCallbackData(SHALCHININKAI_BUTTON);

        var myadininkaiButton = new InlineKeyboardButton();
        myadininkaiButton.setText("Мядининкай");
        myadininkaiButton.setCallbackData(MYADININKAI_BUTTON);

        var lavorishskesiButton = new InlineKeyboardButton();
        lavorishskesiButton.setText("Лаворишскес");
        lavorishskesiButton.setCallbackData(LAVORISHSKES_BUTTON);

        var shumskasButton = new InlineKeyboardButton();
        shumskasButton.setText("Шумскас");
        shumskasButton.setCallbackData(SHUMSKAS_BUTTON);

        rowInLine1.add(tvyaryachusButton);
        rowInLine2.add(raygardasButton);
        rowInLine3.add(shalchininkaiButton);
        rowInLine4.add(myadininkaiButton);
        rowInLine5.add(lavorishskesiButton);
        rowInLine6.add(shumskasButton);

        rowsInline.add(rowInLine1);
        rowsInline.add(rowInLine2);
        rowsInline.add(rowInLine3);
        rowsInline.add(rowInLine4);
        rowsInline.add(rowInLine5);
        rowsInline.add(rowInLine6);

        markupInLine.setKeyboard(rowsInline);

        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }
    private void latviaborder(long chatId) {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(belarusBorderButton);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();

        var paterniekiButton = new InlineKeyboardButton();
        paterniekiButton.setText("Патерниеки");
        paterniekiButton.setCallbackData(PATERNIEKI_BUTTON);

        var sileneButton = new InlineKeyboardButton();
        sileneButton.setText("Силене");
        sileneButton.setCallbackData(SILENE_BUTTON);

        rowInLine1.add(paterniekiButton);
        rowInLine2.add(sileneButton);

        rowsInline.add(rowInLine1);
        rowsInline.add(rowInLine2);

        markupInLine.setKeyboard(rowsInline);

        message.setReplyMarkup(markupInLine);

        executeMessage(message);

    }



    private void registerUser(Message message) {
        if(userRepository.findById(message.getChatId()).isEmpty()) {
            var chatId = message.getChatId();
            var chat = message.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved " + user);

        }
    }

    private void startCommandReceived(long chatId, String name) {
            // Следующая строка - это код который добавляет "поддержку" смайлов в строке. Вместо самого смайла используется Shortcodes с сайта https://emojipedia.org/
            String answer = EmojiParser.parseToUnicode("Здравствуйте, " + name + ", рад приветствовать вас!" + " :slight_smile:" +" \n\n" +
                    "Вы запустили чат-бот, который поможет вам получить информацию об очередях на границе с Беларусью в данный момент. Надеюсь, " +
                    "эта информация будет полезной для вас!  \n \n ");
            log.info("Replied to user " + name);
            sendMessage(chatId, answer);
    }
    private void donateCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Спасибо, " + name + ", что решили поддержать наш проект!:heart: \n\n" +
                "На данный момент есть несколько способов это сделать: \n\n" +
                "Для тех, кто в Европе (перевести на банковский счет):\n" +
                "49 1020 1055 0000 9602 0555 0100\n\n" +
                "Для тех, кто в Польше (мобильный BLIK перевод по номеру телефона):\n" +
                "+48 572 068 230\n\n" +
                "Для тех, кто в Беларуси (перевести на банковский счет моей жены (Сбербанк)):\n" +
                "BY35BPSB3014F000000006825530\n\n" +
                "Или на номер карты моей жены:\n" +
                "5278 4100 2064 4147");
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        executeMessage(message);
    }
}
