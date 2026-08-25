package org.telegram.ui;

import android.view.View;
import org.telegram.margelet.MargeletConfig;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import java.util.ArrayList;

public class OakEverydayActivity extends UniversalFragment {

    private static final int ID_MAX_QUALITY_PHOTOS = 1, ID_SAVE_SELF_DESTRUCT = 2,
            ID_BOOST_DOWNLOADS = 3, ID_LOOP_VIDEOS = 4, ID_INLINE_TRANSLATE = 5,
            ID_DELETE_EVERYONE = 6, ID_EXACT_ONLINE = 7, ID_HIDE_PHONE = 8,
            ID_BOOST_VOICE = 9, ID_NO_PROXIMITY = 10;

    @Override
    protected CharSequence getTitle() {
        return "Удобства и медиа";
    }

    @Override
    public View createView(android.content.Context context) {
        View v = super.createView(context);
        listView.setSections();
        return v;
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asHeader("Качество и загрузки"));
        items.add(UItem.asCheck(ID_MAX_QUALITY_PHOTOS, "Фото в максимальном качестве").setChecked(MargeletConfig.maxQualityPhotos()));
        items.add(UItem.asShadow("Отправка фото до 2560px с качеством 98% без сжатия в мыло."));
        items.add(UItem.asCheck(ID_SAVE_SELF_DESTRUCT, "Сохранять таймерные медиа").setChecked(MargeletConfig.saveSelfDestructing()));
        items.add(UItem.asShadow("Возможность сохранять самоуничтожающиеся фото и видео в галерею."));
        items.add(UItem.asCheck(ID_BOOST_DOWNLOADS, "Ускоритель скачивания файлов").setChecked(MargeletConfig.boostDownloads()));
        items.add(UItem.asShadow("Скачивание файлов параллельными потоками для максимальной скорости."));
        items.add(UItem.asCheck(ID_LOOP_VIDEOS, "Зацикливать воспроизведение видео").setChecked(MargeletConfig.autoLoopVideos()));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("Чат и переводчик"));
        items.add(UItem.asCheck(ID_INLINE_TRANSLATE, "Быстрый перевод сообщений").setChecked(MargeletConfig.inlineTranslation()));
        items.add(UItem.asShadow("Прямой перевод сообщений в чате без сторонних ботов."));
        items.add(UItem.asCheck(ID_DELETE_EVERYONE, "«Удалить для всех» по умолчанию").setChecked(MargeletConfig.deleteForEveryoneDefault()));
        items.add(UItem.asShadow("Галочка «Удалить также для собеседника» всегда включена заранее."));
        items.add(UItem.asCheck(ID_EXACT_ONLINE, "Точное время онлайна").setChecked(MargeletConfig.showExactOnlineTime()));
        items.add(UItem.asShadow("Показывает точное время последнего визита вместо примерного."));
        items.add(UItem.asCheck(ID_HIDE_PHONE, "Скрыть номер телефона в меню").setChecked(MargeletConfig.hidePhoneNumber()));
        items.add(UItem.asShadow("Защита от подглядывания в боковом меню и настройках."));

        items.add(UItem.asHeader("Аудио и голосовые"));
        items.add(UItem.asCheck(ID_BOOST_VOICE, "Усиление громкости голосовых (+6 dB)").setChecked(MargeletConfig.boostVoiceVolume()));
        items.add(UItem.asShadow(null));
        items.add(UItem.asCheck(ID_NO_PROXIMITY, "Не гасить экран у уха (отключить датчик)").setChecked(MargeletConfig.disableProximitySensor()));
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == ID_MAX_QUALITY_PHOTOS) MargeletConfig.setMaxQualityPhotos(!MargeletConfig.maxQualityPhotos());
        else if (item.id == ID_SAVE_SELF_DESTRUCT) MargeletConfig.setSaveSelfDestructing(!MargeletConfig.saveSelfDestructing());
        else if (item.id == ID_BOOST_DOWNLOADS) MargeletConfig.setBoostDownloads(!MargeletConfig.boostDownloads());
        else if (item.id == ID_LOOP_VIDEOS) MargeletConfig.setAutoLoopVideos(!MargeletConfig.autoLoopVideos());
        else if (item.id == ID_INLINE_TRANSLATE) MargeletConfig.setInlineTranslation(!MargeletConfig.inlineTranslation());
        else if (item.id == ID_DELETE_EVERYONE) MargeletConfig.setDeleteForEveryoneDefault(!MargeletConfig.deleteForEveryoneDefault());
        else if (item.id == ID_EXACT_ONLINE) MargeletConfig.setShowExactOnlineTime(!MargeletConfig.showExactOnlineTime());
        else if (item.id == ID_HIDE_PHONE) MargeletConfig.setHidePhoneNumber(!MargeletConfig.hidePhoneNumber());
        else if (item.id == ID_BOOST_VOICE) MargeletConfig.setBoostVoiceVolume(!MargeletConfig.boostVoiceVolume());
        else if (item.id == ID_NO_PROXIMITY) MargeletConfig.setDisableProximitySensor(!MargeletConfig.disableProximitySensor());
        listView.adapter.update(true);
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
