package ru.haroncode.recycler.kit.core

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.datasource.AdapterDataSourceObserver
import ru.haroncode.recycler.kit.core.differ.BaseDiffer
import ru.haroncode.recycler.kit.core.differ.DifferStrategy

open class BaseRenderAdapter<ItemModel : Any>(
    items: List<ItemModel> = emptyList(),
    differStrategy: DifferStrategy<ItemModel>,
    private val itemClickers: SparseArrayCompat<ItemClicker<*, out RecyclerView.ViewHolder>>,
    private val itemIdSelector: (ItemModel) -> Long,
    private val viewTypeSelector: (ItemModel) -> Int,
    private val renderers: SparseArray<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>,
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

    private fun <VH : RecyclerView.ViewHolder> findItemClicker(itemViewType: Int): ItemClicker<Any, VH> {
        @Suppress("UNCHECKED_CAST")
        val itemClicker = itemClickers.get(itemViewType) as? ItemClicker<Any, VH>
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
        if (adapterDelegate is ClickableRenderer && holder is BaseViewHolder) {
            unbindClicker(adapterDelegate, holder)
        }
    }

    private fun <VH : RecyclerView.ViewHolder> unbindClicker(renderer: ClickableRenderer, viewHolder: VH) {
        val itemClicker = findItemClicker<VH>(viewHolder.itemViewType)
        renderer.unbindClickListener(viewHolder)
        itemClicker.onUnbindClicker(viewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val renderer = findRenderer<BaseViewHolder>(viewType)
        val inflater = LayoutInflater.from(parent.context)
        return renderer.onCreateViewHolder(inflater, parent)
    }

    override fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        val itemViewType = viewHolder.itemViewType
        val renderer = findRenderer<BaseViewHolder>(itemViewType)
        renderer.onRecycleViewHolder(viewHolder)
    }

    class Builder<ItemModel : Any> private constructor(
        private val items: List<ItemModel>,
        private val diff: DifferStrategy<ItemModel>,
        private var itemIdSelector: (ItemModel) -> Long,
        private var hasStableIds: Boolean
    ) {

        fun withItemIdSelector(
            itemIdSelector: (ItemModel) -> Long,
            hasStableIds: Boolean
        ): Builder<ItemModel> {
            this.itemIdSelector = itemIdSelector
            this.hasStableIds = hasStableIds
            return this
        }

        fun singleViewType(
            viewHolderRenderer: BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>
        ): SingleViewTypeBuilder<ItemModel> = singleViewType(viewHolderRenderer, ItemClicker.NoneClicker())

        fun <E, VH : RecyclerView.ViewHolder> singleViewType(
            baseRenderer: BaseRenderer<out ItemModel, E, VH>,
            itemClicker: ItemClicker<E, VH>
        ): SingleViewTypeBuilder<ItemModel> =
            SingleViewTypeBuilder(items, diff, itemIdSelector, hasStableIds, baseRenderer, itemClicker)
    }


    class SingleViewTypeBuilder<ItemModel : Any>(
        private val items: List<ItemModel>,
        private val diff: DifferStrategy<ItemModel>,
        private val itemIdSelector: (ItemModel) -> Long,
        private val hasStableIds: Boolean,
        private val viewHolderRenderer: BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>,
        private val itemClicker: ItemClicker<*, out RecyclerView.ViewHolder>
    ) {

        companion object {
            private const val DEFAULT_VIEW_TYPE = 0
        }

        fun build(): BaseRenderAdapter<ItemModel> {
            val viewHolderRenderers = SparseArray<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>(1)
            viewHolderRenderers.put(DEFAULT_VIEW_TYPE, viewHolderRenderer)

            val itemClickers = SparseArrayCompat<ItemClicker<*, out RecyclerView.ViewHolder>>(1)
            itemClickers.put(DEFAULT_VIEW_TYPE, itemClicker)

            return BaseRenderAdapter(
                items = items,
                differStrategy = diff,
                itemIdSelector = itemIdSelector,
                viewTypeSelector = { DEFAULT_VIEW_TYPE },
                renderers = viewHolderRenderers,
                itemClickers = itemClickers
            ).apply {
                setHasStableIds(hasStableIds)
            }
        }
    }
}