package ru.pepelaz.minesweeper

/**
 * Created by pepel on 31.01.2018.
 */
class Game {

    var countX: Int private set
    var countY: Int
    private val blocks: ArrayList<ArrayList<Block>> = ArrayList()

    constructor(countX: Int, countY: Int) {
        this.countX = countX
        this.countY = countY
        for (i in 0..countX - 1) {
            val row: ArrayList<Block> = ArrayList()
            for(j in 0..countY - 1) {
                val block = Block()
                row.add(block)
            }
            blocks.add(row)
        }
    }

    init {

    }


}