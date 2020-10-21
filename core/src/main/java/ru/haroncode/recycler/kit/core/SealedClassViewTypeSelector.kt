package ru.haroncode.recycler.kit.core

import kotlin.reflect.KClass

class SealedClassViewTypeSelector<ItemModel : Any>(
    private val subclasses: List<KClass<out ItemModel>>
) : (ItemModel) -> Int {

    private val cachedTypes: MutableMap<KClass<out ItemModel>, Int> = mutableMapOf()

    companion object {

        inline fun <reified ItemModel : Any> of(): SealedClassViewTypeSelector<ItemModel> =
            SealedClassViewTypeSelector(ItemModel::class.sealedSubclasses)
    }

    override fun invoke(itemModel: ItemModel): Int = viewTypeFor(itemModel::class)

    private fun viewTypeFor(
        klass: KClass<out ItemModel>
    ): Int = cachedTypes.getOrPut(klass) { subclasses.indexOfFirst { value -> klass == value } }
}
