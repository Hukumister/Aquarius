package ru.haroncode.aquarius.renderers

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_carousel.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.RenderAdapterBuilder
import ru.haroncode.aquarius.core.base.strategies.DifferStrategies
import ru.haroncode.aquarius.core.diffutil.ComparableItem
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.CarouselRenderer.RenderContract

class CarouselRenderer<Item> : ItemBaseRenderer<Item, RenderContract>() {

    interface RenderContract {
        val images: List<ImageItem>
    }

    data class ImageItem(
        override val image: String
    ) : ImageRenderer.RenderContract, ComparableItem

    private val itemAdapter by lazy {
        RenderAdapterBuilder<ImageItem>()
            .renderer(ImageItem::class, ImageRenderer())
            .build(DifferStrategies.withDiffUtilComparable())
    }

    override val layoutRes: Int = R.layout.item_carousel

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder {
        val viewHolder = super.onCreateViewHolder(inflater, parent)
        val recyclerView = viewHolder.itemView.recyclerView
        if (recyclerView.adapter == null) {
            recyclerView.adapter = itemAdapter
        }
        return viewHolder
    }

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        itemAdapter.differ.submitList(item.images)
    }
}