package ru.haroncode.aquarius.renderers

import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.StaticLayoutRenderer.RenderContract

class StaticLayoutRenderer<T>(
    override val layoutRes: Int
) : ItemBaseRenderer<T, RenderContract>() {

    interface RenderContract

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) = Unit
}