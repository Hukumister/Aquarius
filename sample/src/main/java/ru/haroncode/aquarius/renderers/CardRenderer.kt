package ru.haroncode.aquarius.renderers

import kotlinx.android.synthetic.main.item_card.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.CardRenderer.RenderContract

class CardRenderer<Item> : ItemBaseRenderer<Item, RenderContract>() {

    interface RenderContract {
        val title: CharSequence
        val subtitle: CharSequence
    }

    override val layoutRes: Int
        get() = R.layout.item_card

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        with(viewHolder) {
            itemView.title.text = item.title
            itemView.subtitle.text = item.subtitle
        }
    }
}