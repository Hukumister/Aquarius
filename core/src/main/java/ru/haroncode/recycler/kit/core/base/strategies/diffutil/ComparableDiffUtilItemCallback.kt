package ru.haroncode.recycler.kit.core.base.strategies.diffutil

import androidx.recyclerview.widget.DiffUtil

class ComparableDiffUtilItemCallback<Item : ComparableItem> : DiffUtil.ItemCallback<Item>() {

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.areContentsTheSame(newItem)

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.areItemsTheSame(newItem)
}
