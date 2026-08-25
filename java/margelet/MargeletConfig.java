package org.telegram.margelet;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.messenger.ApplicationLoader;

/**
 * Настройки форка. Держим их в отдельном файле и в своём разделе настроек,
 * чтобы правки не растекались по коду оригинала: чем меньше тронуто чужих
 * строк, тем проще будет подтягивать новые версии телеграма.
 */
public class MargeletConfig {

    private static final String PREFS = "margelet";

    public static final int INPUT_LINES_DEFAULT = 6;
    public static final float INPUT_TEXT_SIZE_DEFAULT = 18f;

    public static final String APP_NAME = "OakGram";

    public static final int ACCENT_PINK = 0xFFFF4081;
    public static final int ACCENT_PINK_DARK = 0xFFE91E63;

    /**
     * Номер этой сборки. По нему клиент понимает, что на гитхабе лежит версия
     * новее, и предлагает обновиться.
     *
     * Поднимать его надо руками при выпуске — вместе с номером в version.json.
     * Забыть про него значит выпустить сборку, которая всю жизнь будет
     * предлагать обновиться сама на себя.
     */
    public static final String APP_VERSION = "0.4";

    /**
     * Как часто спрашивать гитхаб про новую версию, в минутах. Ноль — не
     * спрашивать вовсе; проверить руками кнопкой можно и тогда.
     */
    public static final int UPDATE_INTERVAL_OFF = 0;
    public static final int UPDATE_INTERVAL_DEFAULT = 3;
    /** Значения для выбора: от трёх минут до суток. */
    public static final int[] UPDATE_INTERVALS = {3, 15, 60, 6 * 60, 24 * 60, UPDATE_INTERVAL_OFF};

    public static int updateIntervalMinutes() {
        return prefs().getInt("update_interval", UPDATE_INTERVAL_DEFAULT);
    }

    public static void setUpdateIntervalMinutes(int minutes) {
        prefs().edit().putInt("update_interval", minutes).apply();
    }

    /** Спрашивать ли самому. Кнопки «проверить сейчас» это не касается. */
    public static boolean updatesChecked() {
        return updateIntervalMinutes() > 0;
    }

    public static final String CHANNEL_URL = "https://t.me/margeletter";
    /**
     * Реквизиты для доната. Лежат здесь, а не в строках: это не перевод, а
     * данные владельца форка, и в каждом языке они одни и те же.
     */
    public static final String DONATE_YOOMONEY = "2204120143055305";
    public static final String DONATE_ROBLOX = "h4ru_456";
    /**
     * Страница пожертвований. В отличие от номера кошелька её не копируют, а
     * открывают: там уже готовая форма, и человеку не нужно никуда вставлять
     * цифры руками.
     */
    public static final String DONATE_PAGE = "https://yoomoney.ru/to/4100118196133693";
    /** Кому дарить подарок за звёзды. Ник нужен на случай, если номера нет в кэше. */
    public static final long DONATE_GIFT_USER = 7826361017L;
    public static final String DONATE_GIFT_USERNAME = "narezany";

    /** Свой набор стикеров: ставится обычной кнопкой, как любой другой набор. */
    public static final String STICKERS_URL = "https://t.me/addstickers/MargeletPackMargeletter";

    public static final String SOURCE_URL = "https://github.com/narezany/Margelet";
    public static final String FORUM_URL = "https://t.me/margeletforum";
    /**
     * Написать нам. Это тот же канал, но с ?direct: телеграм открывает не
     * ленту, а поле для сообщения — человек с жалобой не должен искать, куда
     * её деть.
     */
    public static final String FEEDBACK_URL = "https://t.me/margeletter?direct";
    /**
     * Документация по плагинам. Ведёт на файл в репозитории, а не на страницу
     * сайта: страницы гитхаба у репозитория не включены, а ссылка на
     * несуществующий сайт — просто обман.
     *
     * Язык берётся из приложения. Английская — та, что без суффикса: она же
     * открывается по ссылке из README, и с неё есть переходы на остальные.
     */
    public static String pluginsDocsUrl() {
        final String base = "https://github.com/narezany/Margelet/blob/main/docs/plugins";
        String language = null;
        try {
            language = org.telegram.messenger.LocaleController.getInstance()
                    .getCurrentLocale().getLanguage();
        } catch (Exception ignored) {
        }
        if ("ru".equals(language) || "zh".equals(language)) {
            return base + "." + language + ".md";
        }
        return base + ".md";
    }

