package ru.haroncode.aquarius

import ru.haroncode.aquarius.core.diffutil.ComparableItem
import ru.haroncode.aquarius.renderers.ButtonRenderer
import ru.haroncode.aquarius.renderers.CardRenderer
import ru.haroncode.aquarius.renderers.CarouselRenderer
import ru.haroncode.aquarius.renderers.ImageRenderer
import ru.haroncode.aquarius.renderers.SimpleTextRenderer
import ru.haroncode.aquarius.renderers.TitleRenderer

sealed class Item : ComparableItem {

    data class Button(
        override val title: String,
        override val id: Int? = null
    ) : Item(), ButtonRenderer.RenderContract

    data class Title(
        override val titleRes: Int?
    ) : Item(), TitleRenderer.RenderContract

    data class SimpleTextItem(
        override val title: String,
        override val subtitle: String? = null
    ) : Item(), SimpleTextRenderer.RenderContract

    data class CarouselItem(
        override val images: List<CarouselRenderer.ImageItem>
    ) : Item(), CarouselRenderer.RenderContract

    data class CardItem(
        override val title: String,
        override val subtitle: String
    ) : Item(), CardRenderer.RenderContract

    data class ImageItem(
        override val image: String
    ) : Item(), ImageRenderer.RenderContract
}
