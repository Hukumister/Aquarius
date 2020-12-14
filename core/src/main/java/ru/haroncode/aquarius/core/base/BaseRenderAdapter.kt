package ru.haroncode.aquarius.core.base

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.BaseNotifier
import ru.haroncode.aquarius.core.MutableDiffer
import ru.haroncode.aquarius.core.Notifier
import ru.haroncode.aquarius.core.RenderAdapter
import ru.haroncode.aquarius.core.ViewTypeSelector
import ru.haroncode.aquarius.core.base.strategies.DifferStrategy
import ru.haroncode.aquarius.core.clicker.Clicker
import ru.haroncode.aquarius.core.helper.RenderItemTouchHelperCallback
import ru.haroncode.aquarius.core.observer.AdapterDataListUpdateCallback
import ru.haroncode.aquarius.core.observer.DataListUpdateCallback
import ru.haroncode.aquarius.core.renderer.BaseRenderer
import ru.haroncode.aquarius.core.util.moveSwap
import kotlin.reflect.KClass

class BaseRenderAdapter<T : Any>(
    differStrategy: DifferStrategy<T>,
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

    private val adapterObserver: DataListUpdateCallback = AdapterDataListUpdateCallback(this)

    override val differ: MutableDiffer<T> = BaseDiffer(differStrategy, adapterObserver)

    override val notifier: Notifier<T> = BaseNotifier(differ, adapterObserver)

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (differ is BaseDiffer) {
            differ.move(fromPosition, toPosition)
        }
    }

    override fun onItemDismiss(position: Int, direction: Int) = differ.removeAt(position)

    private class BaseDiffer<T>(
        private val differStrategy: DifferStrategy<T>,
        private val dataListUpdateCallback: DataListUpdateCallback
    ) : MutableDiffer<T> {

        private var actualItems = arrayListOf<T>()

        override val currentList: List<T>
            get() = actualItems

        override fun submitList(items: List<T>) {
            val calculateDiff = differStrategy.calculateDiff(actualItems, items)
            if (actualItems.isEmpty()) {
                actualItems.addAll(items)
            } else {
                actualItems = ArrayList(items)
            }
            calculateDiff.dispatchUpdatesTo(dataListUpdateCallback)
        }

        override fun removeAt(position: Int) {
            val calculateDiff = differStrategy.removeAtPosition(position)
            actualItems.removeAt(position)
            calculateDiff.dispatchUpdatesTo(dataListUpdateCallback)
        }

        fun move(fromPosition: Int, toPosition: Int) {
            val calculateDiff = differStrategy.move(fromPosition, toPosition)
            actualItems.moveSwap(fromPosition, toPosition)
            calculateDiff.dispatchUpdatesTo(dataListUpdateCallback)
        }

        override fun addAt(position: Int, item: T) {
            val calculateDiff = differStrategy.addAtPosition(position)
            actualItems.add(position, item)
            calculateDiff.dispatchUpdatesTo(dataListUpdateCallback)
        }

        override fun addAllAt(position: Int, items: List<T>) {
            val calculateDiff = differStrategy.addAtPosition(position, items.size)
            actualItems.addAll(position, items)
            calculateDiff.dispatchUpdatesTo(dataListUpdateCallback)
        }

        override fun add(item: T) {
            val calculateDiff = differStrategy.addAtPosition(actualItems.size)
            actualItems.add(item)
            calculateDiff.dispatchUpdatesTo(dataListUpdateCallback)
        }

        override fun addAll(items: List<T>) {
            val calculateDiff = differStrategy.addAtPosition(currentList.lastIndex, items.size)
            actualItems.addAll(items)
            calculateDiff.dispatchUpdatesTo(dataListUpdateCallback)
        }

        override fun replace(position: Int, item: T) {
            val calculateDiff = differStrategy.change(position)
            actualItems[position] = item
            calculateDiff.dispatchUpdatesTo(dataListUpdateCallback)
        }
    }
}
