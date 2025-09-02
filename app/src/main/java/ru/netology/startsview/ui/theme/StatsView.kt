package ru.netology.startsview.ui.theme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.netology.startsview.R
import ru.netology.startsview.utils.AndroidUtils
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private var textSize = AndroidUtils.dp(context, 20).toFloat()
    private var radius = 0F
    private var center = PointF()
    private var oval = RectF()
    private var lineWidth = AndroidUtils.dp(context, 20)
    private var colors = emptyList<Int>()
    private var unfilledColor = 0xFFEEEEEE.toInt() // супер светло-серый

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = lineWidth.toFloat()
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = this@StatsView.textSize
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth.toFloat()).toInt()
            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor())
            )
            unfilledColor = getColor(R.styleable.StatsView_unfilledColor, unfilledColor)
        }
    }

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) return

        val totalParts = 4f
        val filledParts = data.size.toFloat()
        val anglePerPart = 360f / totalParts
        val filledAngle = anglePerPart * filledParts
        var startAngle = -90f

        for (i in data.indices.reversed()) {
            paint.color = colors.getOrElse(i) { generateRandomColor() }
            canvas.drawArc(oval, startAngle, anglePerPart, false, paint)
            startAngle += anglePerPart
        }

        if (filledAngle < 360f) {
            paint.color = unfilledColor
            canvas.drawArc(oval, startAngle, 360f - filledAngle, false, paint)
        }

        canvas.drawText(
            "%.0f%%".format(filledAngle / 360f * 100),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint
        )
    }


    private fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}

