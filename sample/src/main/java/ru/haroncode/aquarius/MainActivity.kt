package ru.haroncode.aquarius

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.haroncode.aquarius.core.RenderAdapter
import ru.haroncode.aquarius.core.base.strategies.DifferStrategies
import ru.haroncode.aquarius.renderers.SimpleTextRenderer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buildSimple = RenderAdapter.Builder<Item>()
            .renderer(Item.SimpleTextItem::class, SimpleTextRenderer())
            .build(DifferStrategies.withDiffUtilComparable())
    }
}