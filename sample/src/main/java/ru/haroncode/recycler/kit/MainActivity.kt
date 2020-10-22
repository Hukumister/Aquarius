package ru.haroncode.recycler.kit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.haroncode.recycler.kit.core.AbstractRenderAdapter
import ru.haroncode.recycler.kit.core.SealedClassViewTypeSelector
import ru.haroncode.recycler.kit.core.async.AsyncRenderer
import ru.haroncode.recycler.kit.core.base.strategies.diffutil.ComparableDiffUtilItemCallback
import ru.haroncode.recycler.kit.renderers.SimpleTextRenderer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewTypeSelector = SealedClassViewTypeSelector.of<Item>()
        AsyncRenderer.Builder<Item>()
            .singleViewType(SimpleTextRenderer())
            .build()
            .differ
            .submitList()
    }
}