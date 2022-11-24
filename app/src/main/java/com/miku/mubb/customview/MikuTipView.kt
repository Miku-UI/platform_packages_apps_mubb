package com.miku.mubb.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.miku.mubb.R

class MikuTipView : ConstraintLayout {
    private var tipText: String? = ""
    private var tipImg = R.mipmap.miku_loading
    init {
        inflate(context, R.layout.layout_miku_tip, this)
    }
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MikuTipView)
        tipText = typedArray.getString(R.styleable.MikuTipView_tipText)
        tipImg = typedArray.getResourceId(R.styleable.MikuTipView_tipImg, tipImg)
        setText(tipText).setImage(tipImg)
        typedArray.recycle()
    }

    fun setText(text: String?): MikuTipView {
        text?.apply {
            findViewById<TextView>(R.id.tv_miku_tip_sum).text = this
        }
        return this
    }

    fun setImage(resId: Int?): MikuTipView {
        resId?.apply {
            findViewById<ImageView>(R.id.iv_miku_tip_icon).setImageResource(resId)
        }
        return this
    }
}
