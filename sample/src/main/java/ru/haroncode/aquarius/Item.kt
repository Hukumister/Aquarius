package ru.haroncode.aquarius

import ru.haroncode.aquarius.core.diffutil.ComparableItem
import ru.haroncode.aquarius.renderers.ButtonRenderer
import ru.haroncode.aquarius.renderers.CarouselRenderer
import ru.haroncode.aquarius.renderers.SimpleTextRenderer

sealed class Item : ComparableItem {

    data class Button(
        override val title: String
    ) : Item(), ButtonRenderer.RenderContract

    object Header : Item(), SimpleTextRenderer.RenderContract {
        override val title: String
            get() = "Header"
    }

    data class SimpleTextItem(
        override val title: String,
        override val subtitle: String
    ) : Item(), SimpleTextRenderer.RenderContract

    data class TextWithImageItem(
        override val title: String,
        override val subtitle: String
    ) : Item(), SimpleTextRenderer.RenderContract

    data class CarouselItem(
        override val images: List<CarouselRenderer.ImageItem>
    ) : Item(), CarouselRenderer.RenderContract
}
