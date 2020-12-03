package ru.haroncode.aquarius.pagination

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_pagination.*
import ru.haroncode.aquarius.list.Item
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.RenderAdapterBuilder
import ru.haroncode.aquarius.core.decorators.SpaceRuleItemDecoration
import ru.haroncode.aquarius.renderers.ErrorRenderer
import ru.haroncode.aquarius.renderers.MaterialListRenderer
import ru.haroncode.aquarius.renderers.StaticLayoutRenderer
import ru.haroncode.aquarius.utils.dp

class PaginationFragment : Fragment(R.layout.fragment_pagination) {

    private val itemAdapter by lazy {
        RenderAdapterBuilder<PaginationItem>()
            .renderer(PaginationItem.SimpleItemWithImage::class, MaterialListRenderer())
            .renderer(PaginationItem.ErrorItem::class, ErrorRenderer())
            .renderer(PaginationItem.LoadingItem::class, StaticLayoutRenderer(R.layout.item_loading))
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spaceDecoration = SpaceRuleItemDecoration.Builder<Item>()
            .addRule {
                paddingVertical(4.dp)
                paddingHorizontal(16.dp)
            }
            .create()
        
        var pageCount = 0
        with(recyclerView) {
            setHasFixedSize(true)
            adapter = itemAdapter

            itemAnimator = null
            addItemDecoration(spaceDecoration)

            addOnScrollListener(PaginationScrollListener {
                postDelayed({
                    if (itemAdapter.differ.currentList.last() is PaginationItem.LoadingItem) {
                        itemAdapter.differ.removeAt(itemAdapter.differ.currentList.lastIndex)
                    }
                    if (pageCount < 4) {
                        val newItems = PaginationItemFactory.startItems(pageCount + 1)
                        itemAdapter.differ.addAll(newItems)
                    } else {

                        return@postDelayed
                    }

                    pageCount++
                }, 3000)
            })
        }

        swipeToRefresh.setOnRefreshListener {
            pageCount = 0

            itemAdapter.differ.submitList(PaginationItemFactory.startItems())
            swipeToRefresh.postDelayed({
                swipeToRefresh.isRefreshing = false
            }, 200)
        }

        itemAdapter.differ.submitList(PaginationItemFactory.startItems())
    }

}