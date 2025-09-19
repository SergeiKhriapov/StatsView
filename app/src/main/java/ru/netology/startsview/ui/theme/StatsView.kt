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
    private var lineWidth = AndroidUtils.dp(context, 20)
    private var colors = emptyList<Int>()
    private var unfilledColor = 0xFFEEEEEE.toInt()
    private var radius = 0f
    private var center = PointF()
    private var oval = RectF()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    var progress: Float = 360f // 0..360
        set(value) {
            field = value
            invalidate()
        }

    private var fillType: FillType = FillType.PARALLEL

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth.toFloat()).toInt()
            textPaint.textSize = textSize
            paint.strokeWidth = lineWidth.toFloat()

            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor())
            )

            unfilledColor = getColor(R.styleable.StatsView_unfilledColor, unfilledColor)

            val type = getInt(R.styleable.StatsView_fillType, 0)
            fillType = if (type == 0) FillType.PARALLEL else FillType.SEQUENTIAL
        }
    }

    var data: List<Float> = listOf(
        1F,
        1F,
        1F,
        1F
    )
        set(value) {
            field = value
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2f - lineWidth
        center = PointF(w / 2f, h / 2f)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) return

        when (fillType) {
            FillType.PARALLEL -> drawParallel(canvas)
            FillType.SEQUENTIAL -> drawSequential(canvas)
        }

        // Текст в центре
        canvas.drawText(
            "%.0f%%".format(progress / 360f * 100),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint
        )
    }

    private fun drawParallel(canvas: Canvas) {
        val totalParts = data.size
        val anglePerPart = 360f / totalParts

        for (i in data.indices) {
            paint.color = colors.getOrElse(i) { generateRandomColor() }
            val sweepAngle = anglePerPart * data[i] * (progress / 360f)
            val startAngle = -90f + i * anglePerPart
            if (sweepAngle > 0f) {
                canvas.drawArc(oval, startAngle, sweepAngle, false, paint)
            }
        }
    }

    private fun drawSequential(canvas: Canvas) {
        var startAngle = -90f
        val anglePerPart = 360f / data.size
        var remainingProgress = progress

        for (i in data.indices) {
            paint.color = colors.getOrElse(i) { generateRandomColor() }
            val sweepAngle = min(anglePerPart * data[i], remainingProgress)
            if (sweepAngle > 0f) {
                canvas.drawArc(oval, startAngle, sweepAngle, false, paint)
            }

            startAngle += anglePerPart
            remainingProgress -= sweepAngle
            if (remainingProgress <= 0f) break
        }
    }

    enum class FillType { PARALLEL, SEQUENTIAL }

    private fun generateRandomColor(): Int =
        Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}
