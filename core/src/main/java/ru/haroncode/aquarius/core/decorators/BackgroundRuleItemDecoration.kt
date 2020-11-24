package ru.haroncode.aquarius.core.decorators

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.decorators.BackgroundRuleItemDecoration.Background

class BackgroundRuleItemDecoration private constructor(
    rulesWithParams: List<RuleWithParams<Background>>
) : RuleItemDecoration<Background>(rulesWithParams) {

    private val tempRect = Rect()

    override fun onDraw(
        canvas: Canvas,
        child: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        param: Background
    ) {
        super.onDraw(canvas, child, parent, state, param)
        if (param.fillAllSpace) {
            parent.getDecoratedBoundsWithMargins(child, tempRect)
            param.draw(canvas, tempRect.left, tempRect.top, tempRect.right, tempRect.bottom)
        } else {
            param.draw(canvas, child.left, child.top, child.right, child.bottom)
        }

    }

    sealed class Background {

        /**
         * Fill all an available item space included decorations and margins
         */
        open val fillAllSpace: Boolean = false

        abstract fun draw(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int)

        data class DrawableBackground(
            val drawable: Drawable,
            override val fillAllSpace: Boolean = false
        ) : Background() {

            override fun draw(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
                drawable.setBounds(left, top, right, bottom)
                drawable.draw(canvas)
            }
        }

        data class ColorBackground(
            @ColorInt val colorInt: Int,
            override val fillAllSpace: Boolean = false
        ) : Background() {

            private val paint = Paint().apply { color = colorInt }

            override fun draw(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
            }
        }
    }

    class ParamBuilder<T : Any> {

        private var rule: DecorationRule = AnyRule()
        private lateinit var param: Background

        fun drawable(drawable: Drawable, fillAllSpace: Boolean = false) {
            param = Background.DrawableBackground(drawable, fillAllSpace)
        }

        fun color(@ColorInt colorInt: Int, fillAllSpace: Boolean = false) {
            param = Background.ColorBackground(colorInt, fillAllSpace)
        }

        fun with(ruleBuilder: DecorationRuleBuilder<T>.() -> Unit) {
            rule = DecorationRuleBuilder<T>()
                .apply(ruleBuilder)
                .create()
        }

        fun create() = RuleWithParams(rule, param)
    }

    class Builder<T : Any> {

        private val ruleWithParams = mutableListOf<RuleWithParams<Background>>()

        fun addRule(
            paramBuilder: ParamBuilder<T>.() -> Unit,
        ): Builder<T> {
            val params = ParamBuilder<T>()
                .apply(paramBuilder)
                .create()
            ruleWithParams.add(params)
            return this
        }

        fun create() = BackgroundRuleItemDecoration(ruleWithParams)
    }
}