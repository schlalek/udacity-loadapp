package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var buttonInProgressColor: Int = 0
    private var buttonIdleColor: Int = 0
    private var inProgressText: String? = null
    private var idleText: String? = null
    private var currentText: String = "Download"

    private var widthSize = 0
    private var heightSize = 0

    private var widthProgressSize = 0f
    private var progressCircle = 0f

    private var valueAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                currentText = "Clicked"
            }


            ButtonState.Loading -> {
                currentText = inProgressText.orEmpty()
                valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat())
                valueAnimator.setDuration(2000)
                valueAnimator.addUpdateListener {
                    widthProgressSize = it.animatedValue as Float
                    invalidate()
                }
                valueAnimator.repeatMode = ValueAnimator.RESTART
                valueAnimator.start()

            }

            ButtonState.Completed -> {
                currentText = idleText.orEmpty()
                valueAnimator.cancel()
                progressCircle = 0f
                widthProgressSize = 0f
            }
        }
        invalidate()

    }

    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        color = buttonInProgressColor
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = resources.getDimension(R.dimen.default_text_size)
        textAlign = Paint.Align.CENTER
        color = Color.WHITE
    }


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            idleText = getString(R.styleable.LoadingButton_idleText)
            inProgressText = getString(R.styleable.LoadingButton_inProgressText)
            buttonIdleColor = getColor(R.styleable.LoadingButton_buttonIdleColor, 0)
            buttonInProgressColor = getColor(R.styleable.LoadingButton_buttonInProgressColor, 0)
        }

    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true



        return true
    }


    override fun onDraw(canvas: Canvas?) {
        drawBackground(canvas)
        drawText(canvas)
        drawProgressBar(canvas)
        drawProgressCircle(canvas)
        super.onDraw(canvas)
    }

    private fun drawProgressCircle(canvas: Canvas?) {
        //TODO("Not yet implemented")
    }

    private fun drawProgressBar(canvas: Canvas?) {
        paint.color = buttonInProgressColor
        canvas?.drawRect(0F, 0F, widthProgressSize, heightSize.toFloat(), paint)
    }

    private fun drawText(canvas: Canvas?) {
        val xPos = ((widthSize / 2) - paint.measureText(currentText) / 2).toInt()
        val yPos = ((heightSize / 2) - (textPaint.descent() + textPaint.ascent()) / 2).toInt()
        canvas?.drawText(
            currentText, xPos.toFloat(),
            yPos.toFloat(), textPaint
        )
    }

    private fun drawBackground(canvas: Canvas?) {
        canvas?.drawColor(buttonIdleColor)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}