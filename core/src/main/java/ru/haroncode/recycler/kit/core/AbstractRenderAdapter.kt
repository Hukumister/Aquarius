package ru.haroncode.recycler.kit.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.base.strategies.DifferStrategy
import ru.haroncode.recycler.kit.core.clicker.ClickableRenderer
import ru.haroncode.recycler.kit.core.clicker.Clicker
import ru.haroncode.recycler.kit.core.clicker.Clickers
import kotlin.reflect.KClass

abstract class AbstractRenderAdapter<ItemModel : Any>(
    val differ: Differ<ItemModel>,
    private val itemIdSelector: (ItemModel) -> Long,
    private val viewTypeSelector: (ItemModel) -> Int,
    private val clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    private val renderers: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    class Builder<ItemModel : Any> {

        private var itemIdSelector: (ItemModel) -> Long = { RecyclerView.NO_ID }
        private var hasStableIds: Boolean = false

        fun withItemIdSelector(itemIdSelector: (ItemModel) -> Long, hasStableIds: Boolean): Builder<ItemModel> {
            this.itemIdSelector = itemIdSelector
            this.hasStableIds = hasStableIds
            return this
        }

        fun <E, VH : RecyclerView.ViewHolder> singleViewType(
            baseRenderer: BaseRenderer<out ItemModel, E, VH>,
            clicker: Clicker<E, VH> = Clickers.none()
        ): SingleViewTypeBuilder<ItemModel> = SingleViewTypeBuilder(itemIdSelector, hasStableIds, baseRenderer, clicker)

        fun withViewTypeSelector(viewTypeSelector: (ItemModel) -> Int): MultipleViewTypesBuilder<ItemModel> =
            MultipleViewTypesBuilder(itemIdSelector, hasStableIds, viewTypeSelector)

    }

    class SingleViewTypeBuilder<ItemModel : Any>(
        private val itemIdSelector: (ItemModel) -> Long,
        private val hasStableIds: Boolean,
        private val renderer: BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>,
        private val clicker: Clicker<*, out RecyclerView.ViewHolder>
    ) {

        companion object {
            private const val DEFAULT_VIEW_TYPE = 0
        }


        fun buildAsync(itemCallback: DiffUtil.ItemCallback<ItemModel>) {
            TODO()
        }

        fun build(differStrategy: DifferStrategy<ItemModel>): AbstractRenderAdapter<ItemModel> {
            TODO()
//            val renderers = SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>(1)
//            renderers.put(DEFAULT_VIEW_TYPE, renderer)
//
//            val itemClickers = SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>(1)
//            itemClickers.put(DEFAULT_VIEW_TYPE, clicker)
//
//            return BaseRenderAdapter(
//                differStrategy = diff,
//                itemIdSelector = itemIdSelector,
//                viewTypeSelector = { DEFAULT_VIEW_TYPE },
//                renderers = renderers,
//                clickers = itemClickers
//            ).apply {
//                setHasStableIds(hasStableIds)
//            }
        }
    }

    class MultipleViewTypesBuilder<ItemModel : Any>(
        private val itemIdSelector: (ItemModel) -> Long,
        private val hasStableIds: Boolean,
        private val viewTypeSelector: (ItemModel) -> Int
    ) {

        private val renderers: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>> =
            SparseArrayCompat(3)
        private val clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>> = SparseArrayCompat(3)

        fun <E, VH : RecyclerView.ViewHolder> add(
            viewType: Int,
            viewHolderRenderer: BaseRenderer<ItemModel, E, VH>,
            itemClicker: Clicker<E, VH> = Clickers.none()
        ): MultipleViewTypesBuilder<ItemModel> {
            check(!renderers.containsKey(viewType)) { "" }
            check(!clickers.containsKey(viewType)) { "" }
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

    class SealedClassesTypesBuilder<ItemModel : Any>(
        private val itemIdSelector: (ItemModel) -> Long,
        private val hasStableIds: Boolean,
        private val classViewTypeSelector: (KClass<out ItemModel>) -> Int
    ) {

        private val viewHolderRendererList: SparseArrayCompat<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>> =
            SparseArrayCompat(3)
        private val itemClickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>> = SparseArrayCompat(3)

        fun <E, VH : RecyclerView.ViewHolder> add(
            kClass: KClass<out ItemModel>,
            viewHolderRenderer: BaseRenderer<out ItemModel, E, VH>,
            itemClicker: Clicker<E, VH> = Clickers.none()
        ): SealedClassesTypesBuilder<ItemModel> {
            val viewType = classViewTypeSelector.invoke(kClass)
            if (viewHolderRendererList.containsKey(viewType)) {
                throw IllegalStateException("Builder already contains view holder renderer for view type = ${kClass.simpleName}")
            }
            if (itemClickers.containsKey(viewType)) {
                throw IllegalStateException("Builder already contains item clicker for view type = ${kClass.simpleName}")
            }
            viewHolderRendererList.put(viewType, viewHolderRenderer)
            itemClickers.put(viewType, itemClicker)
            return this
        }

        fun build(): Adapter<ItemModel, *> {
            if (viewHolderRendererList.isEmpty()) {
                throw IllegalStateException("No one view holder renderer is registered")
            }
            if (itemClickers.isEmpty()) {
                throw IllegalStateException("No one item clicker is registered")
            }

            return RenderAdapter(
                items = items,
                diff = diff,
                itemIdSelector = itemIdSelector,
                viewTypeSelector = { item -> classViewTypeSelector.invoke(item::class) },
                viewHolderRendererList = viewHolderRendererList,
                itemClickers = itemClickers
            ).apply {
                setHasStableIds(hasStableIds)
            }
        }
    }
}