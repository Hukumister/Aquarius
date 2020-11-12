package ru.haroncode.aquarius.renderers

import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.item_title.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer

class TitleRenderer<Item> : ItemBaseRenderer<Item, TitleRenderer.RenderContract>() {

    interface RenderContract {
        @get:StringRes val titleRes: Int?
        val title: CharSequence?
            get() = null
    }

    override val layoutRes: Int = R.layout.item_title

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        val titleRes = item.titleRes
        val title = item.title

        viewHolder.itemView.title.text = when {
            titleRes != null -> viewHolder.itemView.context.getString(titleRes)
            else -> title
        }
    }

}