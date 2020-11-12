package ru.haroncode.aquarius.renderers

import kotlinx.android.synthetic.main.item_text.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.SimpleTextRenderer.RenderContract

class SimpleTextRenderer<Item> : ItemBaseRenderer<Item, RenderContract>() {

    interface RenderContract {
        val title: String
        val subtitle: String?
            get() = null
    }

    override val layoutRes: Int = R.layout.item_text

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        viewHolder.itemView.title.text = item.title
        viewHolder.itemView.subtitle.text = item.subtitle
    }
}
