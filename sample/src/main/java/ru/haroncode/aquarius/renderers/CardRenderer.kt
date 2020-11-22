package ru.haroncode.aquarius.renderers

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.clicker.ClickableRenderer
import ru.haroncode.aquarius.core.motion.DraggableRenderer
import ru.haroncode.aquarius.core.motion.SwipableRenderer
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer
import ru.haroncode.aquarius.renderers.CardRenderer.RenderContract

class CardRenderer<Item> : ItemBaseRenderer<Item, RenderContract>(),
    ClickableRenderer,
    SwipableRenderer,
    DraggableRenderer {

    interface RenderContract {
        val title: CharSequence
        val subtitle: CharSequence
    }

    override val layoutRes: Int
        get() = R.layout.item_card

    override fun onBindView(viewHolder: BaseViewHolder, item: RenderContract) {
        with(viewHolder) {
            itemView.title.text = item.title
            itemView.subtitle.text = item.subtitle
        }
    }


    override fun swipeDir(viewHolder: RecyclerView.ViewHolder): Int = ItemTouchHelper.END or ItemTouchHelper.START

    override fun onSwiping(viewHolder: RecyclerView.ViewHolder, dX: Float, currentlyActive: Boolean) {
        val width = viewHolder.itemView.width.toFloat()
        val alpha = 1.0f - Math.abs(dX) / width
        viewHolder.itemView.alpha = alpha
    }

    override fun dragDir(viewHolder: RecyclerView.ViewHolder): Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
}