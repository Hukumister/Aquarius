package ru.haroncode.aquarius.core

import ru.haroncode.aquarius.core.observer.DataSourceObserver

class BaseNotifier<T>(
    private val differ: Differ<T>,
    private val dataSourceObserver: DataSourceObserver
) : Notifier<T> {

    override fun change(
        position: Int,
        count: Int,
        payload: Any?
    ) = dataSourceObserver.onItemRangeChanged(position, count, payload)

    override fun change(item: T, payload: Any?) {
        val index = differ.currentList.indexOf(item)
        if (index != -1) {
            dataSourceObserver.onItemRangeChanged(index, 1, payload)
        }
    }
}
