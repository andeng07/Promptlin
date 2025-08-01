package me.centauri07.promptlin.core.prompt.choice

import me.centauri07.promptlin.core.form.FormSessionScope

class OptionBuilderImpl<O : Option>(
    val option: O
) : OptionBuilder {
    private var shouldInclude: FormSessionScope.() -> Boolean = { true }

    override fun includeIf(block: FormSessionScope.() -> Boolean) {
        shouldInclude = block
    }

    fun build(): ChoiceOption<O> = ChoiceOption(option, shouldInclude)
}