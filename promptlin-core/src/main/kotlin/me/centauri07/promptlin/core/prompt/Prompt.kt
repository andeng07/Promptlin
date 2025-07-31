package me.centauri07.promptlin.core.prompt

import me.centauri07.promptlin.core.form.FormSessionScope
import kotlin.reflect.KClass

/**
 * Represents an abstract prompt that collects, parses, and validates user input of type [T].
 *
 * @param T The type of value this prompt expects.
 * @property valueType The Kotlin [KClass] of [T], used for type reflection or parsing.
 * @property id A unique identifier for this prompt.
 * @property name The display name of the prompt.
 * @property description A short description explaining the purpose of the prompt.
 * @param validators A list of [Validator]s that are applied to the parsed input.
 * @param shouldInclude A lambda that determines whether this prompt should be included in a [me.centauri07.promptlin.core.form.FormSession].
 */
abstract class Prompt<T : Any>(
    val valueType: KClass<T>,
    val id: String,
    val name: String,
    val description: String,
    private val validators: List<Validator<T>>,
    private val shouldInclude: FormSessionScope.() -> Boolean
) {
    /**
     * Returns whether this prompt should be shown, based on [shouldInclude].
     */
    fun isIncluded(formSessionScope: FormSessionScope): Boolean = shouldInclude(formSessionScope)

    /**
     * Validates the given [value] using all registered validators.
     *
     * @return A [Result.success] if all validations pass, [Result.failure] or a
     *         containing concatenated error messages if any fail.
     */
    fun validate(formSessionScope: FormSessionScope, value: T): Result<T> {
        val errors = validators.mapNotNull { validator ->
            value.let {
                if (!validator.predicate(formSessionScope, value)) validator.failureMessage else null
            }
        }

        return if (errors.isEmpty()) Result.success(value)
        else Result.failure(IllegalArgumentException(errors.joinToString("; ")))
    }

    /**
     * Parses the raw string [input] into a value of type [T].
     *
     * @return A [Result.success] with the parsed value, or [Result.failure] on parsing error.
     */
    abstract fun parse(input: String): Result<T>

    /**
     * Represents a validation rule applied to input values of type [T].
     *
     * @property predicate The condition to test against the input.
     * @property failureMessage The error message shown if the predicate fails.
     */
    data class Validator<T>(
        val predicate: FormSessionScope.(T) -> Boolean,
        val failureMessage: String
    )
}
