package ru.haroncode.recycler.kit.renderers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.BaseRenderer
import ru.haroncode.recycler.kit.renderers.SimpleTextRenderer.RenderContract

class SimpleTextRenderer<Item> : BaseRenderer<Item, RenderContract, RecyclerView.ViewHolder>() {

    interface RenderContract {
        val title: String
        val subtitle: String
    }

    override fun getItem(itemModel: Item): RenderContract = itemModel as RenderContract

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return
    }

    override fun onBindView(viewHolder: RecyclerView.ViewHolder, item: RenderContract) {
        viewHolder.itemView.setBackgroundColor()
    }
}