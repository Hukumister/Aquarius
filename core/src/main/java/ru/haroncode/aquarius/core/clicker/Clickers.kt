package ru.haroncode.aquarius.core.clicker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

object Clickers {

    fun <ItemModel, VH : RecyclerView.ViewHolder> none(): Clicker<ItemModel, VH> = NoneClicker()

    fun <ItemModel, VH : RecyclerView.ViewHolder> default(
        consumer: (ItemModel) -> Unit
    ): Clicker<ItemModel, VH> = DefaultClicker(consumer)

    internal class NoneClicker<ItemModel, VH : RecyclerView.ViewHolder> : Clicker<ItemModel, VH> {

        override fun onBindClicker(holder: VH) = Unit

        override fun invoke(holder: VH, view: View, item: ItemModel) = Unit

        override fun onUnbindClicker(holder: VH) = Unit
    }
}