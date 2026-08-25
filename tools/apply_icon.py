#!/usr/bin/env python3
"""Кладу свою иконку в ресурсы форка.

С восьмого андроида иконка состоит из двух слоёв, а форму выбирает лаунчер:
он берёт квадрат 108x108 и накладывает маску. Поэтому фон здесь — сплошной
цвет во всё поле, а самолётик лежит отдельным слоем с запасом по краям.
Третий слой, monochrome, нужен для тематических иконок тринадцатого андроида:
там от рисунка остаётся только силуэт, залитый цветом системы.

Старые png на месте: лаунчеры до восьмого андроида адаптивных иконок не знают.
"""
import os
import shutil

from PIL import Image

import icon

RES = "tg-src/TMessagesProj/src/main/res"
# У сборки standalone своя иконка: манифест зовёт её с суффиксом _sa, и лежит
# она в отдельном модуле. Первый раз я про это забыл и получил apk с чужим
# самолётиком — оба набора надо подменять вместе.
RES_SA = "tg-src/TMessagesProj_AppStandalone/src/main/res"
DPI = {"mdpi": 48, "hdpi": 72, "xhdpi": 96, "xxhdpi": 144, "xxxhdpi": 192}

BG = """<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#%02X%02X%02X" />
</shape>
""" % icon.PINK

# Силуэт для тематической иконки. Координаты те же, что в icon.py, только
# пересчитанные в поле 108 на 108: центр сдвинут вверх на те же три процента.
PLANE = """<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
  <path
      android:fillColor="#FFFFFF"
      android:pathData="M54,29.88 L27.55,67.46 L52.05,59.11 L54,64.12 L55.95,59.11 L80.45,67.46 Z" />
</vector>
"""

ADAPTIVE = """<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/margelet_background" />
    <foreground android:drawable="@mipmap/margelet_foreground" />
    <monochrome android:drawable="@drawable/margelet_plane" />
</adaptive-icon>
"""


# Какому набору иконок телеграма соответствует наш цвет. Имена чужие, потому
# что на них завязаны манифест и список выбора; меняем только содержимое.
SLOTS = [
    ("icon_background_sa", "ic_launcher_sa", "default"),
    ("icon_2_background_sa", "icon_2_launcher_sa", "night"),
    ("icon_3_background_sa", "icon_3_launcher_sa", "lavender"),
    ("icon_4_background_sa", "icon_4_launcher_sa", "sand"),
    ("icon_5_background_sa", "icon_5_launcher_sa", "sea"),
    ("icon_6_background_sa", "icon_6_launcher_sa", "rose"),
]


def variants():
    """Шесть цветов для списка «иконка приложения» в настройках.

    Фон каждого набора переписываем своим сплошным цветом, самолётик у всех
    один и тот же. Часть этих фонов лежит в drawable, часть — картинками в
    mipmap: телеграм смешал и то и другое, поэтому пишем в оба места.
    """
    for bg, launcher, key in SLOTS:
        color = icon.PALETTE[key][0]
        shape = ("""<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#%02X%02X%02X" />
</shape>
""" % color)
        # Фоны кладём в БИБЛИОТЕКУ, а не в модуль сборки: на них ссылается
        # LauncherIconController, который живёт там же, и ресурсов модуля
        # приложения он не видит — компилятор об этом и сказал.
        open(f"{RES}/drawable/{bg}.xml", "w").write(shape)
        # Одноимённые png в mipmap НЕ трогаем: на них ссылаются слоёные
        # drawable вроде icon_2_background.xml, и удаление ломает сборку
        # ресурсов. Ресурсы разных типов спокойно живут с одним именем.
        adaptive = ADAPTIVE.replace("@drawable/margelet_background", f"@drawable/{bg}")
        open(f"{RES_SA}/mipmap-anydpi-v26/{launcher}.xml", "w").write(adaptive)
        for dpi, px in DPI.items():
            legacy = f"{RES_SA}/mipmap-{dpi}/{launcher}.png"
            if os.path.exists(legacy):
                icon.draw(px, shape="rounded", color=color).save(legacy)


def main():
    open(f"{RES}/drawable/margelet_background.xml", "w").write(BG)
    open(f"{RES}/drawable/margelet_plane.xml", "w").write(PLANE)
    # Копии оригиналов кладу ВНЕ res: сборщик ресурсов проверяет каждое имя в
    # папке и на файле ic_launcher.xml.orig падает — расширение должно быть
    # .xml или .png, и никакое другое.
    os.makedirs("icon_orig", exist_ok=True)
    for name in ("ic_launcher.xml", "ic_launcher_round.xml"):
        p = f"{RES}/mipmap-anydpi-v26/{name}"
        if not os.path.exists(f"icon_orig/{name}"):
            shutil.copy(p, f"icon_orig/{name}")
        open(p, "w").write(ADAPTIVE)
    p = f"{RES_SA}/mipmap-anydpi-v26/ic_launcher_sa.xml"
    if not os.path.exists("icon_orig/ic_launcher_sa.xml"):
        shutil.copy(p, "icon_orig/ic_launcher_sa.xml")
    open(p, "w").write(ADAPTIVE)
    for dpi, px in DPI.items():
        sa = f"{RES_SA}/mipmap-{dpi}/ic_launcher_sa.png"
        if os.path.exists(sa):
            icon.draw(px, shape="rounded").save(sa)
        d = f"{RES}/mipmap-{dpi}"
        # слой самолёта рисуется в поле 108 на 108, а не 48 на 48
        icon.foreground(int(round(px * 108 / 48))).save(f"{d}/margelet_foreground.png")
        icon.draw(px, shape="rounded").save(f"{d}/ic_launcher.png")
        icon.draw(px, shape="round").save(f"{d}/ic_launcher_round.png")
    variants()
    print("иконка на месте:", icon.GREEN, "и ещё", len(SLOTS) - 1, "цвета")


if __name__ == "__main__":
    main()
