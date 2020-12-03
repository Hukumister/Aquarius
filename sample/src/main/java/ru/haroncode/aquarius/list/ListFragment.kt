package ru.haroncode.aquarius.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_list.*
import ru.haroncode.aquarius.Item
import ru.haroncode.aquarius.ItemFactory
import ru.haroncode.aquarius.R
import ru.haroncode.aquarius.core.RenderAdapterBuilder
import ru.haroncode.aquarius.core.base.strategies.DifferStrategies
import ru.haroncode.aquarius.core.clicker.DefaultClicker
import ru.haroncode.aquarius.core.decorators.DividerRuleItemDecoration
import ru.haroncode.aquarius.core.decorators.SpaceRuleItemDecoration
import ru.haroncode.aquarius.core.decorators.view.Gravity
import ru.haroncode.aquarius.renderers.ButtonRenderer
import ru.haroncode.aquarius.renderers.CardRenderer
import ru.haroncode.aquarius.renderers.CarouselRenderer
import ru.haroncode.aquarius.renderers.SimpleTextRenderer
import ru.haroncode.aquarius.renderers.TitleRenderer
import ru.haroncode.aquarius.utils.dp

class ListFragment : Fragment(R.layout.fragment_list) {

    private val itemAdapter by lazy {
        RenderAdapterBuilder<Item>()
            .renderer(Item.Title::class, TitleRenderer())
            .renderer(Item.SimpleTextItem::class, SimpleTextRenderer())
            .renderer(Item.CarouselItem::class, CarouselRenderer())
            .renderer(Item.Button::class, ButtonRenderer(), DefaultClicker(::onClickButton))
            .renderer(Item.CardItem::class, CardRenderer())
//            .buildAsync(ComparableDiffUtilItemCallback())
            .build(DifferStrategies.withDiffUtilComparable())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spaceDecoration = SpaceRuleItemDecoration.Builder<Item>()
            .addRule {
                paddingVertical(4.dp)
                paddingHorizontal(16.dp)
            }
            .create()

        val dividerDecoration = DividerRuleItemDecoration.Builder<Item>(requireContext())
            .addRule {
                gravity(Gravity.BOTTOM)
                with {
                    viewType(Item.SimpleTextItem::class)
                }
            }
            .create()

        with(recyclerView) {
            adapter = itemAdapter
            setHasFixedSize(true)

            addItemDecoration(spaceDecoration)
            addItemDecoration(dividerDecoration)
        }

        itemAdapter.differ.submitList(ItemFactory.staticItems(requireContext()))
    }

    private fun onClickButton(renderContract: ButtonRenderer.RenderContract) {
        if (renderContract.id == R.id.button_load_more) {
            loadMore()
        } else {
            refreshAll()
        }
    }

    private fun loadMore() {
        itemAdapter.differ.submitList(itemAdapter.differ.currentList + ItemFactory.loadMore())
    }

    private fun refreshAll() {
        itemAdapter.differ.submitList(ItemFactory.staticItems(requireContext()))
    }
}