package ru.haroncode.recycler.kit.core.base

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.AbstractRenderAdapter
import ru.haroncode.recycler.kit.core.AbstractRenderAdapter.MultipleViewTypesBuilder
import ru.haroncode.recycler.kit.core.BaseRenderer
import ru.haroncode.recycler.kit.core.Differ
import ru.haroncode.recycler.kit.core.base.strategies.DifferStrategy
import ru.haroncode.recycler.kit.core.clicker.Clicker
import ru.haroncode.recycler.kit.core.clicker.Clickers
import ru.haroncode.recycler.kit.core.observer.AdapterDataSourceObserver
import ru.haroncode.recycler.kit.core.observer.ComposeDataSourceObserver

class BaseRenderAdapter<ItemModel : Any>(
    differStrategy: DifferStrategy<ItemModel>,
    clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    itemIdSelector: (ItemModel) -> Long,
    viewTypeSelector: (Class<out ItemModel>) -> Int,
    renderers: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>
) : AbstractRenderAdapter<ItemModel>(
    differ = BaseDiffer(differStrategy),
    clickers = clickers,
    itemIdSelector = itemIdSelector,
    viewTypeSelector = { itemModel -> viewTypeSelector(itemModel::class.java) },
    renderers = renderers
) {

    init {
        differ as BaseDiffer
        differ.dataSourceObserver.registerObserver(AdapterDataSourceObserver(this))
    }

    class Builder<ItemModel : Any> {

        private val itemIdSelector: (ItemModel) -> Long,
        private val hasStableIds: Boolean,
        private val renderers: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>> =
            SparseArrayCompat(3)
        private val clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>> = SparseArrayCompat(3)

        fun <E, VH : RecyclerView.ViewHolder> single(
            renderer: BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>,
            itemClicker: Clicker<E, VH> = Clickers.none()
        ): Builder<ItemModel> {
            //todo add check
            renderers.put(DEFAULT_VIEW_TYPE, renderer)
            clickers.put(DEFAULT_VIEW_TYPE, itemClicker)
            return this
        }


        fun withItemIdSelector(itemIdSelector: (ItemModel) -> Long, hasStableIds: Boolean): Builder<ItemModel> {
            this.itemIdSelector = itemIdSelector
            this.hasStableIds = hasStableIds
            return this
        }

        fun withDifferStrategy(differStrategy: DifferStrategy<ItemModel>): Builder<ItemModel> {
            this.differStrategy = differStrategy
            return this
        }

        fun <E, VH : RecyclerView.ViewHolder> singleViewType(
            baseRenderer: BaseRenderer<out ItemModel, E, VH>,
            clicker: Clicker<E, VH> = Clickers.none()
        ): SingleViewTypeBuilder<ItemModel> =
            SingleViewTypeBuilder(differStrategy, itemIdSelector, hasStableIds, baseRenderer, clicker)

        fun withViewTypeSelector(viewTypeSelector: (ItemModel) -> Int): MultipleViewTypesBuilder<ItemModel> =
            MultipleViewTypesBuilder(itemIdSelector, hasStableIds, viewTypeSelector)

        companion object {
            private const val DEFAULT_VIEW_TYPE = 0
        }
    }

    class SingleViewTypeBuilder<ItemModel : Any>(
        private val diff: DifferStrategy<ItemModel>,
        private val itemIdSelector: (ItemModel) -> Long,
        private val hasStableIds: Boolean,
        private val viewHolderRenderer: BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>,
        private val clicker: Clicker<*, out RecyclerView.ViewHolder>
    ) {

        companion object {
            private const val DEFAULT_VIEW_TYPE = 0
        }

        fun build(): BaseRenderAdapter<ItemModel> {
            val renderers = SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>(1)
            renderers.put(DEFAULT_VIEW_TYPE, viewHolderRenderer)

            val itemClickers = SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>(1)
            itemClickers.put(DEFAULT_VIEW_TYPE, clicker)

            return BaseRenderAdapter(
                differStrategy = diff,
                itemIdSelector = itemIdSelector,
                viewTypeSelector = { DEFAULT_VIEW_TYPE },
                renderers = renderers,
                clickers = itemClickers
            ).apply {
                setHasStableIds(hasStableIds)
            }
        }
    }

    class MultipleViewTypesBuilder<ItemModel : Any>(
        private val items: List<ItemModel>,
        private val differStrategy: DifferStrategy<ItemModel>,
        private val itemIdSelector: (ItemModel) -> Long,
        private val hasStableIds: Boolean,
        private val viewTypeSelector: (ItemModel) -> Int
    ) {

        private val renderers: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>> =
            SparseArrayCompat(3)
        private val clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>> = SparseArrayCompat(3)

        fun add(
            viewType: Int,
            viewHolderRenderer: BaseRenderer<ItemModel, *, out RecyclerView.ViewHolder>
        ): MultipleViewTypesBuilder<ItemModel> {
            return add(viewType, viewHolderRenderer, Clickers.none())
        }

        fun <E, VH : RecyclerView.ViewHolder> add(
            viewType: Int,
            viewHolderRenderer: BaseRenderer<ItemModel, E, VH>,
            itemClicker: Clicker<E, VH>?
        ): MultipleViewTypesBuilder<ItemModel> {
            renderers.put(viewType, viewHolderRenderer)
            clickers.put(viewType, itemClicker)
            return this
        }

        fun build(): BaseRenderAdapter<ItemModel> {
            if (renderers.isEmpty) {
                throw IllegalStateException("No one view holder renderer is registered")
            }
            if (clickers.isEmpty) {
                throw IllegalStateException("No one item clicker is registered")
            }

            return BaseRenderAdapter(
                items = items,
                differStrategy = differStrategy,
                itemIdSelector = itemIdSelector,
                viewTypeSelector = viewTypeSelector,
                renderers = renderers,
                clickers = clickers
            ).apply {
                setHasStableIds(hasStableIds)
            }
        }
    }


    private class BaseDiffer<T>(
        private val differStrategy: DifferStrategy<T>,
        val dataSourceObserver: ComposeDataSourceObserver = ComposeDataSourceObserver()
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