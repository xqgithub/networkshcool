package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.github.forjrking.drawable.Shape
import com.github.forjrking.drawable.selectorDrawable
import com.github.forjrking.drawable.shapeDrawable
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.utils.ConvertUtils
import kotlinx.android.synthetic.main.tk_layout_custom_color_botton.view.*

/**
 * Author  guoyw
 * Date    2021/5/12
 * Desc    自定义主题颜色按钮
 */
class TKCustomColorButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mPrimaryColor: String = VariableConfig.color_button_selected
    private var mPrimaryColorDark: String = VariableConfig.color_button_unselected

    private var mText: String = ""

    private var mEnable: Boolean = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TKCustomColorButton)

        val primaryColor = typedArray.getString(R.styleable.TKCustomColorButton_ccbPrimaryColor)
        val primaryColorDark =
            typedArray.getString(R.styleable.TKCustomColorButton_ccbPrimaryColorDark)
        val text = typedArray.getString(R.styleable.TKCustomColorButton_ccbText)

        mEnable = typedArray.getBoolean(R.styleable.TKCustomColorButton_ccbEnable, false)

        typedArray.recycle()

        text?.let {
            mText = it;
        }

        primaryColor?.let {
            mPrimaryColor = it
        }

        primaryColorDark?.let {
            mPrimaryColorDark = it
        }

        // 从sp中获取颜色值
//        var primaryColorValue = AppPrefsUtil.getPrimaryColor()
//
//        if (!TextUtils.isEmpty(primaryColorValue)) {
//            mPrimaryColor = primaryColorValue
//            // TODO 这里是直接有值 还是半透明 后期调整
//            mPrimaryColorDark = "80$primaryColorValue"
//        }

        initView()
    }

    private fun initView() {

        View.inflate(context, R.layout.tk_layout_custom_color_botton, this)

        customButtonTv.background = selectorDrawable {
            //默认样式
            normal = shapeDrawable {
                shape(Shape.RECTANGLE)
                corner(ConvertUtils.dp2px(15f).toFloat(), false)
                solid(mPrimaryColor)
            }
            //不可用
            unable = shapeDrawable {
                shape(Shape.RECTANGLE)
                corner(ConvertUtils.dp2px(15f).toFloat(), false)
                solid(mPrimaryColorDark)
            }
        }

        customButtonTv.text = mText

        customButtonTv.isEnabled = mEnable;

        this.isEnabled = mEnable
    }

    fun setBtnEnable(flag: Boolean) {
        this.mEnable = flag
        this.isEnabled = mEnable

        customButtonTv.isEnabled = flag

    }

    // 设置文字
    fun setBtnText(text: String) {
        this.mText = text

        customButtonTv.text = text
    }

}