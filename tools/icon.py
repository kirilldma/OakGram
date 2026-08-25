#!/usr/bin/env python3
"""Иконка форка: пастельно-зелёный фон, бумажный самолётик носом на зрителя.

Рисую свой знак, а не перекрашиваю чужой: у телеграма самолёт летит вбок и
это их товарный знак. Здесь самолёт развёрнут к смотрящему — видно нос и две
плоскости крыльев, — и цвет другой.

Что изменилось после разбора владельца:

  * заливка ровная, без градиента: «чисто пастельно зелёный». Оттенок
    салатовый, но уведён в синеву, иначе на белой шторке уведомлений
    зелёный читается как кислотный;
  * форма. Круг был выбран мной без причины, и это была ошибка. С восьмого
    андроида иконку режет лаунчер: он берёт квадрат 108x108 и накладывает
    свою маску — круг, скруглённый квадрат, каплю. Если нарисовать круг
    самому, на телефоне со скруглённым квадратом получится круг внутри
    квадрата, с полями по углам. Поэтому основа теперь квадратная во всё
    поле, а форму выбирает система. Отдельные png с кругом и со скруглением
    остаются — на посмотреть и для старых лаунчеров;
  * самолётик поднят. У него вся масса внизу, в развале крыльев, поэтому
    геометрический центр и видимый центр не совпадают: посаженный ровно, он
    выглядит съехавшим вниз. Сдвиг на три процента высоты вверх.

  python3 icon.py
"""
import sys

from PIL import Image, ImageDraw

S = 512
PINK = (255, 64, 129)        # OakGram Rose / Pink primary accent
GREEN = (141, 209, 176)

# Набор для списка «иконка приложения». Форма и самолётик у всех одни, меняется
# только цвет поля: пять разных рисунков там были бы пятью разными знаками, а
# знак у форка один.
PALETTE = {
    "default": (PINK, "Розовая (OakGram)"),
    "night":   ((38, 20, 28), "Ночная"),
    "green":   (GREEN, "Зелёная"),
    "lavender": ((183, 168, 224), "Лавандовая"),
    "sand":    ((231, 200, 140), "Песочная"),
    "sea":     ((127, 196, 214), "Морская"),
}
LIFT = 0.03                  # оптическая поправка: доля высоты вверх


def plane(d, cx, cy, k=1.0):
    """Самолётик носом на зрителя. cy — видимый центр, не геометрический."""
    def p(x, y):
        return (cx + x * k, cy + y * k)

    nose, span, back, keel = -150, 190, 120, 60
    left = [p(0, nose), p(-span, back), p(-14, keel)]
    right = [p(0, nose), p(span, back), p(14, keel)]
    d.polygon(left, fill=(255, 255, 255, 255))
    d.polygon(right, fill=(238, 243, 250, 255))
    # корпус: узкий клин от носа вниз, темнее — это ребро сгиба
    d.polygon([p(0, nose), p(-14, keel), p(0, 96), p(14, keel)],
              fill=(204, 213, 233, 255))


def draw(size=S, shape="square", radius=0.22, k=0.74, color=None):
    """shape: square | round | rounded. k — размер самолёта относительно поля:
    под круглой маской углы срезаются, и самолёт в полную ширину терял концы
    крыльев. Три четверти влезают под любую маску."""
    im = Image.new("RGBA", (S, S), (0, 0, 0, 0))
    mask = Image.new("L", (S, S), 0)
    m = ImageDraw.Draw(mask)
    if shape == "round":
        m.ellipse([0, 0, S - 1, S - 1], fill=255)
    elif shape == "rounded":
        m.rounded_rectangle([0, 0, S - 1, S - 1], int(S * radius), fill=255)
    else:
        m.rectangle([0, 0, S - 1, S - 1], fill=255)
    im.paste(Image.new("RGB", (S, S), color or GREEN), (0, 0), mask)
    d = ImageDraw.Draw(im)
    plane(d, S / 2, S / 2 - S * LIFT, k=k)
    return im.resize((size, size), Image.LANCZOS) if size != S else im


def foreground(size=S):
    """Верхний слой адаптивной иконки: только самолёт на прозрачном, с запасом
    по краям — лаунчер обрезает до 72 из 108, а ещё двигает слой при качании."""
    im = Image.new("RGBA", (S, S), (0, 0, 0, 0))
    plane(ImageDraw.Draw(im), S / 2, S / 2 - S * LIFT, k=0.66)
    return im.resize((size, size), Image.LANCZOS) if size != S else im


if __name__ == "__main__":
    draw(shape="square").save("margelet_icon_square.png")
    draw(shape="rounded").save("margelet_icon_rounded.png")
    draw(shape="round").save("margelet_icon_round.png")
    draw(shape="rounded").save("margelet_icon.png")
    # слои адаптивной иконки и мипмапы под старые лаунчеры
    for name, px in (("mdpi", 48), ("hdpi", 72), ("xhdpi", 96),
                     ("xxhdpi", 144), ("xxxhdpi", 192)):
        draw(px, shape="rounded").save(f"icon_{name}.png")
        draw(px, shape="round").save(f"icon_round_{name}.png")
        foreground(int(px * 108 / 48)).save(f"icon_fg_{name}.png")
    print("нарисовано:", GREEN, "подъём", LIFT)
