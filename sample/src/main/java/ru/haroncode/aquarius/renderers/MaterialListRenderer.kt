package ru.haroncode.aquarius.renderers

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_material_list.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.MaterialListRenderer.RenderContract

class MaterialListRenderer<T> : ItemBaseRenderer<T, RenderContract>() {

    interface RenderContract {
        val image: String
        val title: CharSequence
        val caption: CharSequence
    }

    override val layoutRes: Int = R.layout.item_material_list

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        viewHolder.itemView.titleView.text = item.title
        viewHolder.itemView.captionView.text = item.caption

        Glide.with(viewHolder.itemView)
            .load(item.image)
            .into(viewHolder.itemView.imageView)
    }
}