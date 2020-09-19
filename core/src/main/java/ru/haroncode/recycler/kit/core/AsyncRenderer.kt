package ru.haroncode.recycler.kit.core

import android.util.SparseArray
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.recycler.kit.core.differ.DifferStrategy

//todo
class AsyncRenderer<ItemModel : Any>(
    differStrategy: DifferStrategy<ItemModel>,
    itemClickers: SparseArrayCompat<ItemClicker<*, out RecyclerView.ViewHolder>>,
    itemIdSelector: (ItemModel) -> Long,
    viewTypeSelector: (ItemModel) -> Int,
    renderers: SparseArray<BaseRenderer<out ItemModel, *, out RecyclerView.ViewHolder>>
) : BaseRenderAdapter<ItemModel>(
    differStrategy = differStrategy,
    itemClickers = itemClickers,
    itemIdSelector = itemIdSelector,
    viewTypeSelector = viewTypeSelector,
    renderers = renderers
)