package ru.haroncode.recycler.kit.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.clicker.ClickableRenderer
import ru.haroncode.recycler.kit.core.clicker.Clicker
import ru.haroncode.recycler.kit.core.clicker.Clickers
import ru.haroncode.recycler.kit.core.differ.core.BaseDiffer
import ru.haroncode.recycler.kit.core.differ.core.DifferStrategy
import ru.haroncode.recycler.kit.core.differ.strategies.DifferStrategies
import ru.haroncode.recycler.kit.core.observer.AdapterDataSourceObserver

open class BaseRenderAdapter<ItemModel : Any>(
    items: List<ItemModel>,
    differStrategy: DifferStrategy<ItemModel>,
    private val clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    private val itemIdSelector: (ItemModel) -> Long,
    private val viewTypeSelector: (ItemModel) -> Int,
    private val renderers: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    open val differ = BaseDiffer(
        differStrategy = differStrategy,
        dataSourceObserver = AdapterDataSourceObserver(this),
    ).apply { submitList(items) }

    override fun getItemId(position: Int): Long {
        val itemModel = differ.currentList[position]
        return itemIdSelector(itemModel)
    }

    override fun getItemViewType(position: Int): Int {
        val itemModel = differ.currentList[position]
        return viewTypeSelector(itemModel)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        val itemViewType = viewHolder.itemViewType

        val renderer = findRenderer<RecyclerView.ViewHolder>(itemViewType)
        val itemModel = differ.currentList[position]
        renderer.onBindItemModel(viewHolder, itemModel, payloads)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = viewHolder.itemViewType

        val renderer = findRenderer<RecyclerView.ViewHolder>(itemViewType)
        val itemModel = differ.currentList[position]
        renderer.onBindItemModel(viewHolder, itemModel)
    }

    private fun <VH : RecyclerView.ViewHolder> findRenderer(itemViewType: Int): BaseRenderer<ItemModel, Any, VH> {
        @Suppress("UNCHECKED_CAST")
        val renderer = renderers.get(itemViewType) as? BaseRenderer<ItemModel, Any, VH>
        return checkNotNull(renderer) { "Unknown view type=$itemViewType" }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val renderer = findRenderer<RecyclerView.ViewHolder>(holder.itemViewType)
        if (renderer is ClickableRenderer) {
            bindClicker(renderer, holder)
        }
    }

    private fun <VH : RecyclerView.ViewHolder> bindClicker(renderer: ClickableRenderer, viewHolder: VH) {
        val itemClicker = findItemClicker<VH>(viewHolder.itemViewType)
        renderer.bindClickListener(viewHolder, ::onClick)
        itemClicker.onBindClicker(viewHolder)
    }

    private fun <VH : RecyclerView.ViewHolder> findItemClicker(itemViewType: Int): Clicker<Any, VH> {
        @Suppress("UNCHECKED_CAST")
        val itemClicker = clickers.get(itemViewType) as? Clicker<Any, VH>
        return checkNotNull(itemClicker) { "Unknown view type=$itemViewType" }
    }

    private fun <VH : RecyclerView.ViewHolder> onClick(viewHolder: VH, view: View) {
        val itemViewType = viewHolder.itemViewType
        val position = viewHolder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val itemClicker = findItemClicker<VH>(itemViewType)
            val itemModel = differ.currentList[position]
            itemClicker.invoke(viewHolder, view, itemModel)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val adapterDelegate = findRenderer<RecyclerView.ViewHolder>(holder.itemViewType)
        if (adapterDelegate is ClickableRenderer) {
            unbindClicker(adapterDelegate, holder)
        }
    }

    private fun <VH : RecyclerView.ViewHolder> unbindClicker(renderer: ClickableRenderer, viewHolder: VH) {
        val itemClicker = findItemClicker<VH>(viewHolder.itemViewType)
        renderer.unbindClickListener(viewHolder)
        itemClicker.onUnbindClicker(viewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val renderer = findRenderer<RecyclerView.ViewHolder>(viewType)
        val inflater = LayoutInflater.from(parent.context)
        return renderer.onCreateViewHolder(inflater, parent)
    }

    override fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        val itemViewType = viewHolder.itemViewType
        val renderer = findRenderer<RecyclerView.ViewHolder>(itemViewType)
        renderer.onRecycleViewHolder(viewHolder)
    }

    class Builder<ItemModel : Any> private constructor(
        private val initialItems: List<ItemModel>,
        private var differStrategy: DifferStrategy<ItemModel> = DifferStrategies.none(),
        private var itemIdSelector: (ItemModel) -> Long = { RecyclerView.NO_ID },
        private var hasStableIds: Boolean = false
    ) {

        constructor(
            items: List<ItemModel> = emptyList()
        ) : this(initialItems = items)

        fun withItemIdSelector(itemIdSelector: (ItemModel) -> Long, hasStableIds: Boolean): Builder<ItemModel> {
            this.itemIdSelector = itemIdSelector
            this.hasStableIds = hasStableIds
            return this
        }

        fun withDifferStrategy(differStrategy: DifferStrategy<ItemModel>) {
            this.differStrategy = differStrategy
        }

        fun <E, VH : RecyclerView.ViewHolder> singleViewType(
            baseRenderer: BaseRenderer<out ItemModel, E, VH>,
            clicker: Clicker<E, VH> = Clickers.none()
        ): SingleViewTypeBuilder<ItemModel> =
            SingleViewTypeBuilder(initialItems, differStrategy, itemIdSelector, hasStableIds, baseRenderer, clicker)
    }


    class SingleViewTypeBuilder<ItemModel : Any>(
        private val items: List<ItemModel>,
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
                items = items,
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
}