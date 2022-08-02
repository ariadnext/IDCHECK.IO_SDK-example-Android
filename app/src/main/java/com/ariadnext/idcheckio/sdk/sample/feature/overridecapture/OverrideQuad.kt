package com.ariadnext.idcheckio.sdk.sample.feature.overridecapture

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.ariadnext.idcheckio.sdk.bean.Quad

/** Custom view used to show a quad */
@SuppressLint("ViewConstructor")
class OverrideQuad(context: Context, quad: Quad, private val useFaceQuad: Boolean) : View(context) {
    private var paint: Paint = Paint()

    /** Store the quad */
    var quad: Quad = quad
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.color = Color.RED
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.FILL
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        if(useFaceQuad) {
            val height = quad.leftBottom.y - quad.leftTop.y
            val heightFaceMargin = height * 0.15
            paint.style = Paint.Style.STROKE
            canvas.drawOval(quad.leftTop.x.toFloat(), (quad.leftTop.y - heightFaceMargin).toFloat(),
                quad.rightBottom.x.toFloat(), (quad.rightBottom.y + heightFaceMargin).toFloat(), paint)
        } else {
            canvas.drawCircle(
                quad.leftTop.x.toFloat(),
                quad.leftTop.y.toFloat(),
                CIRCLE_RADIUS.toFloat(),
                paint
            )
            canvas.drawCircle(
                quad.leftBottom.x.toFloat(),
                quad.leftBottom.y.toFloat(),
                CIRCLE_RADIUS.toFloat(),
                paint
            )
            canvas.drawCircle(
                quad.rightTop.x.toFloat(),
                quad.rightTop.y.toFloat(),
                CIRCLE_RADIUS.toFloat(),
                paint
            )
            canvas.drawCircle(
                quad.rightBottom.x.toFloat(),
                quad.rightBottom.y.toFloat(),
                CIRCLE_RADIUS.toFloat(),
                paint
            )
        }
    }

    companion object {
        /** Circle radius constant  */
        private const val CIRCLE_RADIUS: Int = 10
    }
}

