package me.centauri07.promptlin.core.prompt.choice

/**
 * Represents a selectable choice in a [ChoicePrompt].
 *
 * @property value The internal value used for input parsing and comparison.
 * @property label The user-facing label displayed in the prompt.
 * @property description A short explanation of the option.
 */
open class Option(
    val value: String,
    val label: String,
    val description: String,
)