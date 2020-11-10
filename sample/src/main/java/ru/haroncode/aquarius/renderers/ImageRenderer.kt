package ru.haroncode.aquarius.renderers

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_image.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.ImageRenderer.RenderContract

class ImageRenderer<Item> : ItemBaseRenderer<Item, RenderContract>() {

    interface RenderContract {
        val image: String
    }

    override val layoutRes: Int
        get() = R.layout.item_image

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        Glide.with(viewHolder.itemView)
            .load(item.image)
            .into(viewHolder.itemView.imageView)
    }
}