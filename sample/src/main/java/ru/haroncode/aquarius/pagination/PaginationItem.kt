package ru.haroncode.aquarius.pagination

import ru.haroncode.aquarius.renderers.ErrorRenderer
import ru.haroncode.aquarius.renderers.MaterialListRenderer
import ru.haroncode.aquarius.renderers.StaticLayoutRenderer

sealed class PaginationItem {

    class SimpleItemWithImage(
        override val image: String,
        override val title: CharSequence,
        override val caption: CharSequence
    ) : PaginationItem(), MaterialListRenderer.RenderContract

    class ErrorItem(
        override val title: CharSequence
    ) : PaginationItem(), ErrorRenderer.RenderContract

    object LoadingItem : PaginationItem(), StaticLayoutRenderer.RenderContract
}