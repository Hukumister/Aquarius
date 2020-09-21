package ru.haroncode.recycler.kit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.haroncode.recycler.kit.core.BaseRenderAdapter
import ru.haroncode.recycler.kit.core.SealedClassViewTypeSelector
import ru.haroncode.recycler.kit.renderers.SimpleTextRenderer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewTypeSelector = SealedClassViewTypeSelector.of<Item>()
        BaseRenderAdapter.Builder<Item>()
            .singleViewType(SimpleTextRenderer())
            .build()
            .differ
            .submitList()
    }
}