    private static SharedPreferences prefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /**
     * До скольких строк растёт поле ввода, прежде чем начать прокручиваться.
     * Ноль означает «расти, пока есть место на экране»: сам EditText выше
     * своего контейнера не станет, так что бесконечности тут не будет — будет
     * ровно высота экрана, о которой и просили.
     */
    public static int inputMaxLines() {
        int v = inputMaxLinesRaw();
        return v <= 0 ? Integer.MAX_VALUE : v;
    }

    /**
     * То же значение, но как оно записано: ноль остаётся нулём. Экрану
     * настроек нужно именно это — иначе «без предела» пришлось бы угадывать
     * по двум миллиардам строк.
     */
    public static int inputMaxLinesRaw() {
        return prefs().getInt("input_max_lines", INPUT_LINES_DEFAULT);
    }

    public static void setInputMaxLines(int lines) {
        prefs().edit().putInt("input_max_lines", lines).apply();
    }

    /** Размер текста в поле ввода, в тех же единицах, что и в оригинале. */
    public static float inputTextSize() {
        return prefs().getFloat("input_text_size", INPUT_TEXT_SIZE_DEFAULT);
    }

    public static void setInputTextSize(float sp) {
        prefs().edit().putFloat("input_text_size", sp).apply();
    }

    /** Поле ввода сверху экрана, а не снизу. */
    public static boolean inputOnTop() {
        return prefs().getBoolean("input_on_top", false);
    }

    public static void setInputOnTop(boolean top) {
        prefs().edit().putBoolean("input_on_top", top).apply();
    }

    /**
     * Удалённые подарки в каталоге. По умолчанию выключено: это добавляет к
     * списку то, чего телеграм там не показывает, и включать такое за человека
     * молча неправильно.
     */
    public static boolean giftsEnabled() {
        return prefs().getBoolean("gifts", false);
    }

    public static void setGiftsEnabled(boolean on) {
        prefs().edit().putBoolean("gifts", on).apply();
    }

    /**
     * Виды своего оформления по отдельности. Всё включено по умолчанию:
     * выключение — это про «мне мешает», а не про «покажите сначала».
     */
    public static boolean markupEnabled(int kind) {
        return prefs().getBoolean("markup_" + kind, true);
    }

    public static void setMarkupEnabled(int kind, boolean on) {
        prefs().edit().putBoolean("markup_" + kind, on).apply();
    }

    /**
     * Показывать ли строку со ссылкой на форк в чужих сообщениях. По умолчанию
     * нет: внутри форка она и так ни к чему, она для тех, у кого форка нет.
     */
    public static boolean showWatermarks() {
        return prefs().getBoolean("watermarks", false);
    }

    public static void setShowWatermarks(boolean on) {
        prefs().edit().putBoolean("watermarks", on).apply();
    }

    /**
     * Разметка значками по видам: **жирный**, __курсив__ и прочие, включая
     * свои — ++подчёркнутый++ и цитаты через «больше». Всё включено по
     * умолчанию; выключенный вид остаётся в тексте как есть.
     */
    public static boolean markdownEnabled(String kind) {
        return prefs().getBoolean("markdown_" + kind, true);
    }

    public static void setMarkdownEnabled(String kind, boolean on) {
        prefs().edit().putBoolean("markdown_" + kind, on).apply();
    }

    /**
     * Держать ли канал форка первой строкой в списке чатов. По умолчанию да:
     * узнать о том, что происходит с форком, больше особо неоткуда.
     */
    public static boolean channelOnTop() {
        return prefs().getBoolean("channel_on_top", true);
    }

    public static void setChannelOnTop(boolean on) {
        prefs().edit().putBoolean("channel_on_top", on).apply();
    }

