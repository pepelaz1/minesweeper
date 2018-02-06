package ru.pepelaz.minesweeper

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.lang.System.currentTimeMillis
import java.util.ArrayList
import android.R.attr.name
import android.support.v4.content.ContextCompat
import ru.pepelaz.minesweeper.R.style.AppTheme


/**
 * Created by pepel on 25.01.2018.
 */
class GameView(context: Context) : View(context) {

    val paint: Paint
    val bmpClicked: Bitmap
    val bmpUnclicked: Bitmap
    val bmpFlag: Bitmap
    val bmpBomb: Bitmap
    val bmpSmiley: Bitmap
    val bmpSmileyClicked: Bitmap
    val bmpSmileyWin: Bitmap
    val bmpSmileyLose: Bitmap
    val bmpNumbers = ArrayList<Bitmap>()
    var dx: Float = 0f
    var dy: Float  = 0f
    var blockWidth = 0
    var blockHeight = 0
    var smileyHegiht = 0
    var smileyWidth = 0
    var smileyRect = Rect()
    var lastTime: Long = 0
    var headerHeight: Int = 0

    var smileyState = SmileyState.Unclicked
    var game: Game? = null

    init {

        paint = Paint()
        paint.isFilterBitmap = true
        paint.isAntiAlias = true
        paint.color = Color.YELLOW


        bmpUnclicked = BitmapFactory.decodeResource(resources, R.drawable.block_unclicked)
        bmpClicked = BitmapFactory.decodeResource(resources, R.drawable.block_clicked)
        bmpFlag = BitmapFactory.decodeResource(resources, R.drawable.flag)
        bmpBomb = BitmapFactory.decodeResource(resources, R.drawable.bomb)
        bmpSmiley = BitmapFactory.decodeResource(resources, R.drawable.smiley)
        bmpSmileyClicked = BitmapFactory.decodeResource(resources, R.drawable.smiley_o)
        bmpSmileyWin = BitmapFactory.decodeResource(resources, R.drawable.smiley_win)
        bmpSmileyLose = BitmapFactory.decodeResource(resources, R.drawable.smiley_lose)

        for (n in 1..8) {
            val resId = resources.getIdentifier("n" + n.toString(), "drawable", context.packageName)
            bmpNumbers.add(BitmapFactory.decodeResource(resources, resId))
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.drawColor(ContextCompat.getColor(context, R.color.background))

        val canvasWidth = canvas?.width?:0;
        val canvasHeight = canvas?.height?:0;

        val anchorSide = if (canvasWidth < canvasHeight) canvasWidth else canvasHeight
        headerHeight = ((anchorSide / 100f) * 15).toInt()
        blockWidth = anchorSide / 8
        blockHeight = blockWidth

        smileyHegiht = headerHeight - 4
        smileyWidth = smileyHegiht
        smileyRect = Rect(canvasWidth/2 - smileyWidth/2, 2, canvasWidth/2 + smileyWidth/2, smileyHegiht)


        if (game == null) {

            val cntX = canvasWidth / blockWidth
            val cntY = (canvasHeight - headerHeight) / blockHeight
            game = Game(cntX, cntY)

            val actualWidth = cntX * blockWidth
            val actualHeight = cntY * blockHeight

            dx = canvasWidth.toFloat() / actualWidth
            dy = (canvasHeight - headerHeight).toFloat() / actualHeight
        }

        drawHeader(canvas)
        drawBlocks(canvas)
    }

    fun drawHeader(canvas: Canvas?) {

        val srcRect = Rect(0, 0, bmpSmiley.width, bmpSmiley.height)
        val dstRect = Rect(smileyRect)
        when (smileyState) {
            SmileyState.Clicked -> {
                canvas?.drawBitmap(bmpSmileyClicked, srcRect, dstRect, paint)
            }
            SmileyState.Win -> {
                canvas?.drawBitmap(bmpSmileyWin, srcRect, dstRect, paint)
            }
            SmileyState.Lose -> {
                canvas?.drawBitmap(bmpSmileyLose, srcRect, dstRect, paint)
            }
            else -> {
                canvas?.drawBitmap(bmpSmiley, srcRect, dstRect, paint)
            }
        }
    }

    fun drawBlocks(canvas: Canvas?) {
        val game = game?:return

        val srcRect = Rect(0, 0, bmpUnclicked.width, bmpUnclicked.height)
        for (i in 0..game.countX - 1) {
            for(j in 0..game.countY - 1) {

                val x1 = (i * blockWidth * dx).toInt()
                val y1 = headerHeight +  (j * blockHeight * dy).toInt()
                val x2 = ((i+1) * blockWidth * dx).toInt()
                val y2 = headerHeight + ((j+1) *  blockHeight * dy).toInt()

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
        val gameSafe = game ?: return true

        val smileyClicked = smileyRect.contains((event?.x ?: 0f).toInt(), (event?.y ?: 0f).toInt())

        val i: Int = ((event?.x ?: 0f) / (blockWidth * dx)).toInt()
        val j: Int = (((event?.y ?: 0f) - headerHeight) / (blockHeight * dy)).toInt()

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTime = currentTimeMillis()

                if (smileyClicked) {
                    smileyState = SmileyState.Clicked
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {

                if (smileyClicked) {
                    smileyState = SmileyState.Unclicked
                    game = null
                    invalidate();
                } else {
                    if (gameSafe.state == GameState.Continue) {
                        if (currentTimeMillis() - lastTime < 300) {
                            gameSafe.onShortClick(i, j)
                            when (gameSafe.state) {
                                GameState.Win -> {

                                }
                                GameState.Lose -> {
                                    smileyState = SmileyState.Lose
                                }
                                else -> {

                                }
                            }
                        } else {
                            gameSafe.onLongClick(i, j)

                        }
                        invalidate()
                    }
                }
            }
        }
        return true
    }
}