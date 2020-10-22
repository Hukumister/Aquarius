package ru.haroncode.recycler.kit.core.base

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.AbstractRenderAdapter
import ru.haroncode.recycler.kit.core.BaseRenderer
import ru.haroncode.recycler.kit.core.Differ
import ru.haroncode.recycler.kit.core.base.strategies.DifferStrategy
import ru.haroncode.recycler.kit.core.clicker.Clicker
import ru.haroncode.recycler.kit.core.observer.AdapterDataSourceObserver
import ru.haroncode.recycler.kit.core.observer.DataSourceObserver

class BaseRenderAdapter<ItemModel : Any>(
    differStrategy: DifferStrategy<ItemModel>,
    clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    itemIdSelector: (ItemModel) -> Long,
    viewTypeSelector: (Class<out ItemModel>) -> Int,
    renderers: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>
) : AbstractRenderAdapter<ItemModel>(
    clickers = clickers,
    itemIdSelector = itemIdSelector,
    viewTypeSelector = { itemModel -> viewTypeSelector(itemModel::class.java) },
    renderers = renderers
) {

    override val differ: Differ<ItemModel> = BaseDiffer(differStrategy, AdapterDataSourceObserver(this))

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