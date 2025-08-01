package me.centauri07.promptlin.core.prompt.choice

import me.centauri07.promptlin.core.prompt.PromptBuilderBase

/**
 * A builder for creating [ChoicePrompt]s with options of type [O].
 *
 * @param O The type of [Option] this builder will produce in the [ChoicePrompt].
 */
interface ChoicePromptBuilder<O : Option> : PromptBuilderBase<O> {

    /**
     * Adds an [option] to the [ChoicePrompt] being built.
     *
     * @param option The [Option] instance to include in the prompt.
     * @param builder An optional configuration block for customizing the option using [OptionBuilder].
     */
    fun option(option: O, builder: OptionBuilder.() -> Unit = {})
}