    /**
     * Показывать ли относительное время последнего посещения («был 5 минут назад»)
     * вместо абсолютного времени («был сегодня в 7:55»).
     */
    public static boolean relativeOnlineTime() {
        return prefs().getBoolean("relative_online_time", false);
    }

    public static void setRelativeOnlineTime(boolean on) {
        prefs().edit().putBoolean("relative_online_time", on).apply();
    }

    /**
     * Дописывать ли строку со ссылкой на форк в свои оформленные сообщения.
     * По умолчанию да: форк живёт тем, что о нём узнают.
     */
    public static boolean watermarkOnSend() {
        return prefs().getBoolean("watermark_send", false);
    }

    public static void setWatermarkOnSend(boolean on) {
        prefs().edit().putBoolean("watermark_send", on).apply();
    }

    /** Премиум-значки без премиума: видны только в форке. */
    public static boolean freeEmoji() {
        return prefs().getBoolean("free_emoji", true);
    }

    public static void setFreeEmoji(boolean on) {
        prefs().edit().putBoolean("free_emoji", on).apply();
    }

    public static boolean emojiWarned() {
        return prefs().getBoolean("emoji_warned", false);
    }

    public static void setEmojiWarned(boolean value) {
        prefs().edit().putBoolean("emoji_warned", value).apply();
    }

    /** Пункт «Копировать с оформлением» в меню сообщения. */
    public static boolean copyFormatting() {
        return prefs().getBoolean("copy_formatting", true);
    }

    public static void setCopyFormatting(boolean on) {
        prefs().edit().putBoolean("copy_formatting", on).apply();
    }

    /** Показывали ли предупреждение о своём оформлении. Один раз за всё время. */
    public static boolean markupWarned() {
        return prefs().getBoolean("markup_warned", false);
    }

    public static void setMarkupWarned(boolean value) {
        prefs().edit().putBoolean("markup_warned", value).apply();
    }

    /** Значки форка у имён. Включены по умолчанию: без них форк выглядит чужим. */
    public static boolean badgesEnabled() {
        return prefs().getBoolean("badges", true);
    }

    public static void setBadgesEnabled(boolean on) {
        prefs().edit().putBoolean("badges", on).apply();
    }

    /** Показывать айди в профилях людей, групп, каналов и ботов. */
    public static boolean showIds() {
        return prefs().getBoolean("show_ids", true);
    }

    public static void setShowIds(boolean on) {
        prefs().edit().putBoolean("show_ids", on).apply();
    }

    /**
     * «Приступ»: весь текст переливается радугой. Выключено по умолчанию и
     * включается только через предупреждение — мигающая картинка бывает опасна
     * не в переносном смысле.
     */
    public static boolean seizure() {
        return prefs().getBoolean("seizure", false);
    }

    public static void setSeizure(boolean on) {
        prefs().edit().putBoolean("seizure", on).apply();
    }

    /** Режим стримера: прятать номер телефона. */
    public static boolean streamerMode() {
        return prefs().getBoolean("streamer", false);
    }

    public static void setStreamerMode(boolean on) {
        prefs().edit().putBoolean("streamer", on).apply();
    }

    public static boolean streamerHidesOthers() {
        return prefs().getBoolean("streamer_others", false);
    }

    public static void setStreamerHidesOthers(boolean on) {
        prefs().edit().putBoolean("streamer_others", on).apply();
    }

    public static boolean streamerHidesUsername() {
        return prefs().getBoolean("streamer_username", false);
    }

    public static void setStreamerHidesUsername(boolean on) {
        prefs().edit().putBoolean("streamer_username", on).apply();
    }

    /** Правка тегов у музыки. По умолчанию включена. */
    public static boolean tagsEnabled() {
        return prefs().getBoolean("tags_enabled", true);
    }

    public static void setTagsEnabled(boolean enabled) {
        prefs().edit().putBoolean("tags_enabled", enabled).apply();
    }

    /**
     * Слышал ли человек мяуканье хоть раз. До этого раздела «Звук» в
     * настройках нет: настраивать то, о существовании чего не знаешь, незачем,
     * а найденная случайно шутка тем и хороша, что найдена.
     */
    public static boolean meowHeard() {
        return prefs().getBoolean("meow_heard", false);
    }

