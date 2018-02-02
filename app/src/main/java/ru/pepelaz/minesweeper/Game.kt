package ru.pepelaz.minesweeper

import android.widget.Toast

/**
 * Created by pepel on 31.01.2018.
 */
class Game {

    var countX: Int private set
    var countY: Int private set
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

    fun blockState(i: Int, j: Int) : Int {
        return  blocks[i][j].state
    }

    fun onShortClick(i: Int, j :Int) {
        val state =  blocks[i][j].state
        if (state == 0) {
            blocks[i][j].state = 2
        } else {
            blocks[i][j].state = 0
        }
    }

    fun onLongClick(i: Int, j :Int) {

    }
}