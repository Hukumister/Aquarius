package ru.haroncode.recycler.kit.core.differ.strategies.diffutil

interface ComparableItem {

    fun areContentsTheSame(other: ComparableItem): Boolean

    fun areItemsTheSame(other: ComparableItem): Boolean
}
