package ru.haroncode.aquarius.renderers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_button.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.clicker.ClickableRenderer
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.ButtonRenderer.RenderContract

class ButtonRenderer<Item> : ItemBaseRenderer<Item, RenderContract>(), ClickableRenderer {

    interface RenderContract {
        val title: String
    }

    override val layoutRes: Int
        get() = R.layout.item_button

    override fun getItem(itemModel: Item): RenderContract = itemModel as RenderContract

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        viewHolder.itemView.button.text = item.title
    }

    override fun bindClickListener(
        viewHolder: RecyclerView.ViewHolder,
        listener: (RecyclerView.ViewHolder, View) -> Unit
    ) {
        viewHolder.itemView.button.setOnClickListener { listener(viewHolder, it) }
    }

    override fun unbindClickListener(viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.button.setOnClickListener(null)
    }
}