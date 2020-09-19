package ru.haroncode.recycler.kit.core

import android.view.View

class DefaultItemClicker<ItemModel, VH : BaseViewHolder>(
    private val consumer: (ItemModel) -> Unit
) : ItemClicker<ItemModel, VH> {

    override fun onBindClicker(holder: VH) = Unit

    override fun invoke(holder: VH, view: View, item: ItemModel) = consumer(item)

    override fun onUnbindClicker(holder: VH) = Unit
}