    public static void setMeowHeard() {
        prefs().edit().putBoolean("meow_heard", true).apply();
    }

    /** Мяуканье по нажатию на название — можно выключить совсем. */
    public static boolean meowEnabled() {
        return prefs().getBoolean("meow_enabled", true);
    }

    public static void setMeowEnabled(boolean enabled) {
        prefs().edit().putBoolean("meow_enabled", enabled).apply();
    }

    /**
     * Путь к своему звуку. Пусто — играет тот, что лежит в сборке. Файл
     * копируется к нам при выборе: ссылка на чужой файл живёт до первой
     * уборки в галерее, а копия — сколько нужно.
     */
    public static String meowPath() {
        return prefs().getString("meow_path", null);
    }

    public static void setMeowPath(String path) {
        if (path == null) {
            prefs().edit().remove("meow_path").apply();
        } else {
            prefs().edit().putString("meow_path", path).apply();
        }
    }

    /**
     * Первый запуск. Нужен, чтобы один раз включить тёмно-зелёную тему и
     * больше в выбор темы не лезть: если человек потом поставит другую, наше
     * дело в это не вмешиваться.
     */
    public static boolean claimFirstLaunch() {
        if (prefs().getBoolean("first_launch_done", false)) {
            return false;
        }
        prefs().edit().putBoolean("first_launch_done", true).apply();
        return true;
    }

    /**
     * Главный выключатель плагинов. По умолчанию выключен: плагин выполняется
     * внутри приложения и может всё, что может приложение, — такое не
     * включают за человека.
     */
    public static boolean pluginsEnabled() {
        return prefs().getBoolean("plugins_enabled", false);
    }

    public static void setPluginsEnabled(boolean on) {
        prefs().edit().putBoolean("plugins_enabled", on).apply();
    }

    /** Включён ли отдельный плагин. Новый плагин всегда выключен. */
    public static boolean pluginEnabled(String id) {
        return prefs().getBoolean("plugin_" + id, false);
    }

    public static void setPluginEnabled(String id, boolean on) {
        prefs().edit().putBoolean("plugin_" + id, on).apply();
    }

    /**
     * Пример плагина кладётся в папку один раз. Если человек его удалил,
     * второй раз он не появится: удаление — это ответ, а не случайность.
     */
    public static boolean claimExamplePlugin() {
        if (prefs().getBoolean("plugin_example_done", false)) {
            return false;
        }
        prefs().edit().putBoolean("plugin_example_done", true).apply();
        return true;
    }

    public static boolean filterZalgo() {
        return prefs().getBoolean("filter_zalgo", false);
    }

    public static void setFilterZalgo(boolean on) {
        prefs().edit().putBoolean("filter_zalgo", on).apply();
    }

    public static boolean hideSendAsPeer() {
        return prefs().getBoolean("hide_send_as_peer", false);
    }

    public static void setHideSendAsPeer(boolean on) {
        prefs().edit().putBoolean("hide_send_as_peer", on).apply();
    }

    public static boolean hideBotButton() {
        return prefs().getBoolean("hide_bot_button", false);
    }

    public static void setHideBotButton(boolean on) {
        prefs().edit().putBoolean("hide_bot_button", on).apply();
    }

    public static int avatarRadius() {
        return prefs().getInt("avatar_radius", 0);
    }

    public static void setAvatarRadius(int radius) {
        prefs().edit().putInt("avatar_radius", radius).apply();
    }

    public static boolean classicDrawer() {
        return prefs().getBoolean("classic_drawer", false);
    }

    public static void setClassicDrawer(boolean on) {
        prefs().edit().putBoolean("classic_drawer", on).apply();
    }

    public static boolean hideAllChats() {
        return prefs().getBoolean("hide_all_chats", false);
    }

    public static void setHideAllChats(boolean hide) {
        prefs().edit().putBoolean("hide_all_chats", hide).apply();
    }

