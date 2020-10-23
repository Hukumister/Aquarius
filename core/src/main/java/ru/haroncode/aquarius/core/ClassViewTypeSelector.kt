package ru.haroncode.aquarius.core

import kotlin.reflect.KClass

internal class ClassViewTypeSelector<T : Any> : (T) -> Int {

    private val cachedTypes: MutableMap<KClass<out T>, Int> = mutableMapOf()

    override fun invoke(item: T): Int {
        return cachedTypes.getOrPut(item::class) { item::class.hashCode() }
    }
}
