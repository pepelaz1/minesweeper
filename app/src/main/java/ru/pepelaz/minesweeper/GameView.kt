package ru.pepelaz.minesweeper

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.lang.System.currentTimeMillis
import java.util.ArrayList
import android.R.attr.name




/**
 * Created by pepel on 25.01.2018.
 */
class GameView(context: Context) : View(context) {

    val paint: Paint
    //var circleX: Float?
    //var circleY: Float?
    val bmpClicked: Bitmap
    val bmpUnclicked: Bitmap
    val bmpFlag: Bitmap
    val bmpBomb: Bitmap
    val bmpNumbers = ArrayList<Bitmap>()
    var dx: Float = 0f
    var dy: Float  = 0f
    val blockWidth = 120
    var blockHeight = 120
    var lastTime: Long = 0

    var game: Game? = null

    init {

        paint = Paint()
        paint.isFilterBitmap = true
        paint.isAntiAlias = true
        paint.color = Color.YELLOW


        //circleX = 100f
       //circleY = 100f

        bmpUnclicked = BitmapFactory.decodeResource(resources, R.drawable.block_unclicked)
        bmpClicked = BitmapFactory.decodeResource(resources, R.drawable.block_clicked)
        bmpFlag = BitmapFactory.decodeResource(resources, R.drawable.flag)
        bmpBomb = BitmapFactory.decodeResource(resources, R.drawable.bomb)

        for (n in 1..8) {
            val resId = resources.getIdentifier("n" + n.toString(), "drawable", context.packageName)
            bmpNumbers.add(BitmapFactory.decodeResource(resources, resId))
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        //canvas?.drawColor(Color.GRAY)
        //canvas?.drawCircle(circleX ?: 0f, circleY ?: 0f, 50f, paint)

        val canvasWidth = canvas?.width?:0;
        val canvasHeight = canvas?.height?:0;

        if (game == null) {



            val cntX = canvasWidth / blockWidth
            val cntY = canvasHeight / blockHeight
            game = Game(cntX, cntY)

            val actualWidth = cntX * blockWidth
            val actualHeight = cntY * blockHeight

            dx = canvasWidth.toFloat() / actualWidth
            dy = canvasHeight.toFloat() / actualHeight
        }

        val game = game?:return

        val srcRect = Rect(0, 0, bmpUnclicked.width, bmpUnclicked.height)
        for (i in 0..game.countX - 1) {
            for(j in 0..game.countY - 1) {

                val x1 = (i * blockWidth * dx).toInt()
                val y1 = (j * blockHeight * dy).toInt()
                val x2 = ((i+1) * blockWidth * dx).toInt()
                val y2 = ((j+1) *  blockHeight * dy).toInt()

                val dstRect = Rect(x1, y1, x2, y2)
                when(game.blockState(i,j)) {
                    BlockState.Flag -> {
                        canvas?.drawBitmap(bmpFlag, srcRect, dstRect, paint)
                    }
                    BlockState.Bomb -> {
                        canvas?.drawBitmap(bmpBomb, srcRect, dstRect, paint)
                    }
                    BlockState.Unclicked -> {
                        canvas?.drawBitmap(bmpUnclicked, srcRect, dstRect, paint)
                    }
                    BlockState.Clicked -> {
                        canvas?.drawBitmap(bmpClicked, srcRect, dstRect, paint)
                    }
                    else -> {

                        canvas?.drawBitmap(bmpNumbers[game.blockState(i,j).state - 1], srcRect, dstRect, paint)
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //circleX = event?.x
        //circleY = event?.y
        val game = game?:return true

        val i: Int = ( (event?.x?:0f) / (blockWidth * dx)).toInt()
        val j: Int = ( (event?.y?:0f) / (blockHeight * dy)).toInt()

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTime = currentTimeMillis()
            }
            MotionEvent.ACTION_UP -> {

                if (currentTimeMillis() - lastTime < 1000) {
                    game.onShortClick(i, j)
                    invalidate()
                } else {
                    game.onLongClick(i, j)
                    invalidate()
                }
            }
        }
        return true
    }
}