package ru.haroncode.aquarius.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.async.AsyncRenderAdapter
import ru.haroncode.aquarius.core.base.BaseRenderAdapter
import ru.haroncode.aquarius.core.base.strategies.DifferStrategies
import ru.haroncode.aquarius.core.base.strategies.DifferStrategy
import ru.haroncode.aquarius.core.clicker.ClickableRenderer
import ru.haroncode.aquarius.core.clicker.Clicker
import ru.haroncode.aquarius.core.clicker.Clickers
import ru.haroncode.aquarius.core.renderer.BaseRenderer
import kotlin.reflect.KClass

abstract class RenderAdapter<T : Any>(
    private val itemIdSelector: (T) -> Long,
    private val viewTypeSelector: ViewTypeSelector<T>,
    private val clickers: SparseArrayCompat<Clicker<*, out RecyclerView.ViewHolder>>,
    private val renderers: SparseArrayCompat<BaseRenderer<out T, *, out RecyclerView.ViewHolder>>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract val differ: Differ<T>

    override fun getItemId(position: Int): Long {
        val itemModel = differ.currentList[position]
        return itemIdSelector(itemModel)
    }

    override fun getItemViewType(position: Int): Int {
        val itemModel = differ.currentList[position]
        return viewTypeSelector.viewTypeFor(itemModel)
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

    private fun <VH : RecyclerView.ViewHolder> findRenderer(itemViewType: Int): BaseRenderer<T, Any, VH> {
        @Suppress("UNCHECKED_CAST")
        val renderer = renderers.get(itemViewType) as? BaseRenderer<T, Any, VH>
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

    class Builder<T : Any> {

        private var idSelector: (T) -> Long = { RecyclerView.NO_ID }
        private val renderers = SparseArrayCompat<BaseRenderer<out T, *, *>>()
        private val clickers = SparseArrayCompat<Clicker<*, *>>()
        private var classViewTypeSelector: ViewTypeSelector<KClass<out T>> = ClassViewTypeSelector()

        fun withIdSelector(selector: (T) -> Long): Builder<T> {
            idSelector = selector
            return this
        }

        fun withViewTypeSelector(selector: ViewTypeSelector<KClass<out T>>): Builder<T> {
            classViewTypeSelector = selector
            return this
        }

        fun <E, VH : RecyclerView.ViewHolder> renderer(
            viewType: Int,
            renderer: BaseRenderer<out T, E, VH>,
            itemClicker: Clicker<E, VH> = Clickers.none()
        ): Builder<T> {
            check(!renderers.containsKey(viewType)) { "Builder already contains view holder renderer for view type = $viewType" }
            check(!clickers.containsKey(viewType)) { "Builder already contains view holder clicker for view type = $viewType" }

            renderers.put(viewType, renderer)
            clickers.put(viewType, itemClicker)
            return this
        }

        fun <E, VH : RecyclerView.ViewHolder> renderer(
            kClass: KClass<out T>,
            renderer: BaseRenderer<out T, E, VH>,
            itemClicker: Clicker<E, VH> = Clickers.none()
        ): Builder<T> {
            val viewType = classViewTypeSelector.createViewTypeFor(kClass)

            check(!renderers.containsKey(viewType)) { "Builder already contains view holder renderer for view type = ${kClass.simpleName}" }
            check(!clickers.containsKey(viewType)) { "Builder already contains view holder clicker for view type = ${kClass.simpleName}" }

            renderers.put(viewType, renderer)
            clickers.put(viewType, itemClicker)
            return this
        }

        fun build(
            differStrategy: DifferStrategy<T> = DifferStrategies.none()
        ): RenderAdapter<T> = BaseRenderAdapter(
            differStrategy = differStrategy,
            clickers = clickers,
            itemIdSelector = idSelector,
            viewTypeSelector = classViewTypeSelector,
            renderers = renderers
        )

        fun buildAsync(
            itemCallback: DiffUtil.ItemCallback<T>
        ): RenderAdapter<T> = AsyncRenderAdapter(
            itemCallback = itemCallback,
            clickers = clickers,
            itemIdSelector = idSelector,
            viewTypeSelector = classViewTypeSelector,
            renderers = renderers
        )
    }
}