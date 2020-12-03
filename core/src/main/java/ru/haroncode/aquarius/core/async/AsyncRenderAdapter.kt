package ru.haroncode.aquarius.core.async

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.Differ
import ru.haroncode.aquarius.core.RenderAdapter
import ru.haroncode.aquarius.core.ViewTypeSelector
import ru.haroncode.aquarius.core.clicker.Clicker
import ru.haroncode.aquarius.core.helper.RenderItemTouchHelperCallback
import ru.haroncode.aquarius.core.observer.AdapterDataSourceObserver
import ru.haroncode.aquarius.core.observer.DataSourceObserver
import ru.haroncode.aquarius.core.observer.DataSourceUpdateCallback
import ru.haroncode.aquarius.core.renderer.BaseRenderer
import kotlin.reflect.KClass

class AsyncRenderAdapter<T : Any>(
    itemCallback: DiffUtil.ItemCallback<T>,
    itemIdSelector: (T) -> Long,
    touchHelperCallback: RenderItemTouchHelperCallback,
    viewTypeSelector: ViewTypeSelector<KClass<out T>>,
    clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    renderers: SparseArrayCompat<BaseRenderer<out T, *, out RecyclerView.ViewHolder>>
) : RenderAdapter<T>(
    itemIdSelector = itemIdSelector,
    viewTypeSelector = object : ViewTypeSelector<T> {
        override fun createViewTypeFor(item: T): Int = viewTypeSelector.createViewTypeFor(item::class)

        override fun viewTypeFor(item: T): Int = viewTypeSelector.viewTypeFor(item::class)
    },
    clickers = clickers,
    renderers = renderers,
    touchHelperCallback = touchHelperCallback
) {

    override val differ: Differ<T> = AsyncDiffer(itemCallback, AdapterDataSourceObserver(this))
    override fun onItemDismiss(position: Int, direction: Int) {
        val newList = differ.currentList.toMutableList().apply { removeAt(position) }
        differ.submitList(newList)
    }

    private class AsyncDiffer<T : Any>(
        itemCallback: DiffUtil.ItemCallback<T>,
        dataSourceObserver: DataSourceObserver
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