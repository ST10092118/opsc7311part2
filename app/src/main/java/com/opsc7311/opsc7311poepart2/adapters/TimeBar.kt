package com.opsc7311.opsc7311poepart2.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.opsc7311.opsc7311poepart2.R

class TimeBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var progress = 0
    private var max = 100
    private var timerText = "00:00:00"

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.primary)
        strokeWidth = 20f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val backgroundPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.grey)
        strokeWidth = 16f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.primary)
        textSize = 100f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val rect = RectF()

    private var countDownTimer: CountDownTimer? = null

    init {
        // Optional: Initialize any additional properties or listeners here
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = paint.strokeWidth / 2
        rect.set(padding, padding, w - padding, h - padding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawOval(rect, backgroundPaint)
        val angle = 360 * progress / max
        canvas.drawArc(rect, -90f, angle.toFloat(), false, paint)
        canvas.drawText(timerText, rect.centerX(), rect.centerY() + textPaint.textSize / 4, textPaint)
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    fun setMax(max: Int) {
        this.max = max
    }

    fun setTimerText(text: String) {
        this.timerText = text
        invalidate()
    }

    fun getTimerText(): String {
        return this.timerText
    }

    fun setBackgroundPaint(colorResId: Int) {
        backgroundPaint.color = ContextCompat.getColor(context, colorResId)
        invalidate()
    }

    fun startTimer(durationInMillis: Long) {
        countDownTimer?.cancel() // Cancel any existing timer
        countDownTimer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                val minutes = secondsLeft / 60
                val seconds = secondsLeft % 60
                timerText = String.format("%02d:%02d", minutes, seconds)
                progress = ((durationInMillis - millisUntilFinished) * max / durationInMillis).toInt()
                invalidate()
            }

            override fun onFinish() {
                timerText = "00:00"
                progress = max
                invalidate()
            }
        }.start()
    }

    fun stopTimer() {
        countDownTimer?.cancel()
        timerText = getTimerText()
        progress = 0
        invalidate()
    }
}