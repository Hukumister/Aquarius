package ru.haroncode.aquarius.core.base

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.BaseRenderAdapter
import ru.haroncode.aquarius.core.renderer.BaseRenderer
import ru.haroncode.aquarius.core.Differ
import ru.haroncode.aquarius.core.base.strategies.DifferStrategy
import ru.haroncode.aquarius.core.clicker.Clicker
import ru.haroncode.aquarius.core.observer.AdapterDataSourceObserver
import ru.haroncode.aquarius.core.observer.DataSourceObserver
import kotlin.reflect.KClass

class RenderAdapter<T : Any>(
    differStrategy: DifferStrategy<T>,
    itemIdSelector: (T) -> Long,
    viewTypeSelector: (KClass<out T>) -> Int,
    clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    renderers: SparseArrayCompat<BaseRenderer<out T, *, out RecyclerView.ViewHolder>>
) : BaseRenderAdapter<T>(
    itemIdSelector = itemIdSelector,
    viewTypeSelector = { itemModel -> viewTypeSelector(itemModel::class) },
    clickers = clickers,
    renderers = renderers
) {

    override val differ: Differ<T> = BaseDiffer(differStrategy, AdapterDataSourceObserver(this))

    private class BaseDiffer<T>(
        private val differStrategy: DifferStrategy<T>,
        private val dataSourceObserver: DataSourceObserver
    ) : Differ<T> {

        private val actualItems = arrayListOf<T>()

        override val currentList: List<T>
            get() = actualItems

        override fun submitList(items: List<T>) {
            val calculateDiff = differStrategy.calculateDiff(actualItems, items)
            calculateDiff.dispatchUpdatesTo(dataSourceObserver)
        }
    }
}