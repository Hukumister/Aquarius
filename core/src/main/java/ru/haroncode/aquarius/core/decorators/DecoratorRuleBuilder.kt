package ru.haroncode.aquarius.core.decorators

import kotlin.reflect.KClass

class DecoratorRuleBuilder<T : Any> {

    private val rules: MutableList<DecoratorRule> = mutableListOf()

    fun viewType(vararg classes: KClass<out T>): Boolean = rules.add(ClassViewTypeRule(classes.toSet()))

    fun viewType(vararg viewTypes: Int) = rules.add(ViewTypeRule(viewTypes.toSet()))

    fun position(vararg positions: Int) = rules.add(PositionRule(positions.toSet()))

    fun first() = position(0)

    fun last() = rules.add(LastRule())

    fun not(inverted: DecoratorRuleBuilder<T>.() -> Unit): Boolean {
        val rule = DecoratorRuleBuilder<T>()
            .apply(inverted)
            .create()
        val invertedRule = NotRule(rule)
        return rules.add(invertedRule)
    }

    fun next(nextRule: DecoratorRuleBuilder<T>.() -> Unit): Boolean {
        val rule = DecoratorRuleBuilder<T>()
            .apply(nextRule)
            .create()
        val appliedForNextRule = NextRule(rule)
        return rules.add(appliedForNextRule)
    }

    fun prev(prevRule: DecoratorRuleBuilder<T>.() -> Unit): Boolean {
        val rule = DecoratorRuleBuilder<T>()
            .apply(prevRule)
            .create()
        val appliedForPrevRule = PrevRule(rule)
        return rules.add(appliedForPrevRule)
    }

    fun any(vararg builders: DecoratorRuleBuilder<T>.() -> Unit): Boolean {
        val rulesList = builders.map { builder ->
            DecoratorRuleBuilder<T>()
                .apply(builder)
                .create()
        }
        val oneOfRule = OrRule(rulesList)
        return rules.add(oneOfRule)
    }

    fun any() = rules.add(AnyRule())

    fun create(): DecoratorRule = AndRule(rules)
}