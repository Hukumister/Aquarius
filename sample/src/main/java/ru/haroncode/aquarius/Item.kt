package ru.haroncode.aquarius

import ru.haroncode.aquarius.core.diffutil.ComparableItem
import ru.haroncode.aquarius.renderers.ButtonRenderer
import ru.haroncode.aquarius.renderers.CardRenderer
import ru.haroncode.aquarius.renderers.CarouselRenderer
import ru.haroncode.aquarius.renderers.ImageRenderer
import ru.haroncode.aquarius.renderers.SimpleTextRenderer
import ru.haroncode.aquarius.renderers.TitleRenderer
import java.util.*

sealed class Item : ComparableItem {

    data class Button(
        override val title: String,
        override val id: Int? = null,
        val uuid: String = UUID.randomUUID().toString()
    ) : Item(), ButtonRenderer.RenderContract

    data class Title(
        override val titleRes: Int?,
        val uuid: String = UUID.randomUUID().toString()
    ) : Item(), TitleRenderer.RenderContract

    data class SimpleTextItem(
        override val title: String,
        override val subtitle: String? = null,
        val uuid: String = UUID.randomUUID().toString()
    ) : Item(), SimpleTextRenderer.RenderContract

    data class CarouselItem(
        override val images: List<CarouselRenderer.ImageItem>,
        val uuid: String = UUID.randomUUID().toString()
    ) : Item(), CarouselRenderer.RenderContract

    data class CardItem(
        override val title: String,
        override val subtitle: String,
        val uuid: String = UUID.randomUUID().toString()
    ) : Item(), CardRenderer.RenderContract

    data class ImageItem(
        override val image: String
    ) : Item(), ImageRenderer.RenderContract
}
