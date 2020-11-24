package ru.haroncode.aquarius.core.decorators

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView

abstract class RuleItemDecoration<T>(
    private val rulesWithParams: List<RuleWithParams<T>>
) : RecyclerView.ItemDecoration() {

    data class RuleWithParams<T>(
        val rule: DecorationRule,
        val param: T
    )

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        rulesWithParams
            .filter { ruleWithParam -> ruleWithParam.rule.resolve(parent.getChildAdapterPosition(view), parent) }
            .maxByOrNull { ruleWithParams -> ruleWithParams.rule.weight() }
            ?.let { ruleWithParam -> getItemOffsets(outRect, view, parent, state, ruleWithParam.param) }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        parent.forEach { child ->
            rulesWithParams
                .filter { ruleWithParam -> ruleWithParam.rule.resolve(parent.getChildAdapterPosition(child), parent) }
                .maxByOrNull { ruleWithParams -> ruleWithParams.rule.weight() }
                ?.let { ruleWithParam -> onDraw(canvas, child, parent, state, ruleWithParam.param) }
        }
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        parent.forEach { child ->
            rulesWithParams
                .filter { ruleWithParam -> ruleWithParam.rule.resolve(parent.getChildAdapterPosition(child), parent) }
                .maxByOrNull { ruleWithParams -> ruleWithParams.rule.weight() }
                ?.let { ruleWithParam -> onDrawOver(canvas, child, parent, state, ruleWithParam.param) }
        }
    }

    protected open fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        param: T
    ) = Unit

    protected open fun onDraw(
        canvas: Canvas,
        child: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        param: T
    ) = Unit

    protected open fun onDrawOver(
        canvas: Canvas,
        child: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        param: T
    ) = Unit
}