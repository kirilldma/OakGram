package org.telegram.ui;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import org.telegram.margelet.MargeletConfig;
import org.telegram.margelet.OakDns;
import org.telegram.margelet.OakPanic;
import org.telegram.margelet.OakTor;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import java.util.ArrayList;

public class OakPrivacyActivity extends UniversalFragment {

    private static final int ID_STRIP_EXIF = 1, ID_SANITIZE_LINKS = 2, ID_BLOCK_P2P = 3,
            ID_DISABLE_PREVIEWS = 4, ID_GHOST_TYPING = 5, ID_GHOST_STORIES = 6,
            ID_GHOST_READ = 7, ID_ANTI_DELETE = 8, ID_PLUGIN_FIREWALL = 9,
            ID_TOR_TRAFFIC = 10, ID_BLOCK_SCREENSHOTS = 11, ID_CLEAN_FORWARD = 12,
            ID_CLEAR_CACHE_EXIT = 13, ID_DURESS_PIN = 14, ID_STEALTH_ACC = 15,
            ID_PANIC_WIPE = 16;

    @Override
    protected CharSequence getTitle() {
        return "Приватность";
    }

    @Override
    public View createView(android.content.Context context) {
        View v = super.createView(context);
        listView.setSections();
        return v;
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asHeader("Метаданные и пересылка"));
        items.add(UItem.asCheck(ID_STRIP_EXIF, "Срезать EXIF с фото").setChecked(MargeletConfig.stripExif()));
        items.add(UItem.asShadow("Удаляет GPS, модель камеры и дату из отправляемых файлов."));
        items.add(UItem.asCheck(ID_SANITIZE_LINKS, "Чистить ссылки от UTM").setChecked(MargeletConfig.sanitizeLinks()));
        items.add(UItem.asShadow("Вырезает utm_, fbclid, si= и прочие трекеры."));
        items.add(UItem.asCheck(ID_CLEAN_FORWARD, "Анонимная пересылка").setChecked(MargeletConfig.cleanForward()));
        items.add(UItem.asShadow("Пересылает сообщения без указания автора и подписей."));
        items.add(UItem.asCheck(ID_ANTI_DELETE, "Анти-удаление сообщений").setChecked(MargeletConfig.antiDelete()));
        items.add(UItem.asShadow("Сохраняет удалённые собеседником сообщения локально."));

        items.add(UItem.asHeader("Экран и кэш"));
        items.add(UItem.asCheck(ID_BLOCK_SCREENSHOTS, "Запретить скриншоты (FLAG_SECURE)").setChecked(MargeletConfig.blockScreenshots()));
        items.add(UItem.asShadow("Блокирует скриншоты, запись экрана и превью в Recent Apps."));
        items.add(UItem.asCheck(ID_CLEAR_CACHE_EXIT, "Стирать кэш при уходе в фон").setChecked(MargeletConfig.clearCacheOnExit()));
        items.add(UItem.asShadow("Авто-очистка временных медиа и кэшей при скрытии приложения."));

        items.add(UItem.asHeader("Сеть, TOR и DoH"));
        items.add(UItem.asCheck(ID_TOR_TRAFFIC, "Пустить трафик через TOR (127.0.0.1:9050)").setChecked(MargeletConfig.useTor()));
        items.add(UItem.asShadow("Авто-маршрутизация всего трафика через локальный SOCKS5 демон Tor / Orbot."));
        items.add(UItem.asHeader("Защищённый DNS (DNS-over-HTTPS)"));
        items.add(UItem.asSlideView(OakDns.DOH_PROVIDERS, MargeletConfig.dohProvider(), i -> MargeletConfig.setDohProvider(i)));
        items.add(UItem.asShadow("Шифрование DNS-запросов через Mullvad, Quad9 или Cloudflare."));
        items.add(UItem.asCheck(ID_BLOCK_P2P, "Блокировать P2P в звонках").setChecked(MargeletConfig.blockP2PCalls()));
        items.add(UItem.asShadow("Защищает реальный IP в звонках, пуская трафик через сервера."));
        items.add(UItem.asCheck(ID_DISABLE_PREVIEWS, "Не грузить превью ссылок").setChecked(MargeletConfig.disableLinkPreviews()));
        items.add(UItem.asShadow("Не делает фоновых запросов к чужим сайтам при вставке ссылки."));

