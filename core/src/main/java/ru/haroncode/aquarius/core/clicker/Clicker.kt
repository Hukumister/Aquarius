package ru.haroncode.aquarius.core.clicker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface Clicker<T, VH : RecyclerView.ViewHolder> {

    fun onBindClicker(holder: VH)

    fun invoke(holder: VH, view: View, item: T)

    fun onUnbindClicker(holder: VH)
}
