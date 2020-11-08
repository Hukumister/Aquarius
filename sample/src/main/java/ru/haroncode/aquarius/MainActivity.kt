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

        itemAdapter.differ.submitList(buildItemList())
    }

    private fun onClickButton(renderContract: ButtonRenderer.RenderContract) {
        refreshAll()
    }

    private fun refreshAll() {
        itemAdapter.differ.submitList(buildItemList())
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView.adapter = null
    }

    private fun buildItemList(): List<Item> {
        val resultList = mutableListOf<Item>()
        resultList += Item.Header
        resultList += Item.Button("Update")
        resultList += listOf(
            Item.SimpleTextItem("title1", "subtitle1"),
            Item.SimpleTextItem("title2", "subtitle2"),
            Item.SimpleTextItem("title3", "subtitle3"),
        )

        return resultList
    }
}