        items.add(UItem.asHeader("Скрытность"));
        String stealthStatus = MargeletConfig.stealthAccountPasscode().isEmpty() ? "Не задан" : "Код активен";
        items.add(UItem.asButton(ID_STEALTH_ACC, "Скрытые аккаунты (Stealth Accounts)", stealthStatus));
        items.add(UItem.asShadow("Ввод кодового слова в общем поиске показывает скрытые профили."));
        items.add(UItem.asCheck(ID_GHOST_TYPING, "Не слать «печатает...»").setChecked(MargeletConfig.ghostNoTyping()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_GHOST_STORIES, "Смотреть истории анонимно").setChecked(MargeletConfig.ghostStealthStories()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_GHOST_READ, "Не отмечать прочитанным").setChecked(MargeletConfig.ghostStealthRead()));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("Плагины"));
        items.add(UItem.asCheck(ID_PLUGIN_FIREWALL, "Изолировать плагины от сети").setChecked(MargeletConfig.pluginFirewallEnabled()));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("Экстренно"));
        String pinStatus = MargeletConfig.duressPin().isEmpty() ? "Не задан" : "Включён";
        items.add(UItem.asButton(ID_DURESS_PIN, "Пин-код под принуждением (Duress PIN)", pinStatus));
        items.add(UItem.asShadow("Ввод этого PIN при разблокировке мгновенно стирает все данные."));
        items.add(UItem.asButton(ID_PANIC_WIPE, "Стереть всё и закрыть", "Удалить кэши, базы и сессии"));
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == ID_STRIP_EXIF) MargeletConfig.setStripExif(!MargeletConfig.stripExif());
        else if (item.id == ID_SANITIZE_LINKS) MargeletConfig.setSanitizeLinks(!MargeletConfig.sanitizeLinks());
        else if (item.id == ID_CLEAN_FORWARD) MargeletConfig.setCleanForward(!MargeletConfig.cleanForward());
        else if (item.id == ID_ANTI_DELETE) MargeletConfig.setAntiDelete(!MargeletConfig.antiDelete());
        else if (item.id == ID_BLOCK_SCREENSHOTS) MargeletConfig.setBlockScreenshots(!MargeletConfig.blockScreenshots());
        else if (item.id == ID_CLEAR_CACHE_EXIT) MargeletConfig.setClearCacheOnExit(!MargeletConfig.clearCacheOnExit());
        else if (item.id == ID_TOR_TRAFFIC) OakTor.apply(!MargeletConfig.useTor());
        else if (item.id == ID_BLOCK_P2P) MargeletConfig.setBlockP2PCalls(!MargeletConfig.blockP2PCalls());
        else if (item.id == ID_DISABLE_PREVIEWS) MargeletConfig.setDisableLinkPreviews(!MargeletConfig.disableLinkPreviews());
        else if (item.id == ID_GHOST_TYPING) MargeletConfig.setGhostNoTyping(!MargeletConfig.ghostNoTyping());
        else if (item.id == ID_GHOST_STORIES) MargeletConfig.setGhostStealthStories(!MargeletConfig.ghostStealthStories());
        else if (item.id == ID_GHOST_READ) MargeletConfig.setGhostStealthRead(!MargeletConfig.ghostStealthRead());
        else if (item.id == ID_PLUGIN_FIREWALL) MargeletConfig.setPluginFirewallEnabled(!MargeletConfig.pluginFirewallEnabled());
        else if (item.id == ID_STEALTH_ACC) promptStealthPasscode();
        else if (item.id == ID_DURESS_PIN) promptDuressPin();
        else if (item.id == ID_PANIC_WIPE) confirmPanic();
        if (item.id != ID_PANIC_WIPE && item.id != ID_DURESS_PIN && item.id != ID_STEALTH_ACC) listView.adapter.update(true);
    }

    private void promptStealthPasscode() {
        if (getContext() == null) return;
        EditText input = new EditText(getContext());
        input.setHint("Кодовое слово (например: secret)");
        new AlertDialog.Builder(getContext())
                .setTitle("Код для скрытых аккаунтов")
                .setMessage("Введите секретное слово, которое при вводе в поиск покажет скрытые аккаунты:")
                .setView(input)
                .setPositiveButton("Сохранить", (d, w) -> {
                    MargeletConfig.setStealthAccountPasscode(input.getText().toString());
                    listView.adapter.update(true);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void promptDuressPin() {
        if (getContext() == null) return;
        EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setHint("4-6 цифр");
        new AlertDialog.Builder(getContext())
                .setTitle("Задать Duress PIN")
                .setMessage("При вводе этого кода на экране блокировки клиент немедленно зачистит базы, сессии и закроется.")
                .setView(input)
                .setPositiveButton("Сохранить", (d, w) -> {
                    MargeletConfig.setDuressPin(input.getText().toString());
                    listView.adapter.update(true);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void confirmPanic() {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Стереть все данные?")
                .setMessage("Приложение удалит кэш, базы сообщений, сессии и закроется.")
                .setPositiveButton("Стереть", (d, w) -> OakPanic.executePanicWipe(getParentActivity()))
                .setNegativeButton(LocaleController.getString(R.string.Cancel), null)
                .show();
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
