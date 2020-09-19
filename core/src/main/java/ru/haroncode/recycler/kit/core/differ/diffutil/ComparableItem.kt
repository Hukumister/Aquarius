package ru.haroncode.recycler.kit.core.differ.diffutil

interface ComparableItem {

    fun areContentsTheSame(other: ComparableItem): Boolean

    fun areItemsTheSame(other: ComparableItem): Boolean
}
