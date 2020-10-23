package ru.haroncode.aquarius

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.haroncode.aquarius.core.BaseRenderAdapter
import ru.haroncode.aquarius.renderers.SimpleTextRenderer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buildSimple = BaseRenderAdapter.Builder<Item>()
            .renderer(Item.SimpleTextItem::class, SimpleTextRenderer())
            .build()
    }
}