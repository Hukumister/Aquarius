package ru.haroncode.recycler.kit.core

import android.util.SparseArray
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.clicker.Clicker
import ru.haroncode.recycler.kit.core.differ.core.BaseDiffer
import ru.haroncode.recycler.kit.core.differ.core.DifferStrategy
import ru.haroncode.recycler.kit.core.observer.AdapterDataSourceObserver
import ru.haroncode.recycler.kit.core.observer.DataSourceObserver
import ru.haroncode.recycler.kit.core.observer.DataSourceUpdateCallback

class AsyncRenderer<ItemModel : Any>(
    itemCallback: DiffUtil.ItemCallback<ItemModel>,
    items: List<ItemModel>,
    differStrategy: DifferStrategy<ItemModel>,
    clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    itemIdSelector: (ItemModel) -> Long,
    viewTypeSelector: (ItemModel) -> Int,
    renderers: SparseArray<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>
) : BaseRenderAdapter<ItemModel>(
    items = items,
    differStrategy = differStrategy,
    itemClickers = clickers,
    itemIdSelector = itemIdSelector,
    viewTypeSelector = viewTypeSelector,
    renderers = renderers
) {

    override val differ: BaseDiffer<ItemModel> = AsyncDiffer(
        itemCallback = itemCallback,
        differStrategy = differStrategy,
        dataSourceObserver = AdapterDataSourceObserver(this)
    )

    private class AsyncDiffer<T : Any>(
        itemCallback: DiffUtil.ItemCallback<T>,
        differStrategy: DifferStrategy<T>,
        dataSourceObserver: DataSourceObserver
    ) : BaseDiffer<T>(
        differStrategy = differStrategy,
        dataSourceObserver = dataSourceObserver
    ) {

        private val asyncDiffer = AsyncListDiffer(
            DataSourceUpdateCallback(dataSourceObserver),
            AsyncDifferConfig.Builder(itemCallback).build()
        )

        override val currentList: List<T>
            get() = asyncDiffer.currentList

        override fun submitList(items: List<T>) {
            asyncDiffer.submitList(items)
        }
    }
}