    public static boolean m3SwitchStyle() {
        return prefs().getBoolean("m3_switch_style", false);
    }

    public static void setM3SwitchStyle(boolean on) {
        prefs().edit().putBoolean("m3_switch_style", on).apply();
    }

    public static boolean hideBottomTabs() {
        return prefs().getBoolean("hide_bottom_tabs", false);
    }

    public static void setHideBottomTabs(boolean on) {
        prefs().edit().putBoolean("hide_bottom_tabs", on).apply();
    }

    public static boolean hideTabLabels() {
        return prefs().getBoolean("hide_tab_labels", false);
    }

    public static void setHideTabLabels(boolean on) {
        prefs().edit().putBoolean("hide_tab_labels", on).apply();
    }

    public static int glassOutlineStyle() {
        return prefs().getInt("glass_outline_style", 0); // 0: GLARE, 1: SOLID, 2: HIDDEN
    }

    public static void setGlassOutlineStyle(int style) {
        prefs().edit().putInt("glass_outline_style", style).apply();
    }

    public static boolean antiDelete() {
        return prefs().getBoolean("anti_delete", false);
    }

    public static void setAntiDelete(boolean on) {
        prefs().edit().putBoolean("anti_delete", on).apply();
    }

    public static boolean stripExif() { return prefs().getBoolean("oak_strip_exif", true); }
    public static void setStripExif(boolean on) { prefs().edit().putBoolean("oak_strip_exif", on).apply(); }

    public static boolean sanitizeLinks() { return prefs().getBoolean("oak_sanitize_links", true); }
    public static void setSanitizeLinks(boolean on) { prefs().edit().putBoolean("oak_sanitize_links", on).apply(); }

    public static boolean blockP2PCalls() { return prefs().getBoolean("oak_block_p2p", true); }
    public static void setBlockP2PCalls(boolean on) { prefs().edit().putBoolean("oak_block_p2p", on).apply(); }

    public static boolean disableLinkPreviews() { return prefs().getBoolean("oak_disable_link_previews", false); }
    public static void setDisableLinkPreviews(boolean on) { prefs().edit().putBoolean("oak_disable_link_previews", on).apply(); }

    public static boolean ghostNoTyping() { return prefs().getBoolean("oak_ghost_no_typing", false); }
    public static void setGhostNoTyping(boolean on) { prefs().edit().putBoolean("oak_ghost_no_typing", on).apply(); }

    public static boolean ghostStealthStories() { return prefs().getBoolean("oak_ghost_stealth_stories", false); }
    public static void setGhostStealthStories(boolean on) { prefs().edit().putBoolean("oak_ghost_stealth_stories", on).apply(); }

    public static boolean ghostStealthRead() { return prefs().getBoolean("oak_ghost_stealth_read", false); }
    public static void setGhostStealthRead(boolean on) { prefs().edit().putBoolean("oak_ghost_stealth_read", on).apply(); }

    public static boolean pluginFirewallEnabled() { return prefs().getBoolean("oak_plugin_firewall", true); }
    public static void setPluginFirewallEnabled(boolean on) { prefs().edit().putBoolean("oak_plugin_firewall", on).apply(); }

    public static boolean hideStories() { return prefs().getBoolean("oak_hide_stories", false); }
    public static void setHideStories(boolean on) { prefs().edit().putBoolean("oak_hide_stories", on).apply(); }

    public static boolean hideSendAs() { return prefs().getBoolean("oak_hide_send_as", false); }
    public static void setHideSendAs(boolean on) { prefs().edit().putBoolean("oak_hide_send_as", on).apply(); }

    public static boolean hideBotButtons() { return prefs().getBoolean("oak_hide_bot_buttons", false); }
    public static void setHideBotButtons(boolean on) { prefs().edit().putBoolean("oak_hide_bot_buttons", on).apply(); }

    public static boolean disableDoubleTapReaction() { return prefs().getBoolean("oak_no_double_tap", false); }
    public static void setDisableDoubleTapReaction(boolean on) { prefs().edit().putBoolean("oak_no_double_tap", on).apply(); }

