package ru.haroncode.recycler.kit.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface ItemClicker<ItemModel, VH : RecyclerView.ViewHolder> {

    fun onBindClicker(holder: VH)

    fun invoke(holder: VH, view: View, item: ItemModel)

    fun onUnbindClicker(holder: VH)

    class NoneClicker<ItemModel, VH : RecyclerView.ViewHolder> : ItemClicker<ItemModel, VH> {

        override fun onBindClicker(holder: VH) = Unit

        override fun invoke(holder: VH, view: View, item: ItemModel) = Unit

        override fun onUnbindClicker(holder: VH) = Unit
    }
}
