package ru.haroncode.aquarius.core.renderer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.renderer.ItemBaseRenderer.BaseViewHolder

abstract class ItemBaseRenderer<ItemModel, RC> : BaseRenderer<ItemModel, RC, BaseViewHolder>() {

    @get:LayoutRes abstract val layoutRes: Int

    final override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(layoutRes, parent))
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}