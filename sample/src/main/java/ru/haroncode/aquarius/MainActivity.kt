package ru.haroncode.aquarius

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.haroncode.aquarius.core.RenderAdapter
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

class MainActivity : AppCompatActivity() {

    private val itemAdapter by lazy {
        RenderAdapter.Builder<Item>()
            .renderer(Item.Title::class, TitleRenderer())
            .renderer(Item.SimpleTextItem::class, SimpleTextRenderer())
            .renderer(Item.CarouselItem::class, CarouselRenderer())
            .renderer(Item.Button::class, ButtonRenderer(), DefaultClicker(::onClickButton))
            .renderer(Item.CardItem::class, CardRenderer())
//            .buildAsync(ComparableDiffUtilItemCallback())
            .build(DifferStrategies.withDiffUtilComparable())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spaceDecoration = SpaceRuleItemDecoration.Builder<Item>()
            .addRule {
                paddingVertical(4.dp)
                paddingHorizontal(16.dp)
            }
            .create()

        val dividerDecoration = DividerRuleItemDecoration.Builder<Item>(this)
            .addRule {
                gravity(Gravity.BOTTOM)
                with {
                    viewType(Item.SimpleTextItem::class)
                }
            }
            .create()

        with(recyclerView) {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)

            addItemDecoration(spaceDecoration)
            addItemDecoration(dividerDecoration)
        }

        itemAdapter.differ.submitList(ItemFactory.staticItems(this))
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
        itemAdapter.differ.submitList(ItemFactory.staticItems(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView.adapter = null
    }

}