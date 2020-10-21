package ru.haroncode.recycler.kit.core.base.strategies

import androidx.recyclerview.widget.DiffUtil
import ru.haroncode.recycler.kit.core.base.strategies.diffutil.ComparableDiffUtilItemCallback
import ru.haroncode.recycler.kit.core.base.strategies.diffutil.ComparableItem
import ru.haroncode.recycler.kit.core.base.strategies.diffutil.DiffUtilDifferStrategy

object DifferStrategies {

    fun <T : Any> none(): DifferStrategy<T> = NoneStrategy()

    fun <T : Any> withCallback(
        itemCallback: DiffUtil.ItemCallback<T>
    ): DifferStrategy<T> = DiffUtilDifferStrategy(itemCallback)

    fun <T : ComparableItem> withComparable(): DifferStrategy<T> =
        DiffUtilDifferStrategy(ComparableDiffUtilItemCallback())
}