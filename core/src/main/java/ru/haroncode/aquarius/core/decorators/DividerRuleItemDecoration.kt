package ru.haroncode.aquarius.core.decorators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.decorators.DividerRuleItemDecoration.Param
import ru.haroncode.aquarius.core.decorators.view.Gravity
import ru.haroncode.aquarius.core.decorators.view.Padding
import ru.haroncode.aquarius.core.util.resolveDrawableAttr
import java.util.*

class DividerRuleItemDecoration private constructor(
    ruleWithParams: List<RuleWithParams<Param>>,
    context: Context
) : RuleItemDecoration<Param>(ruleWithParams) {

    private val listDivider = context.resolveDrawableAttr(android.R.attr.listDivider)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        param: Param
    ) {
        super.getItemOffsets(outRect, view, parent, state, param)
        val listDivider = param.drawable ?: listDivider ?: return
        if (Gravity.START in param) outRect.left = listDivider.intrinsicHeight
        if (Gravity.TOP in param) outRect.top = listDivider.intrinsicHeight
        if (Gravity.END in param) outRect.right = listDivider.intrinsicHeight
        if (Gravity.BOTTOM in param) outRect.bottom = listDivider.intrinsicHeight
    }

    override fun onDrawOver(
        canvas: Canvas,
        child: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        param: Param
    ) {
        super.onDrawOver(canvas, child, parent, state, param)
        val listDivider = param.drawable ?: listDivider ?: return
        param.gravities.forEach { gravity ->
            val padding = param.paddingMap[gravity] ?: Padding()
            drawDivider(canvas, child, gravity, padding, listDivider)
        }
    }

    private fun drawDivider(
        canvas: Canvas,
        child: View,
        gravity: Gravity,
        padding: Padding,
        listDivider: Drawable
    ) {

        if (listDivider.intrinsicHeight <= 0) return
        val dividerHeight = listDivider.intrinsicHeight
        when (gravity) {
            Gravity.START -> listDivider.setBounds(
                child.left - dividerHeight,
                child.top + padding.start,
                child.left,
                child.bottom - padding.end
            )
            Gravity.TOP -> listDivider.setBounds(
                child.left + padding.start,
                child.top - dividerHeight,
                child.right - padding.end,
                child.top
            )
            Gravity.END -> listDivider.setBounds(
                child.right,
                child.top + padding.start,
                child.right + dividerHeight,
                child.bottom - padding.end
            )
            Gravity.BOTTOM -> listDivider.setBounds(
                child.left + padding.start,
                child.bottom,
                child.right - padding.end,
                child.bottom + dividerHeight
            )
        }
        listDivider.draw(canvas)
    }

    data class Param(
        val drawable: Drawable? = null,
        val gravities: EnumSet<Gravity> = EnumSet.noneOf(Gravity::class.java),
        val paddingMap: EnumMap<Gravity, Padding> = EnumMap(Gravity::class.java)
    ) {

        operator fun contains(gravity: Gravity): Boolean = gravity in gravities
    }

    class ParamBuilder<T : Any> {

        private var rule: DecorationRule = AnyRule()
        private var param: Param = Param()

        fun drawable(drawable: Drawable) {
            param = param.copy(drawable = drawable)
        }

        fun gravity(gravity: Gravity, startPadding: Int = 0, endPadding: Int = 0) {
            param = param.copy(
                gravities = param.gravities.apply { add(gravity) },
                paddingMap = param.paddingMap.apply {
                    put(gravity, Padding(startPadding, endPadding))
                }
            )
        }

        fun with(ruleBuilder: DecorationRuleBuilder<T>.() -> Unit) {
            rule = DecorationRuleBuilder<T>()
                .apply(ruleBuilder)
                .create()
        }

        fun create() = RuleWithParams(rule, param)
    }

    class Builder<T : Any>(private val context: Context) {

        private val ruleWithParams = mutableListOf<RuleWithParams<Param>>()

        fun addRule(
            paramBuilder: ParamBuilder<T>.() -> Unit
        ): Builder<T> {
            val param = ParamBuilder<T>()
                .apply(paramBuilder)
                .create()
            ruleWithParams.add(param)
            return this
        }

        fun create() = DividerRuleItemDecoration(ruleWithParams, context)
    }
}