package ru.haroncode.aquarius

import ru.haroncode.aquarius.core.diffutil.ComparableItem
import ru.haroncode.aquarius.renderers.SimpleTextRenderer

sealed class Item : ComparableItem {

    data class SimpleTextItem(
        override val title: String,
        override val subtitle: String
    ) : Item(), SimpleTextRenderer.RenderContract {
        override fun areContentsTheSame(other: ComparableItem): Boolean {
            TODO("Not yet implemented")
        }

        override fun areItemsTheSame(other: ComparableItem): Boolean {
            TODO("Not yet implemented")
        }
    }
}