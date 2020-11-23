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
    )

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
            block: ParamBuilder<T>.() -> Unit
        ): Builder<T> {
            val ruleWithParam = ParamBuilder<T>()
                .apply(block)
                .create()
            ruleWithParams.add(ruleWithParam)
            return this
        }

        fun create() = SpaceDecoration(ruleWithParams, spanCount, orientation)

        class ParamBuilder<T : Any> {

            var start: Int = 0
            var top: Int = 0
            var end: Int = 0
            var bottom: Int = 0

            val startContainer: Int = -1
            val topContainer: Int = -1
            val endContainer: Int = -1
            val bottomContainer: Int = -1

            private var rule: DecorationRule = AnyRule()

            fun with(ruleBuilder: DecorationRuleBuilder<T>.() -> Unit) {
                rule = DecorationRuleBuilder<T>()
                    .apply(ruleBuilder)
                    .create()
            }

            fun create(): RuleWithParams<Param> {
                val param = Param(
                    start = start,
                    top = top,
                    end = end,
                    bottom = bottom,

                    startContainer = if (startContainer == -1) start else startContainer,
                    topContainer = if (topContainer == -1) start else topContainer,
                    endContainer = if (endContainer == -1) start else endContainer,
                    bottomContainer = if (bottomContainer == -1) start else bottomContainer
                )
                return RuleWithParams(rule, param)
            }
        }
    }
}