    public static int bubbleCornersRadius() { return prefs().getInt("oak_bubble_radius", 16); }
    public static void setBubbleCornersRadius(int r) { prefs().edit().putInt("oak_bubble_radius", r).apply(); }

    public static boolean showUserIdDc() { return prefs().getBoolean("oak_show_id_dc", true); }
    public static void setShowUserIdDc(boolean on) { prefs().edit().putBoolean("oak_show_id_dc", on).apply(); }

    public static int avatarCorners() { return prefs().getInt("oak_avatar_shape", 0); }
    public static void setAvatarCorners(int s) { prefs().edit().putInt("oak_avatar_shape", s).apply(); }

    public static boolean disableHaptics() { return prefs().getBoolean("oak_no_haptics", false); }
    public static void setDisableHaptics(boolean on) { prefs().edit().putBoolean("oak_no_haptics", on).apply(); }

    public static boolean pauseMusicOnVoice() { return prefs().getBoolean("oak_pause_music", true); }
    public static void setPauseMusicOnVoice(boolean on) { prefs().edit().putBoolean("oak_pause_music", on).apply(); }

    public static boolean confirmVoiceSend() { return prefs().getBoolean("oak_confirm_voice", false); }
    public static void setConfirmVoiceSend(boolean on) { prefs().edit().putBoolean("oak_confirm_voice", on).apply(); }

    public static boolean rearCameraVideoNotes() { return prefs().getBoolean("oak_rear_video", false); }
    public static void setRearCameraVideoNotes(boolean on) { prefs().edit().putBoolean("oak_rear_video", on).apply(); }

    public static int voicePitch() { return prefs().getInt("oak_voice_pitch", 0); }
    public static void setVoicePitch(int p) { prefs().edit().putInt("oak_voice_pitch", p).apply(); }

    public static boolean unlimitedVoiceSpeed() { return prefs().getBoolean("oak_unlimited_speed", true); }
    public static void setUnlimitedVoiceSpeed(boolean on) { prefs().edit().putBoolean("oak_unlimited_speed", on).apply(); }

    public static boolean hideAllChatsTab() { return prefs().getBoolean("oak_hide_all_chats", false); }
    public static void setHideAllChatsTab(boolean on) { prefs().edit().putBoolean("oak_hide_all_chats", on).apply(); }

    public static boolean showTabCounters() { return prefs().getBoolean("oak_tab_counters", true); }
    public static void setShowTabCounters(boolean on) { prefs().edit().putBoolean("oak_tab_counters", on).apply(); }

    public static int blurIntensity() { return prefs().getInt("oak_blur_intensity", 50); }
    public static void setBlurIntensity(int val) { prefs().edit().putInt("oak_blur_intensity", val).apply(); }

    public static boolean sendByEnter() { return prefs().getBoolean("oak_send_by_enter", false); }
    public static void setSendByEnter(boolean on) { prefs().edit().putBoolean("oak_send_by_enter", on).apply(); }

    public static boolean useTor() { return prefs().getBoolean("oak_use_tor", false); }
    public static void setUseTor(boolean on) { prefs().edit().putBoolean("oak_use_tor", on).apply(); }

    public static String duressPin() { return prefs().getString("oak_duress_pin", ""); }
    public static void setDuressPin(String pin) { prefs().edit().putString("oak_duress_pin", pin != null ? pin.trim() : "").apply(); }
    public static boolean isDuressPin(String input) {
        String dp = duressPin();
        return !dp.isEmpty() && dp.equals(input != null ? input.trim() : "");
    }

    public static boolean blockScreenshots() { return prefs().getBoolean("oak_block_screenshots", true); }
    public static void setBlockScreenshots(boolean on) { prefs().edit().putBoolean("oak_block_screenshots", on).apply(); }

    public static boolean cleanForward() { return prefs().getBoolean("oak_clean_forward", true); }
    public static void setCleanForward(boolean on) { prefs().edit().putBoolean("oak_clean_forward", on).apply(); }

    public static boolean clearCacheOnExit() { return prefs().getBoolean("oak_clear_cache_exit", false); }
    public static void setClearCacheOnExit(boolean on) { prefs().edit().putBoolean("oak_clear_cache_exit", on).apply(); }

