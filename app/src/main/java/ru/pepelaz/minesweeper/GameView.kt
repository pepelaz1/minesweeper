package ru.pepelaz.minesweeper

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View


/**
 * Created by pepel on 25.01.2018.
 */
class GameView(context: Context) : View(context) {

    val paint: Paint
    //var circleX: Float?
    //var circleY: Float?
    val bmp: Bitmap
    var dx: Float = 0f
    var dy: Float  = 0f
    val blockWidth = 120
    var blockHeight = 120

    var game: Game? = null

    init {

        paint = Paint()
        paint.isFilterBitmap = true
        paint.isAntiAlias = true
        paint.color = Color.YELLOW


        //circleX = 100f
       //circleY = 100f

        bmp = BitmapFactory.decodeResource(resources, R.drawable.block_unclicked)
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

        val srcRect = Rect(0, 0, bmp.width, bmp.height)
        for (i in 0..game.countX - 1) {
            for(j in 0..game.countY - 1) {

                val x1 = (i * blockWidth * dx).toInt()
                val y1 = (j * blockHeight * dy).toInt()
                val x2 = ((i+1) * blockWidth * dx).toInt()
                val y2 = ((j+1) *  blockHeight * dy).toInt()

                val dstRect = Rect(x1, y1, x2, y2)
                canvas?.drawBitmap(bmp, srcRect, dstRect, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //circleX = event?.x
        //circleY = event?.y
        invalidate()
        return true
    }
}