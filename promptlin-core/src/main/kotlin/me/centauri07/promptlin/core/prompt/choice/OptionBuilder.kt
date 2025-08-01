package me.centauri07.promptlin.core.prompt.choice

import me.centauri07.promptlin.core.form.FormSessionScope

/**
 * A builder for configuring an individual [Option] in a [ChoicePrompt].
 *
 * Allows setting conditions for when the option should be included.
 */
interface OptionBuilder {

    /**
     * Sets the condition for including this option in the prompt.
     *
     * @param block A lambda with [FormSessionScope] receiver that returns `true`
     * if the option should be included, or `false` otherwise.
     */
    fun includeIf(block: FormSessionScope.() -> Boolean)
}