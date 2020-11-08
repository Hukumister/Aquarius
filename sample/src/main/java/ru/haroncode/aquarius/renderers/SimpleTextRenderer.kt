package ru.haroncode.aquarius.renderers

import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.SimpleTextRenderer.RenderContract

class SimpleTextRenderer<Item> : ItemBaseRenderer<Item, RenderContract>() {

    interface RenderContract {
        val title: String
        val subtitle: String
    }

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        TODO("Not yet implemented")
    }

    override val layoutRes: Int
        get() = TODO("Not yet implemented")
}