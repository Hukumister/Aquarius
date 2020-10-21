package ru.haroncode.recycler.kit.core.observer

class ComposeDataSourceObserver(
    private val observers: MutableList<DataSourceObserver> = mutableListOf()
) : DataSourceObserver {

    fun registerObserver(observer: DataSourceObserver) {
        observers.add(observer)
    }

    fun unregisterObserver(observer: DataSourceObserver) {
        observers.remove(observer)
    }

    override fun onChanged() {
        observers.forEach(DataSourceObserver::onChanged)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        observers.forEach { observer -> observer.onItemRangeChanged(positionStart, itemCount, payload) }
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        observers.forEach { observer -> observer.onItemRangeInserted(positionStart, itemCount) }
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        observers.forEach { observer -> observer.onItemRangeRemoved(positionStart, itemCount) }
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        observers.forEach { observer -> observer.onItemMoved(fromPosition, toPosition) }
    }

}