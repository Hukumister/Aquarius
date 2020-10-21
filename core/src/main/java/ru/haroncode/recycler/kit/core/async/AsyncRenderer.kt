package ru.haroncode.recycler.kit.core.async

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.AbstractRenderAdapter
import ru.haroncode.recycler.kit.core.BaseRenderer
import ru.haroncode.recycler.kit.core.Differ
import ru.haroncode.recycler.kit.core.clicker.Clicker
import ru.haroncode.recycler.kit.core.observer.AdapterDataSourceObserver
import ru.haroncode.recycler.kit.core.observer.ComposeDataSourceObserver
import ru.haroncode.recycler.kit.core.observer.DataSourceUpdateCallback

class AsyncRenderer<ItemModel : Any>(
    itemCallback: DiffUtil.ItemCallback<ItemModel>,
    clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    itemIdSelector: (ItemModel) -> Long,
    viewTypeSelector: (ItemModel) -> Int,
    renderers: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>
) : AbstractRenderAdapter<ItemModel>(
    differ = AsyncDiffer(itemCallback),
    clickers = clickers,
    itemIdSelector = itemIdSelector,
    viewTypeSelector = viewTypeSelector,
    renderers = renderers
) {

    init {
        differ as AsyncDiffer
        differ.dataSourceObserver.registerObserver(AdapterDataSourceObserver(this))
    }


    class Builder{


    }



    private class AsyncDiffer<T : Any>(
        itemCallback: DiffUtil.ItemCallback<T>,
        val dataSourceObserver: ComposeDataSourceObserver = ComposeDataSourceObserver()
    ) : Differ<T> {

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