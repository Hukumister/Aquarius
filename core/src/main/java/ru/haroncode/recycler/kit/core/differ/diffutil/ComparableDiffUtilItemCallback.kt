package ru.haroncode.recycler.kit.core.differ.diffutil

import ru.haroncode.wordlearn.common.ui.adapter.SimpleItemDiffCallback

class ComparableDiffUtilItemCallback<Item : ComparableItem> : SimpleItemDiffCallback<Item>() {

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.areContentsTheSame(newItem)

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.areItemsTheSame(newItem)
}
