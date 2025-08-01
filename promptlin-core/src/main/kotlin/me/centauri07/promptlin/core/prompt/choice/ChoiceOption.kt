package me.centauri07.promptlin.core.prompt.choice

import me.centauri07.promptlin.core.form.FormSessionScope

/**
 * Represents a selectable option in a choice prompt.
 *
 * @param O The type of the [Option] this choice represents.
 * @property option The [Option] value associated with this choice.
 * @property shouldInclude  determines whether this option should be included in the prompt.
 */
data class ChoiceOption<O : Option>(val option: O, val shouldInclude: FormSessionScope.() -> Boolean)