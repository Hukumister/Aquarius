package ru.haroncode.recycler.kit

import ru.haroncode.recycler.kit.core.base.strategies.diffutil.ComparableItem
import ru.haroncode.recycler.kit.renderers.SimpleTextRenderer

sealed class Item :ComparableItem{

    data class SimpleTextItem(
        override val title: String,
        override val subtitle: String
    ) : SimpleTextRenderer.RenderContract
}