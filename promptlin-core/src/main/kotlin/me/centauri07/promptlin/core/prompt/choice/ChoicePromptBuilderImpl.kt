package me.centauri07.promptlin.core.prompt.choice

import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.PromptBuilderBaseImpl
import kotlin.reflect.KClass

class ChoicePromptBuilderImpl<O : Option>(
    valueType: KClass<O>,
    id: String,
    name: String,
    description: String
) : PromptBuilderBaseImpl<O>(valueType, id, name, description), ChoicePromptBuilder<O> {
    val options: MutableList<ChoiceOption<O>> = mutableListOf()

    override fun option(
        option: O,
        builder: OptionBuilder.() -> Unit
    ) {
        val builderImpl = OptionBuilderImpl(option)

        builderImpl.builder()

        options += builderImpl.build()
    }

    override fun build(): Prompt<O> {
        return ChoicePrompt(valueType, id, name, description, validators, shouldInclude, onComplete, options)
    }
}