package ru.haroncode.aquarius.core.decorators

import android.graphics.Rect
import android.view.View
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView

/**
 * Decoration which add space between {@link RecyclerView} child's. Separate for space between child's and
 * space between child and {@link RecyclerView} container
 */
class SpaceDecoration private constructor(
    rulesWithParams: List<RuleWithParams<Param>>,
    @IntRange(from = 0) private val spanCount: Int = 1,
    @RecyclerView.Orientation private val orientation: Int = RecyclerView.VERTICAL
) : RuleItemDecoration<SpaceDecoration.Param>(rulesWithParams) {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        param: Param
    ) {
        super.getItemOffsets(outRect, view, parent, state, param)
        if (orientation == RecyclerView.VERTICAL) {
            outRect.left = if (isStartSpan(view, parent)) param.startContainer else param.start
            outRect.top = if (isFirstRow(view, parent)) param.topContainer else param.top
            outRect.right = if (isEndSpan(view, parent)) param.endContainer else param.end
            outRect.bottom = if (isLastRow(view, parent)) param.bottomContainer else param.bottom
        } else {
            outRect.left = if (isFirstRow(view, parent)) param.startContainer else param.start
            outRect.right = if (isLastRow(view, parent)) param.endContainer else param.end
            outRect.top = if (isStartSpan(view, parent)) param.topContainer else param.top
            outRect.bottom = if (isEndSpan(view, parent)) param.bottomContainer else param.bottom
        }

    }

    private fun isStartSpan(view: View, parent: RecyclerView): Boolean {
        val position = getPosition(parent, view)
        return position % spanCount == 0
    }

    private fun isFirstRow(view: View, parent: RecyclerView): Boolean {
        val position = getPosition(parent, view)
        return (position + 1).toFloat() / spanCount.toFloat() <= 1.0f
    }

    private fun isLastRow(view: View, parent: RecyclerView): Boolean {
        val position = getPosition(parent, view)
        val itemCount = parent.adapter?.itemCount ?: 0
        val remainder = itemCount % spanCount
        val lastRowItemCount = if (remainder == 0) spanCount else remainder
        return position >= itemCount - lastRowItemCount
    }

    private fun isEndSpan(view: View, parent: RecyclerView): Boolean {
        val position = getPosition(parent, view)
        return position % spanCount == spanCount - 1
    }

    private fun getPosition(parent: RecyclerView, view: View): Int = parent.getChildAdapterPosition(view)

    data class Param(
        val start: Int = 0,
        val top: Int = 0,
        val end: Int = 0,
        val bottom: Int = 0,
        val startContainer: Int = start,
        val topContainer: Int = top,
        val endContainer: Int = end,
        val bottomContainer: Int = bottom
    ) {

        constructor(vertical: Int = 0, horizontal: Int = 0) : this(
            start = horizontal,
            end = horizontal,
            top = vertical,
            bottom = vertical
        )

    }

    class Builder<T : Any> {

        private var spanCount: Int = 1
        @RecyclerView.Orientation private val orientation: Int = RecyclerView.VERTICAL

        private val ruleWithParams = mutableListOf<RuleWithParams<Param>>()

        fun withSpanCount(count: Int): Builder<T> {
            spanCount = count
            return this
        }

        fun withOrientation(count: Int): Builder<T> {
            spanCount = count
            return this
        }

        fun addRule(
            param: Param,
            ruleBuilder: DecoratorRuleBuilder<T>.() -> Unit = { any() }
        ): Builder<T> {
            val rule = DecoratorRuleBuilder<T>()
                .apply(ruleBuilder)
                .create()
            ruleWithParams.add(RuleWithParams(rule, param))
            return this
        }

        fun create() = SpaceDecoration(ruleWithParams, spanCount, orientation)
    }
}