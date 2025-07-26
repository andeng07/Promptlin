package me.centauri07.promptlin.core.form

import me.centauri07.promptlin.core.prompt.Prompt

/**
 * A utility scope for accessing prompt values during a [FormSession].
 *
 * This scope provides read access to the resolved values of all prompts in a [Form],
 * allowing retrieval by prompt reference or prompt ID.
 *
 * Values are strongly typed based on the associated [Prompt]'s type.
 * Use prompt IDs only when the prompt reference is not directly accessible.
 */
class FormSessionScope(private val formSession: FormSession<*>) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(prompt: Prompt<T>): T = formSession.promptInstances.first { it.prompt.id == prompt.id }.value!! as T

    fun <T> getOrElse(prompt: Prompt<T>, defaultValue: (Prompt<T>) -> T) : T = getOrNull(prompt) ?: defaultValue(prompt)

    fun <T> getOrNull(prompt: Prompt<T>) : T? = runCatching { get(prompt) }.getOrNull()

    @Suppress("UNCHECKED_CAST")
    fun <T> get(promptId: String): T = formSession.promptInstances.first { it.prompt.id == promptId }.value!! as T

    fun <T> getOrElse(promptId: String, defaultValue: (String) -> T) : T = getOrNull(promptId) ?: defaultValue(promptId)

    fun <T> getOrNull(promptId: String) : T? = runCatching { get<T>(promptId) }.getOrNull()
}