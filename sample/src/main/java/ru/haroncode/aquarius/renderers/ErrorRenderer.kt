package ru.haroncode.aquarius.renderers

import kotlinx.android.synthetic.main.item_error.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.ErrorRenderer.RenderContract

class ErrorRenderer<T> : ItemBaseRenderer<T, RenderContract>() {

    interface RenderContract {
        val title: CharSequence
    }

    override val layoutRes: Int = R.layout.item_error

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        viewHolder.itemView.errorTitle.text = item.title
    }
}