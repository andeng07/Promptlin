package me.centauri07.promptlin.core.prompt

import me.centauri07.promptlin.core.form.FormSession
import me.centauri07.promptlin.core.form.FormSessionScope

/**
 * Execution scope provided to rendering lifecycle callbacks for a specific [PromptInstance].
 *
 * This scope gives access to methods that interact with the prompt's session lifecycle—
 * such as submitting input via [attemptSet]. It ensures that only the currently active prompt
 * can be answered to prevent race conditions or invalid state updates.
 *
 *
 * Example usage:
 * ```
 * onInvoke { ctx, prompt ->
 *     ctx.sendMessage("Enter something:")
 *     attemptSet(ctx.awaitMessage()) // Safe input submission
 * }
 * ```
 *
 * @property session the parent [FormSession] managing the prompt flow
 * @property sessionPrompt the current prompt instance in focus
 * @property sessionScope A broader [FormSessionScope] for accessing session-level utilities.
 *
 * @see FormSession
 * @see PromptInstance
 */
class PromptInstanceScope(
    private val session: FormSession<*>,
    private val sessionPrompt: PromptInstance<*>,
    val sessionScope: FormSessionScope
) {
    /**
     * Attempts to submit the given [input] to the current prompt.
     *
     * This method ensures that the prompt is still the latest in the session before accepting input.
     * If the session has already advanced, it throws an [IllegalStateException] to avoid race conditions
     * or duplicate submissions.
     *
     * @param input the string input to submit to the prompt
     * @throws IllegalStateException if the current prompt is no longer active in the session
     */
    fun attemptSet(input: String) {
        if (session.latestPrompt != sessionPrompt) throw IllegalStateException("")
        session.set(input)
    }
}