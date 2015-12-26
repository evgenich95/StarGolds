﻿# StarGolds

StarGolds это увлекательная игра для Android 4.0.3 и выше.

![Главный экран игры - карта](./images/just.png)

Вы — алчный Дарт Вейдер, который хочет собрать всё золото из запасов повстанцев. Но на пути у вас стоит целая армия R2-D2, которых следует избегать. Ваша задача — пробраться по коридорам хранилища и собрать всё золото, не будучи пойманным роботами. Для того, чтобы собрать золото, вам необходимо встать на одну с ним клетку. Будьте осторожны: роботы пытаются вас догнать по кратчайшему пути, так что придётся пользоваться *бластером*.

> При применении бластера роботы, находящиеся на соседних с игроком клетках
по горизонтали, вертикали и диагонали, парализуются на 5 ходов. Но не тратьте заряды зря — их у вас всего 3!
> ![Один замороженный робот](./images/freeze.png)

Также в комнате присутствуют ямы, на которые никто не может вставать.

## Генерация комнаты
Игрок перемещается по карте, характеристики которой задаёт перед началом игры. Комната генерируется таким образом, чтобы избежать возможных коллизий:

 * у игрока всегда есть путь до золота, как и в начале игры
 * в начале игры в радиусе 2-х клеток от игрока роботов нет;

Путь, по которому роботы догоняют игрока — всегда кратчайший; он строится по волновому алгоритму Ли.
После того, как игрок соберёт всё золото или встанет на одно клетку с роботом, игра заканчивается.

![Проигрыш](./images/gameover.png)
![Победа](./images/win.png)


## Техническая часть

