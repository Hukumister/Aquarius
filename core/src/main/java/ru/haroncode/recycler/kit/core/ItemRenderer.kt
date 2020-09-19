package ru.haroncode.recycler.kit.core

import android.view.LayoutInflater
import android.view.ViewGroup

abstract class ItemRenderer<ItemModel, RC> : BaseRenderer<ItemModel, RC, BaseViewHolder>() {

    abstract val layoutRes: Int

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(layoutRes, parent))
    }
}