package ru.haroncode.aquarius.renderers

import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.ImageRenderer.RenderContract

class ImageRenderer<Item>(
) : ItemBaseRenderer<Item, RenderContract>() {

    interface RenderContract {
        val image: String
    }

    override val layoutRes: Int
        get() = R.layout.item_image

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        //TODO("load image")
    }
}