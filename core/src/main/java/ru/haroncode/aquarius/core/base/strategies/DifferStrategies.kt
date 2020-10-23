package ru.haroncode.aquarius.core.base.strategies

import androidx.recyclerview.widget.DiffUtil
import ru.haroncode.aquarius.core.base.strategies.diffutil.ComparableDiffUtilItemCallback
import ru.haroncode.aquarius.core.base.strategies.diffutil.ComparableItem
import ru.haroncode.aquarius.core.base.strategies.diffutil.DiffUtilDifferStrategy

object DifferStrategies {

    fun <T : Any> none(): DifferStrategy<T> = NoneStrategy()

    fun <T : Any> withDiffUtil(
        itemCallback: DiffUtil.ItemCallback<T>
    ): DifferStrategy<T> = DiffUtilDifferStrategy(itemCallback)

    fun <T : ComparableItem> withDiffUtilComparable(): DifferStrategy<T> =
        DiffUtilDifferStrategy(ComparableDiffUtilItemCallback())
}