package ru.haroncode.aquarius.core.diffutil

interface ComparableItem {

    fun areContentsTheSame(other: ComparableItem): Boolean = this == other

    fun areItemsTheSame(other: ComparableItem): Boolean = this == other
}
