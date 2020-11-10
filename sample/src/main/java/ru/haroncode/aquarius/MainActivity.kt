package ru.haroncode.aquarius

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.haroncode.aquarius.core.RenderAdapter
import ru.haroncode.aquarius.core.clicker.DefaultClicker
import ru.haroncode.aquarius.core.diffutil.ComparableDiffUtilItemCallback
import ru.haroncode.aquarius.renderers.ButtonRenderer
import ru.haroncode.aquarius.renderers.CarouselRenderer
import ru.haroncode.aquarius.renderers.SimpleTextRenderer

class MainActivity : AppCompatActivity() {

    private val itemAdapter by lazy {
        RenderAdapter.Builder<Item>()
            .renderer(Item.Header::class, SimpleTextRenderer())
            .renderer(Item.SimpleTextItem::class, SimpleTextRenderer())
            .renderer(Item.CarouselItem::class, CarouselRenderer())
            .renderer(Item.Button::class, ButtonRenderer(), DefaultClicker(::onClickButton))
            .buildAsync(ComparableDiffUtilItemCallback())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(recyclerView) {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        itemAdapter.differ.submitList(ItemFactory.staticItems())
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
        itemAdapter.differ.submitList(ItemFactory.staticItems())
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView.adapter = null
    }

}