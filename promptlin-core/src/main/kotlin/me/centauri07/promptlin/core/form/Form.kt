package me.centauri07.promptlin.core.form

import me.centauri07.promptlin.core.Promptlin
import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.renderer.RenderContext

/**
 * Represents a form composed of multiple [Prompt]s.
 *
 * A [Form] is a high-level container that defines the structure of a prompt-driven interaction.
 * It can be executed on a specific [RenderContext] via [start], which renders prompts and runs a final callback.
 *
 * @property prompts The list of prompts that define the steps of this form.
 */
class Form(
    val prompts: List<Prompt<*>>
) {
    /**
     * Starts the form rendering and execution on the provided [context].
     *
     * This function resolves the platform-specific [me.centauri07.promptlin.core.platform.PlatformHandler]
     * and initiates a new [FormSession], which manages the state and flow of prompt interactions.
     *
     * @param context The [RenderContext] on which the form will be rendered.
     * @param finish A lambda with a [FormSessionScope] receiver that is executed once all prompts have been completed.
     *               You can access submitted responses using [FormSessionScope.get] or related helpers.
     *
     * Example usage:
     * ```
     * form.start(context) {
     *     val name = get("namePromptId")
     *     val age = getOrNull("agePromptId") ?: 0
     *     ...
     * }
     * ```
     *
     * @throws NotImplementedError If no platform handler is registered for the given context type.
     */
    fun <C : RenderContext> start(context: C, finish: FormSessionScope.(C) -> Unit) {
        val handler = Promptlin.handlerFor(context)
            ?: throw NotImplementedError("No platform registered for ${context::class.simpleName}")

        val session = FormSession(this, context, handler.renderer, finish)
        FormSessionRegistry.register(session)
        session.run()
    }
}