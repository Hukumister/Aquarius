package ru.haroncode.recycler.kit.core.base.strategies.diffutil

interface ComparableItem {

    fun areContentsTheSame(other: ComparableItem): Boolean

    fun areItemsTheSame(other: ComparableItem): Boolean
}
