package me.centauri07.promptlin.core.prompt

import me.centauri07.promptlin.core.BuilderDsl
import me.centauri07.promptlin.core.form.FormSessionScope

/**
 * DSL builder for configuring prompt validation and conditional inclusion.
 *
 * This builder is used to define validation rules and dynamic visibility logic
 * for a specific prompt within a [me.centauri07.promptlin.core.form.FormSession].
 *
 * Example usage:
 * ```
 * prompt<String> {
 *     validate("Must not be blank") { it.isNotBlank() }
 *     includeIf { currentUser.isAdmin }
 * }
 * ```
 *
 * @param T the type of input this prompt expects (e.g., `String`, `Int`, etc.)
 *
 * @see me.centauri07.promptlin.core.form.FormSession
 * @see Prompt
 */
@BuilderDsl
interface PromptBuilderBase<T> {
    /**
     * Registers a validation rule for the current prompt.
     *
     * @param message The error message to display when the validation fails.
     * @param predicate A predicate that checks whether the input is valid.
     * This lambda runs in the context of [FormSessionScope], giving you access to
     * other prompt values within the same session.
     *
     * The input being validated is passed as the parameter to the lambda.
     *
     * You can retrieve other prompt values using either:
     * - a [Prompt] reference
     * - a prompt ID string
     *
     * Example with prompt reference:
     * ```
     * validate("Passwords do not match") { input ->
     *     get(confirmPasswordPrompt) == input
     * }
     * ```
     *
     * Example with prompt ID:
     * ```
     * validate("Passwords do not match") { input ->
     *     get<String>("confirmPassword") == input
     * }
     * ```
     *
     * In both examples, `this` refers to the [FormSessionScope], and `input` is the current
     * prompt's answer being validated.
     */
    fun validate(message: String, predicate: FormSessionScope.(T) -> Boolean)

    /**
     * Conditionally includes this prompt in the form based on session state.
     *
     * @param predicate A condition evaluated in the context of [FormSessionScope].
     * If the predicate returns `false`, this prompt will be excluded from the form session.
     *
     * You can access other prompt answers using either:
     * - a [Prompt] reference
     * - a prompt ID string
     *
     * Example with prompt reference:
     * ```
     * includeIf {
     *     get(shouldShowDetailsPrompt)
     * }
     * ```
     *
     * Example with prompt ID:
     * ```
     * includeIf {
     *     get<Boolean>("shouldShowDetails")
     * }
     * ```
     *
     * This is useful for dynamic forms where certain questions are only shown
     * based on previous input.
     */
    fun includeIf(predicate: FormSessionScope.() -> Boolean)
}