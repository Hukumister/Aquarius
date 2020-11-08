package ru.haroncode.aquarius.renderers

import kotlinx.android.synthetic.main.item_carousel.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.RenderAdapter
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
        RenderAdapter.Builder<ImageItem>()
            .renderer(ImageItem::class, ImageRenderer())
            .build(DifferStrategies.withDiffUtilComparable())
    }

    override val layoutRes: Int = R.layout.item_carousel

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        val recyclerView = viewHolder.itemView.recyclerView
        if (recyclerView.adapter == null) {
            recyclerView.adapter = itemAdapter
        }
        itemAdapter.differ.submitList(item.images)
    }
}