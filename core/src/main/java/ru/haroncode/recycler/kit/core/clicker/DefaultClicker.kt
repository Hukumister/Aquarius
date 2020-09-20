package ru.haroncode.recycler.kit.core.clicker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DefaultClicker<ItemModel, VH : RecyclerView.ViewHolder>(
    private val consumer: (ItemModel) -> Unit
) : Clicker<ItemModel, VH> {

    override fun onBindClicker(holder: VH) = Unit

    override fun invoke(holder: VH, view: View, item: ItemModel) = consumer(item)

    override fun onUnbindClicker(holder: VH) = Unit
}
