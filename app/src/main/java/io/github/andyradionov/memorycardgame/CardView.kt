package io.github.andyradionov.memorycardgame

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View

/**
 * @author Andrey Radionov
 */

class CardView : View {

    var isFlipped: Boolean = false
    var resourceId: Int = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr) {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CardView,
                0, 0)

        try {
            isFlipped = a.getBoolean(R.styleable.CardView_flipped, false)
            resourceId = a.getInteger(R.styleable.CardView_res_id, R.drawable.back)
        } finally {
            a.recycle()
        }
    }
}
