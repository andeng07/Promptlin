package me.centauri07.promptlin.core.prompt.choice

import me.centauri07.promptlin.core.form.FormSessionScope
import me.centauri07.promptlin.core.prompt.Prompt
import kotlin.reflect.KClass

/**
 * A [Prompt] that allows the user to select a [Option] from a predefined list of options.
 *
 * @param O The type of [Option] this prompt will handle.
 * @param valueType The [KClass] of the [Option] type [O], used for type resolution and reflection.
 * @property id A unique identifier for this prompt.
 * @property name The display name of the prompt.
 * @property description A short description explaining the purpose of the prompt.
 * @param validators A mutable list of [Validator]s that are applied to the parsed input.
 * @param shouldInclude A lambda that determines whether this prompt should be included in a [me.centauri07.promptlin.core.form.FormSession].
 * @param options The list of available [Option] options the user can select from.
 */
class ChoicePrompt<O : Option>(
    valueType: KClass<O>,
    id: String,
    name: String,
    description: String,
    validators: MutableList<Validator<O>>,
    shouldInclude: FormSessionScope.() -> Boolean,
    private val options: List<ChoiceOption<O>>
) : Prompt<O>(valueType, id, name, description, validators, shouldInclude) {

    /**
     * Parses the given [input] string and returns the corresponding [Option] if it exists.
     *
     * @param input The raw user input representing a choice's [Option.value].
     * @return [Result.success] with the matching [Option], or [Result.failure] if no match is found.
     */
    override fun parse(input: String): Result<O> {
        val choiceOption = options.firstOrNull { it.option.value == input }
            ?: return Result.failure<O>(IllegalArgumentException("Invalid option: '$input'"))

        return Result.success(choiceOption.option)
    }

    /**
     * Returns all [Option]s that are available for the given [FormSessionScope].
     *
     * Only options whose [me.centauri07.promptlin.core.prompt.choice.ChoicePrompt.shouldInclude] returns `true` are included.
     */
    fun getOptions(scope: FormSessionScope): List<O> = options.filter { it.shouldInclude(scope) }.map { it.option }

}