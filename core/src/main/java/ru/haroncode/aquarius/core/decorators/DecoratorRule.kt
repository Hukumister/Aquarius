package ru.haroncode.aquarius.core.decorators

import androidx.recyclerview.widget.RecyclerView

interface DecoratorRule {
    /**
     * Можем ли мы применить это правило для элемента по данному номеру
     */
    fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean

    /**
     * Вес показывает насколько правильно приоритетнее. Обычно такая логика, если правило контейнер то вес
     * равет суме весов правил внутри.
     */
    fun weight(): Int
}
