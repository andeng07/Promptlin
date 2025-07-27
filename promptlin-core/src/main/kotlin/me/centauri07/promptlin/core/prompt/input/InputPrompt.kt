package me.centauri07.promptlin.core.prompt.input

import me.centauri07.promptlin.core.form.FormSessionScope
import me.centauri07.promptlin.core.prompt.Prompt
import kotlin.reflect.KClass

/**
 * A [Prompt] that uses a [InputHandler] to parse user input from a string into type [T].
 *
 * @param T The type of value expected from the user.
 * @property handler The handler responsible for parsing and validating input.
 * @param name The name of the prompt.
 * @param description A description to help users understand what the prompt is asking for.
 */
class InputPrompt<T : Any>(
    private val handler: InputHandler<T>,
    valueType: KClass<T>,
    id: String,
    name: String,
    description: String,
    validators: MutableList<Validator<T>>,
    shouldInclude: FormSessionScope.() -> Boolean
) : Prompt<T>(valueType, id, name, description, validators, shouldInclude) {

    /**
     * Parses the provided [input] using the configured [InputHandler].
     *
     * @param input The raw input string to parse.
     * @return A [Result] containing the parsed value or an error.
     */
    override fun parse(input: String): Result<T> = handler.handle(input)
}