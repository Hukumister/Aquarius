package ru.haroncode.aquarius.core.decorators

import androidx.recyclerview.widget.RecyclerView
import ru.haroncode.aquarius.core.RenderAdapter
import kotlin.reflect.KClass

data class AndRule(val rules: List<DecorationRule>) : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean =
        rules.all { rule -> rule.resolve(adapterPosition, parent) }

    override fun weight(): Int = rules.sumBy(DecorationRule::weight)

}

data class OrRule(val rules: List<DecorationRule>) : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean =
        rules.any { rule -> rule.resolve(adapterPosition, parent) }

    override fun weight(): Int = rules.sumBy(DecorationRule::weight)

}

data class PositionRule(val positions: Set<Int>) : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean = adapterPosition in positions

    override fun weight(): Int = 1
}

data class KlassViewTypeRule(val viewTypes: Set<KClass<*>>) : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean {
        val baseRenderAdapter = parent.adapter as? RenderAdapter<*> ?: return false
        if (adapterPosition == RecyclerView.NO_POSITION) return false
        val item = baseRenderAdapter.differ.currentList[adapterPosition]
        return item::class in viewTypes
    }

    override fun weight(): Int = 1
}

data class ViewTypeRule(val viewTypes: Set<Int>) : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean =
        adapterPosition != RecyclerView.NO_POSITION && parent.adapter?.getItemViewType(adapterPosition) in viewTypes

    override fun weight(): Int = 1

}

data class NextRule(val rule: DecorationRule) : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean {
        val nextAdapterPosition = adapterPosition + 1
        return nextAdapterPosition < parent.adapter?.itemCount ?: 0 && rule.resolve(nextAdapterPosition, parent)
    }

    override fun weight(): Int = 1

}

data class PrevRule(val rule: DecorationRule) : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean {
        val previousAdapterPosition = adapterPosition - 1
        return previousAdapterPosition > 0 && rule.resolve(previousAdapterPosition, parent)
    }

    override fun weight(): Int = 1

}

data class NotRule(val rule: DecorationRule) : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean = !rule.resolve(adapterPosition, parent)

    override fun weight(): Int = 0
}

class LastRule : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean =
        adapterPosition == parent.adapter?.itemCount?.let { itemCount -> itemCount - 1 }

    override fun weight(): Int = 1
}

class AnyRule : DecorationRule {

    override fun resolve(adapterPosition: Int, parent: RecyclerView): Boolean = true

    override fun weight(): Int = 0
}