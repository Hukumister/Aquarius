package ru.haroncode.aquarius.core.decorators

import kotlin.reflect.KClass

class DecorationRuleBuilder<T : Any> {

    private val rules: MutableList<DecorationRule> = mutableListOf()

    fun viewType(vararg classes: KClass<out T>): Boolean = rules.add(KlassViewTypeRule(classes.toSet()))

    fun viewType(vararg viewTypes: Int) = rules.add(ViewTypeRule(viewTypes.toSet()))

    fun position(vararg positions: Int) = rules.add(PositionRule(positions.toSet()))

    fun first() = position(0)

    fun last() = rules.add(LastRule())

    fun not(inverted: DecorationRuleBuilder<T>.() -> Unit): Boolean {
        val rule = DecorationRuleBuilder<T>()
            .apply(inverted)
            .create()
        val invertedRule = NotRule(rule)
        return rules.add(invertedRule)
    }

    fun next(nextRule: DecorationRuleBuilder<T>.() -> Unit): Boolean {
        val rule = DecorationRuleBuilder<T>()
            .apply(nextRule)
            .create()
        val appliedForNextRule = NextRule(rule)
        return rules.add(appliedForNextRule)
    }

    fun prev(prevRule: DecorationRuleBuilder<T>.() -> Unit): Boolean {
        val rule = DecorationRuleBuilder<T>()
            .apply(prevRule)
            .create()
        val appliedForPrevRule = PrevRule(rule)
        return rules.add(appliedForPrevRule)
    }

    fun oneOf(builders: DecorationRuleBuilder<T>.() -> Unit): Boolean {
        val rulesList = DecorationRuleBuilder<T>()
            .apply(builders)
            .createInternal(::OrRule)
        return rules.add(rulesList)
    }

    fun all(builders: DecorationRuleBuilder<T>.() -> Unit): Boolean {
        val rulesList = DecorationRuleBuilder<T>()
            .apply(builders)
            .create()
        return rules.add(rulesList)
    }

    fun any() = rules.add(AnyRule())

    fun create() = createInternal(::AndRule)

    private inline fun createInternal(
        block: (List<DecorationRule>) -> DecorationRule
    ): DecorationRule = block(rules)
}