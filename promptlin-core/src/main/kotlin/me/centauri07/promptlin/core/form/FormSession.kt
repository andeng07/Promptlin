package me.centauri07.promptlin.core.form

import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.PromptInstance
import me.centauri07.promptlin.core.renderer.RenderContext
import me.centauri07.promptlin.core.renderer.Renderer

/**
 * Controls the lifecycle of a single form session tied to a specific [RenderContext].
 *
 * The session is generic over a [RenderContext], allowing platform-specific behavior to be injected.
 *
 * @param form The form to be rendered and completed.
 * @param context The platform-specific context passed throughout the session.
 * @param renderer Responsible for rendering prompts and responding to input success/failure.
 * @param onFinish Callback invoked once the form is fully completed.
 */
class FormSession<C : RenderContext>(
    form: Form,
    private val context: C,
    private val renderer: Renderer<C>,
    private val onFinish: FormSessionScope.(C) -> Unit
) {
    /**
     * Scope that provides read access to [PromptInstance] within this session.
     * Used in validation, inclusion logic, and during finalization.
     */
    private val scope: FormSessionScope = FormSessionScope(this)

    /**
     * All [PromptInstance]s associated with this session.
     * These wrap each [me.centauri07.promptlin.core.prompt.Prompt] in the form and track its answered state.
     */
    val promptInstances: List<PromptInstance<*>> = form.prompts.map {
        @Suppress("UNCHECKED_CAST")
        PromptInstance(it as Prompt<Any>, scope)
    }

    /**
     * The next unanswered prompt that should be rendered.
     *
     * Only prompts whose `includeIf` conditions evaluate to true are considered.
     * Returns `null` if the form is complete.
     */
    val latestPrompt: PromptInstance<*>?
        get() = promptInstances.firstOrNull { !it.acknowledged && it.prompt.isIncluded(scope) }

    /**
     * Starts or continues the form session by rendering the latest prompt.
     *
     * If the form is already complete, the [onFinish] callback is triggered instead.
     */
    fun run() {
        val latestPrompt = this.latestPrompt ?: run {
            onFinish(scope, context)
            FormSessionRegistry.unregister(this)
            return
        }

        renderer.invoke(this, scope, latestPrompt, context)
    }

    /**
     * Attempts to assign user input to the current prompt.
     *
     * Handles validation and rerenders on failure.
     * On success, marks the prompt as completed and continues the session.
     *
     * @param assignValue The raw user input (usually a string) to assign.
     * @return [Result.success] if the input was valid and accepted,
     *         [Result.failure] with the thrown exception if validation failed.
     *         Returns failure immediately if the form is already completed.
     */
    fun set(assignValue: String): Result<Unit> {
        val latestPrompt = this.latestPrompt ?: return Result.failure(
            IllegalStateException("Attempted to access a prompt after form completion.")
        )

        val result = latestPrompt.attemptSet(assignValue)

        if (result.isFailure) {
            renderer.failure(this, scope, latestPrompt, context, result.exceptionOrNull()!!)
            run()
            return result
        }

        renderer.complete(this, scope, latestPrompt, context)

        run()
        return Result.success(Unit)
    }

}