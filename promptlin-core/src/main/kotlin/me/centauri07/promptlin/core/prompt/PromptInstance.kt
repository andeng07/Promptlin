package me.centauri07.promptlin.core.prompt

import me.centauri07.promptlin.core.form.FormSessionScope

/**
 * Wraps a [Prompt] with its associated runtime state during a form session.
 *
 * Each [PromptInstance] tracks whether a prompt has been answered (acknowledged),
 * holds the parsed value if successfully answered, and provides input assignment
 * with validation through [attemptSet].
 *
 * @param T The expected data type of the prompt's answer.
 * @property prompt The [Prompt] definition this instance wraps.
 * @property formSessionScope Provides access to the current session's state for validation or inclusion logic.
 */
class PromptInstance<T : Any>(val prompt: Prompt<T>, private val formSessionScope: FormSessionScope) {
    /**
     * The parsed and validated value provided by the user.
     *
     * Will be `null` until successfully assigned via [attemptSet].
     */
    var value: T? = null
        private set

    /**
     * Indicates whether this prompt has been acknowledged by the user.
     *
     * A prompt is considered acknowledged after a successful input assignment
     * or explicit call to [acknowledge].
     */
    var acknowledged: Boolean = false
        private set

    /**
     * Attempts to parse and validate the given user input, then assigns it as this prompt's value.
     *
     * Parsing is done through [Prompt.parse], and validation via [Prompt.validate],
     * both of which may return [Result.failure] on error.
     *
     * If both steps succeed, the parsed value is stored in [value] and the prompt is marked as acknowledged.
     *
     * @param input The raw user input as a [String].
     * @return A [Result.success] if the value was accepted,
     *         or a [Result.failure] if parsing or validation fails.
     */
    fun attemptSet(input: String): Result<Unit> {
        val parsedResult = prompt.parse(input)
        if (parsedResult.isFailure) return parsedResult.map {}

        val parsed = parsedResult.getOrNull()!!
        val validationResult = prompt.validate(formSessionScope, parsed)
        if (validationResult.isFailure) return validationResult.map {}

        value = parsed
        prompt.complete(parsed)
        acknowledge()
        return Result.success(Unit)
    }

    /**
     * Marks this prompt as acknowledged, even without assigning a value.
     *
     * Typically called after a successful [attemptSet], but can also be used
     * in special cases like confirmations or optional prompts.
     */
    fun acknowledge() {
        acknowledged = true
    }

}