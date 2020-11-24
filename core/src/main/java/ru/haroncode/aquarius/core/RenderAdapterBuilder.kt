package ru.haroncode.aquarius.core

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.async.AsyncRenderAdapter
import ru.haroncode.aquarius.core.base.BaseRenderAdapter
import ru.haroncode.aquarius.core.base.strategies.DifferStrategies
import ru.haroncode.aquarius.core.base.strategies.DifferStrategy
import ru.haroncode.aquarius.core.clicker.Clicker
import ru.haroncode.aquarius.core.clicker.Clickers
import ru.haroncode.aquarius.core.helper.RenderItemTouchHelperCallback
import ru.haroncode.aquarius.core.renderer.BaseRenderer
import kotlin.reflect.KClass

class RenderAdapterBuilder<T : Any> {

    private var touchHelperCallback: RenderItemTouchHelperCallback = RenderItemTouchHelperCallback()
    private var idSelector: (T) -> Long = { RecyclerView.NO_ID }
    private val renderers = SparseArrayCompat<BaseRenderer<out T, *, *>>()
    private val clickers = SparseArrayCompat<Clicker<*, *>>()
    private var classViewTypeSelector: ViewTypeSelector<KClass<out T>> = ClassViewTypeSelector()

    fun withItemCallBack(itemCallback: RenderItemTouchHelperCallback) {
        touchHelperCallback = itemCallback
    }

    fun withIdSelector(selector: (T) -> Long): RenderAdapterBuilder<T> {
        idSelector = selector
        return this
    }

    fun withViewTypeSelector(selector: ViewTypeSelector<KClass<out T>>): RenderAdapterBuilder<T> {
        classViewTypeSelector = selector
        return this
    }

    fun <E, VH : RecyclerView.ViewHolder> renderer(
        viewType: Int,
        renderer: BaseRenderer<out T, E, VH>,
        itemClicker: Clicker<E, VH> = Clickers.none()
    ): RenderAdapterBuilder<T> {
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
    ): RenderAdapterBuilder<T> {
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
        renderers = renderers,
        touchHelperCallback = touchHelperCallback
    )

    fun buildAsync(
        itemCallback: DiffUtil.ItemCallback<T>
    ): RenderAdapter<T> = AsyncRenderAdapter(
        itemCallback = itemCallback,
        clickers = clickers,
        itemIdSelector = idSelector,
        viewTypeSelector = classViewTypeSelector,
        renderers = renderers,
        touchHelperCallback = touchHelperCallback
    )
}