    public static boolean codeFormattingEnabled() { return prefs().getBoolean("oak_code_formatting", true); }
    public static void setCodeFormattingEnabled(boolean on) { prefs().edit().putBoolean("oak_code_formatting", on).apply(); }

    public static String stealthAccountPasscode() { return prefs().getString("oak_stealth_acc_pass", ""); }
    public static void setStealthAccountPasscode(String p) { prefs().edit().putString("oak_stealth_acc_pass", p != null ? p.trim() : "").apply(); }
    public static boolean isStealthUnlocked() { return prefs().getBoolean("oak_stealth_unlocked", false); }
    public static void setStealthUnlocked(boolean u) { prefs().edit().putBoolean("oak_stealth_unlocked", u).apply(); }
    public static boolean isAccountHidden(int id) { return prefs().getBoolean("oak_hidden_acc_" + id, false); }
    public static void setAccountHidden(int id, boolean h) { prefs().edit().putBoolean("oak_hidden_acc_" + id, h).apply(); }

    public static int dohProvider() { return prefs().getInt("oak_doh_provider", 1); }
    public static void setDohProvider(int p) { prefs().edit().putInt("oak_doh_provider", p).apply(); }

    public static boolean unlimitedPins() { return prefs().getBoolean("oak_unlimited_pins", true); }
    public static void setUnlimitedPins(boolean on) { prefs().edit().putBoolean("oak_unlimited_pins", on).apply(); }

    public static boolean maxQualityPhotos() { return prefs().getBoolean("oak_max_quality_photos", true); }
    public static void setMaxQualityPhotos(boolean on) { prefs().edit().putBoolean("oak_max_quality_photos", on).apply(); }

    public static boolean saveSelfDestructing() { return prefs().getBoolean("oak_save_self_destruct", true); }
    public static void setSaveSelfDestructing(boolean on) { prefs().edit().putBoolean("oak_save_self_destruct", on).apply(); }

    public static boolean boostDownloads() { return prefs().getBoolean("oak_boost_downloads", true); }
    public static void setBoostDownloads(boolean on) { prefs().edit().putBoolean("oak_boost_downloads", on).apply(); }

    public static boolean boostVoiceVolume() { return prefs().getBoolean("oak_boost_voice", false); }
    public static void setBoostVoiceVolume(boolean on) { prefs().edit().putBoolean("oak_boost_voice", on).apply(); }

    public static boolean disableProximitySensor() { return prefs().getBoolean("oak_no_proximity", false); }
    public static void setDisableProximitySensor(boolean on) { prefs().edit().putBoolean("oak_no_proximity", on).apply(); }

    public static boolean deleteForEveryoneDefault() { return prefs().getBoolean("oak_del_everyone", true); }
    public static void setDeleteForEveryoneDefault(boolean on) { prefs().edit().putBoolean("oak_del_everyone", on).apply(); }

    public static boolean showExactOnlineTime() { return prefs().getBoolean("oak_exact_online", true); }
    public static void setShowExactOnlineTime(boolean on) { prefs().edit().putBoolean("oak_exact_online", on).apply(); }

    public static boolean hidePhoneNumber() { return prefs().getBoolean("oak_hide_phone", true); }
    public static void setHidePhoneNumber(boolean on) { prefs().edit().putBoolean("oak_hide_phone", on).apply(); }

    public static boolean autoLoopVideos() { return prefs().getBoolean("oak_loop_videos", true); }
    public static void setAutoLoopVideos(boolean on) { prefs().edit().putBoolean("oak_loop_videos", on).apply(); }

    public static boolean inlineTranslation() { return prefs().getBoolean("oak_inline_translate", true); }
    public static void setInlineTranslation(boolean on) { prefs().edit().putBoolean("oak_inline_translate", on).apply(); }

    public static String targetTranslateLang() { return prefs().getString("oak_translate_lang", "ru"); }
    public static void setTargetTranslateLang(String lang) { prefs().edit().putString("oak_translate_lang", lang).apply(); }
}
