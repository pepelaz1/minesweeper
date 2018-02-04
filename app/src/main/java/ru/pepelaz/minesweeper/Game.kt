package ru.pepelaz.minesweeper

import java.util.*


/**
 * Created by pepel on 31.01.2018.
 */
class Game {

    var countX: Int private set
    var countY: Int private set
    private val blocks: ArrayList<ArrayList<Block>> = ArrayList()

    private var bombsCount: Int = 0

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
        generateBombs()
    }

    init {

    }

    private fun generateBombs() {
        bombsCount = (countX * countY) / 100 * 20
        val random = Random()
        for(n in 0..bombsCount - 1) {
            while(true){
                val i = random.nextInt(countX)
                val j = random.nextInt(countY)
                if (blocks[i][j].state != BlockState.Bomb) {
                    blocks[i][j].state = BlockState.Bomb
                    break
                }
            }
        }
    }

    fun blockState(i: Int, j: Int) : BlockState {
        return  blocks[i][j].state
    }

    fun onShortClick(i: Int, j :Int) {
        calcBombsAround(i, j)
    }

    fun onLongClick(i: Int, j :Int) {
        val state =  blocks[i][j].state
        if (state == BlockState.Unclicked) {
            blocks[i][j].state = BlockState.Flag
        } else {
            blocks[i][j].state = BlockState.Unclicked
        }
    }

    fun calcBombsAround(i: Int, j :Int) {
        var cnt = 0
        if ((i - 1 >= 0) && (j - 1 >= 0)) {
            if (blocks[i - 1][j - 1].state == BlockState.Bomb)
                cnt++
        }

        if (i - 1 >= 0 ) {
            if (blocks[i - 1][j].state == BlockState.Bomb)
                cnt++
        }

        if (i - 1 >= 0 && (j + 1 <= countY - 1)) {
            if (blocks[i - 1][j + 1].state == BlockState.Bomb)
                cnt++
        }

        if (j - 1 >= 0) {
            if (blocks[i][j - 1].state == BlockState.Bomb)
                cnt++
        }

        if (j + 1 <= countY - 1) {
            if (blocks[i][j + 1].state == BlockState.Bomb)
                cnt++
        }

        if ((i + 1 <= countX - 1) && (j - 1 >= 0)) {
            if (blocks[i + 1][j - 1].state == BlockState.Bomb)
                cnt++
        }

        if (i + 1 <= countX - 1 ) {
            if (blocks[i + 1][j].state == BlockState.Bomb)
                cnt++
        }

        if ((i + 1 <= countX - 1) && (j + 1 <= countY - 1)) {
            if (blocks[i + 1][j + 1].state == BlockState.Bomb)
                cnt++
        }

        blocks[i][j].state = BlockState.values()[cnt]

        if (cnt == 0)
            exploreFreeSpace(i, j)
    }

    fun exploreFreeSpace(i: Int, j: Int) {

    }
}