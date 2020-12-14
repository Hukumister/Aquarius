package ru.haroncode.aquarius.core

import ru.haroncode.aquarius.core.observer.DataListUpdateCallback

class BaseNotifier<T>(
    private val differ: Differ<T>,
    private val dataListUpdateCallback: DataListUpdateCallback
) : Notifier<T> {

    override fun change(
        position: Int,
        count: Int,
        payload: Any?
    ) = dataListUpdateCallback.onChanged(position, count, payload)

    override fun change(item: T, payload: Any?) {
        val index = differ.currentList.indexOf(item)
        if (index != -1) {
            dataListUpdateCallback.onChanged(index, 1, payload)
        }
    }
}
