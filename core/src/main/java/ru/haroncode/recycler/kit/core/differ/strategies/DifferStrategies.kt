package ru.haroncode.recycler.kit.core.differ.strategies

import androidx.recyclerview.widget.DiffUtil
import ru.haroncode.recycler.kit.core.differ.core.DifferStrategy
import ru.haroncode.recycler.kit.core.differ.strategies.diffutil.ComparableDiffUtilItemCallback
import ru.haroncode.recycler.kit.core.differ.strategies.diffutil.ComparableItem
import ru.haroncode.recycler.kit.core.differ.strategies.diffutil.DiffUtilDifferStrategy

object DifferStrategies {

    fun <T : Any> none(): DifferStrategy<T> = NoneStrategy()

    fun <T : Any> withCallback(
        itemCallback: DiffUtil.ItemCallback<T>
    ): DifferStrategy<T> = DiffUtilDifferStrategy(itemCallback)

    fun <T : ComparableItem> withComparable(): DifferStrategy<T> =
        DiffUtilDifferStrategy(ComparableDiffUtilItemCallback())
}