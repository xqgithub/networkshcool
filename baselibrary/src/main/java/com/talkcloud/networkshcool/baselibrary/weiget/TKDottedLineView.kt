package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View


/**
 * Author  guoyw
 * Date    2021/5/13
 * Desc    竖虚线
 */
open class TKDottedLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPath: Path

    init {
        mPaint.color = resources.getColor(android.R.color.white)
        // 需要加上这句，否则画不出东西
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 4f
        mPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)

        mPath = Path()
    }

    override fun onDraw(canvas: Canvas) {
        val centerX = width / 2
        mPath.reset()
        mPath.moveTo(centerX.toFloat(), 0f)
        mPath.lineTo(centerX.toFloat(), height.toFloat())
        canvas.drawPath(mPath, mPaint)
    }

}
