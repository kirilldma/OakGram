package org.telegram.ui;

import android.view.View;
import org.telegram.margelet.MargeletConfig;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import java.util.ArrayList;

public class OakCustomizationActivity extends UniversalFragment {

    private static final int ID_HIDE_STORIES = 1, ID_HIDE_SEND_AS = 2, ID_HIDE_BOT_BUTTON = 3,
            ID_NO_DOUBLE_TAP = 4, ID_SHOW_ID_DC = 5, ID_NO_HAPTICS = 6,
            ID_PAUSE_MUSIC = 7, ID_CONFIRM_VOICE = 8, ID_REAR_CAMERA = 9,
            ID_UNLIMITED_SPEED = 10, ID_HIDE_ALL_CHATS = 11, ID_TAB_COUNTERS = 12,
            ID_HIDE_TAB_LABELS = 13, ID_SEND_BY_ENTER = 14, ID_CODE_FORMATTING = 15,
            ID_UNLIMITED_PINS = 16;

    private static final int[] BUBBLE_RADII = {0, 6, 12, 16, 22, 28};
    private static final String[] BUBBLE_LABELS = {"0dp (Острые)", "6dp", "12dp", "16dp", "22dp", "28dp (Круглые)"};

    private static final String[] AVATAR_SHAPES = {"Круглые", "Сквиркл", "Квадратные"};

    private static int indexOf(int[] arr, int v, int fb) {
        for (int i = 0; i < arr.length; i++) if (arr[i] == v) return i;
        return fb;
    }

    @Override
    protected CharSequence getTitle() {
        return "Кастомизация";
    }

    @Override
    public View createView(android.content.Context context) {
        View v = super.createView(context);
        listView.setSections();
        return v;
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asHeader("Сообщения и пузыри"));
        items.add(UItem.asSlideView(BUBBLE_LABELS, indexOf(BUBBLE_RADII, MargeletConfig.bubbleCornersRadius(), 3),
                i -> MargeletConfig.setBubbleCornersRadius(BUBBLE_RADII[i])));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("Форма аватаров"));
        items.add(UItem.asSlideView(AVATAR_SHAPES, MargeletConfig.avatarCorners(),
                i -> MargeletConfig.setAvatarCorners(i)));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("Чат, текст и кнопки"));
        items.add(UItem.asCheck(ID_UNLIMITED_PINS, "Неограниченно закреплённых чатов").setChecked(MargeletConfig.unlimitedPins()));
        items.add(UItem.asShadow("Снимает лимит в 5 закреплённых чатов в папках и списке."));
        items.add(UItem.asCheck(ID_CODE_FORMATTING, "Быстрое форматирование (Monospace / Code)").setChecked(MargeletConfig.codeFormattingEnabled()));
        items.add(UItem.asShadow("Кнопка моноширинного кода и блоков кода в поле ввода."));
        items.add(UItem.asCheck(ID_HIDE_STORIES, "Скрыть блок историй").setChecked(MargeletConfig.hideStories()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_SHOW_ID_DC, "Показывать ID и DC в профиле").setChecked(MargeletConfig.showUserIdDc()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_HIDE_SEND_AS, "Скрыть выбор «Отправить от имени»").setChecked(MargeletConfig.hideSendAs()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_HIDE_BOT_BUTTON, "Скрыть кнопку запуска Web-бота").setChecked(MargeletConfig.hideBotButtons()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_NO_DOUBLE_TAP, "Отключить реакцию по двойному тапу").setChecked(MargeletConfig.disableDoubleTapReaction()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_SEND_BY_ENTER, "Отправка по Enter").setChecked(MargeletConfig.sendByEnter()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_NO_HAPTICS, "Отключить виброотклик (Haptics)").setChecked(MargeletConfig.disableHaptics()));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("Голосовые и кружочки"));
        items.add(UItem.asCheck(ID_PAUSE_MUSIC, "Ставить музыку на паузу при записи").setChecked(MargeletConfig.pauseMusicOnVoice()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_CONFIRM_VOICE, "Подтверждать отправку голосовых").setChecked(MargeletConfig.confirmVoiceSend()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_REAR_CAMERA, "Кружочки сразу с задней камеры").setChecked(MargeletConfig.rearCameraVideoNotes()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_UNLIMITED_SPEED, "Ускорение голосовых до 3.0x").setChecked(MargeletConfig.unlimitedVoiceSpeed()));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("Папки и вкладки"));
        items.add(UItem.asCheck(ID_TAB_COUNTERS, "Счётчики сообщений на вкладках").setChecked(MargeletConfig.showTabCounters()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_HIDE_TAB_LABELS, "Скрыть названия папок").setChecked(MargeletConfig.hideTabLabels()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_HIDE_ALL_CHATS, "Скрыть вкладку «Все чаты»").setChecked(MargeletConfig.hideAllChatsTab()));
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == ID_UNLIMITED_PINS) MargeletConfig.setUnlimitedPins(!MargeletConfig.unlimitedPins());
        else if (item.id == ID_CODE_FORMATTING) MargeletConfig.setCodeFormattingEnabled(!MargeletConfig.codeFormattingEnabled());
        else if (item.id == ID_HIDE_STORIES) MargeletConfig.setHideStories(!MargeletConfig.hideStories());
        else if (item.id == ID_SHOW_ID_DC) MargeletConfig.setShowUserIdDc(!MargeletConfig.showUserIdDc());
        else if (item.id == ID_HIDE_SEND_AS) MargeletConfig.setHideSendAs(!MargeletConfig.hideSendAs());
        else if (item.id == ID_HIDE_BOT_BUTTON) MargeletConfig.setHideBotButtons(!MargeletConfig.hideBotButtons());
        else if (item.id == ID_NO_DOUBLE_TAP) MargeletConfig.setDisableDoubleTapReaction(!MargeletConfig.disableDoubleTapReaction());
        else if (item.id == ID_SEND_BY_ENTER) MargeletConfig.setSendByEnter(!MargeletConfig.sendByEnter());
        else if (item.id == ID_NO_HAPTICS) MargeletConfig.setDisableHaptics(!MargeletConfig.disableHaptics());
        else if (item.id == ID_PAUSE_MUSIC) MargeletConfig.setPauseMusicOnVoice(!MargeletConfig.pauseMusicOnVoice());
        else if (item.id == ID_CONFIRM_VOICE) MargeletConfig.setConfirmVoiceSend(!MargeletConfig.confirmVoiceSend());
        else if (item.id == ID_REAR_CAMERA) MargeletConfig.setRearCameraVideoNotes(!MargeletConfig.rearCameraVideoNotes());
        else if (item.id == ID_UNLIMITED_SPEED) MargeletConfig.setUnlimitedVoiceSpeed(!MargeletConfig.unlimitedVoiceSpeed());
        else if (item.id == ID_TAB_COUNTERS) MargeletConfig.setShowTabCounters(!MargeletConfig.showTabCounters());
        else if (item.id == ID_HIDE_TAB_LABELS) MargeletConfig.setHideTabLabels(!MargeletConfig.hideTabLabels());
        else if (item.id == ID_HIDE_ALL_CHATS) MargeletConfig.setHideAllChatsTab(!MargeletConfig.hideAllChatsTab());
        listView.adapter.update(true);
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
