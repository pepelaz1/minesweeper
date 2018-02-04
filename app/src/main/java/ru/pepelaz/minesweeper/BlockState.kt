package ru.pepelaz.minesweeper

/**
 * Created by pepel on 04.02.2018.
 */
enum class BlockState(val state: Int) {

    Clicked(0),
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5),
    Six(6),
    Seven(7),
    Eight(8),
    Unclicked(10),
    Flag(11),
    Bomb(12),
    BombClicked(13)
}