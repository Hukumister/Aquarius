package ru.haroncode.aquarius.core

import kotlin.reflect.KClass

internal class ClassViewTypeSelector<T : Any> : ViewTypeSelector<KClass<out T>> {

    private val cachedTypes: MutableMap<KClass<out T>, Int> = mutableMapOf()

    override fun viewTypeFor(item: KClass<out T>): Int {
        return cachedTypes[item] ?: throw IllegalStateException("Cannot find view type for class ${item.simpleName}")
    }

    override fun createViewTypeFor(item: KClass<out T>): Int {
        return cachedTypes.getOrPut(item) { item.hashCode() }
    }
}
