package com.example.clockview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class ClockView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attr, defStyle) {

    private var myHeight: Int = 0
    private var myWidth: Int = 0
    private var padding: Int = 0
    private var fontSize: Int = 0
    private var handTruncation: Int = 0
    private var hourHandTruncation: Int = 0
    private var numeralSpacing: Int = 0
    private var radius: Int = 0
    private var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()
    private val paint = Paint()

    private fun initClock() {
        myHeight = height
        myWidth = width
        padding = numeralSpacing + 50
        fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 13F,
            resources.displayMetrics
        ).toInt()
        val min = myHeight.coerceAtMost(myWidth)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }
        canvas.drawColor(Color.WHITE)
        drawCircle(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }


    private fun drawHour(canvas: Canvas, loc: Float) {
        paint.reset()
        paint.color = Color.BLUE
        paint.strokeWidth = 26F
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius = radius - handTruncation * 2
        canvas.drawLine(
            (myWidth / 2 - cos(angle) * handRadius / 4).toFloat(),
            (myHeight / 2 - sin(angle) * handRadius / 4).toFloat(),
            (myWidth / 2 + cos(angle) * handRadius / 2).toFloat(),
            (myHeight / 2 + sin(angle) * handRadius / 2).toFloat(),
            paint
        )
    }

    private fun drawMinute(canvas: Canvas, loc: Float) {
        paint.reset()
        paint.color = Color.RED
        paint.strokeWidth = 16F
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius = radius - handTruncation
        paint.let {
            canvas.drawLine(
                (myWidth / 2 - cos(angle) * handRadius / 3).toFloat(),
                (myHeight / 2 - sin(angle) * handRadius / 3).toFloat(),
                (myWidth / 2 + cos(angle) * handRadius / 1.5).toFloat(),
                (myHeight / 2 + sin(angle) * handRadius / 1.5).toFloat(),
                it
            )
        }
    }

    private fun drawSecond(canvas: Canvas, loc: Float) {
        paint.reset()
        paint.color = Color.BLACK
        paint.strokeWidth = 12F
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius = radius - handTruncation
        canvas.drawLine(
            (myWidth / 2 - cos(angle) * handRadius / 4).toFloat(),
            (myHeight / 2 - sin(angle) * handRadius / 4).toFloat(),
            (myWidth / 2 + cos(angle) * handRadius).toFloat(),
            (myHeight / 2 + sin(angle) * handRadius).toFloat(),
            paint
        )
    }

    private fun drawHands(canvas: Canvas) {
        val c: Calendar = Calendar.getInstance()
        var hour: Int = c.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour
        drawHour(canvas, (hour + c.get(Calendar.MINUTE) / 60) * 5f)
        drawMinute(canvas, c.get(Calendar.MINUTE).toFloat())
        drawSecond(canvas, c.get(Calendar.SECOND).toFloat())
    }

    private fun drawNumeral(canvas: Canvas) {
        paint.textSize = fontSize.toFloat()
        for (number in numbers) {
            val tmp = number.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (myWidth / 2 + cos(angle) * radius - rect.width() / 2).toFloat()
            val y = (myHeight / 2 + sin(angle) * radius + rect.height() / 2).toFloat()
            paint.let { canvas.drawText(tmp, x, y, it) }
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.let { 
            canvas.drawCircle((myWidth / 2).toFloat(),
            (myHeight / 2).toFloat(), 12F, it)
        }
    }

    private fun drawCircle(canvas: Canvas) {
        paint.reset()
        paint.color = Color.BLACK
        paint.strokeWidth = 5F
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.let {
            canvas.drawCircle(
                (myWidth / 2).toFloat(),
                (myHeight / 2).toFloat(), (radius + padding - 10).toFloat(), it
            )
        }
    }
}