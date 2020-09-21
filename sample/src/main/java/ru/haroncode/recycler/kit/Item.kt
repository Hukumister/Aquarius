package ru.haroncode.recycler.kit

import ru.haroncode.recycler.kit.renderers.SimpleTextRenderer

sealed class Item {

    data class SimpleTextItem(
        override val title: String,
        override val subtitle: String
    ) : SimpleTextRenderer.RenderContract
}