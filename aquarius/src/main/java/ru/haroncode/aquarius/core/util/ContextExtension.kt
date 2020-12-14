package ru.haroncode.aquarius.core.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.appcompat.content.res.AppCompatResources

fun Context.resolveDrawableAttr(@AttrRes attr: Int): Drawable? {
    val value = TypedValue()
    theme.resolveAttribute(attr, value, true)
    return if (value.resourceId != 0) AppCompatResources.getDrawable(this, value.resourceId) else null
}
