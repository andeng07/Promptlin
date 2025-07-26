package me.centauri07.promptlin.core.renderer

import me.centauri07.promptlin.core.form.FormSession
import me.centauri07.promptlin.core.prompt.PromptInstance
import me.centauri07.promptlin.core.prompt.PromptInstanceScope
import me.centauri07.promptlin.core.prompt.Prompt
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Platform-agnostic form renderer.
 *
 * [Renderer] maps prompt types to platform-specific rendering logic.
 * Renderers can be defined using [me.centauri07.promptlin.core.renderer] DSL or extended via [extend].
 *
 * @param bindings A map of prompt types to their render functions.
 */
class Renderer<C : RenderContext> internal constructor(
    private val contextType: KClass<C>,
    private val bindings: List<RenderBinding<*, C>>
) {
    /**
     * Renders the given [prompt] using the associated renderer.
     * Throws a [NotImplementedError] if no renderer is found.
     *
     * @param prompt The prompt to render.
     */
    fun <P : PromptInstance<*>> invoke(session: FormSession<*>, prompt: P, context: C) {
        val renderer = resolve(prompt.prompt)

        if (renderer != null) {
            renderer.onInvoke(PromptInstanceScope(session, prompt), context, prompt.prompt)
        } else {
            throw NotImplementedError("No renderer registered for ${prompt::class.simpleName}")
        }
    }

    fun <P : PromptInstance<*>> complete(session: FormSession<*>, prompt: P, context: C) {
        val renderer = resolve(prompt.prompt)

        if (renderer != null) {
            renderer.onComplete(PromptInstanceScope(session, prompt), context, prompt.prompt)
        } else {
            throw NotImplementedError("No renderer registered for ${prompt::class.simpleName}")
        }
    }

    fun <P : PromptInstance<*>> failure(session: FormSession<*>, prompt: P, context: C, e: Throwable) {
        val renderer = resolve(prompt.prompt)

        if (renderer != null) {
            renderer.onFailure(PromptInstanceScope(session, prompt), context, prompt.prompt, e)
        } else {
            throw NotImplementedError("No renderer registered for ${prompt::class.simpleName}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <P : Prompt<*>> resolve(prompt: P): RenderBinding<P, C>? {
        val promptClass = prompt::class

        // Try exact match
        val exact = bindings.firstOrNull { it.promptType == promptClass }
        if (exact != null) return exact as RenderBinding<P, C>

        val fallback = bindings.firstOrNull {
            promptClass.isSubclassOf(it.promptType)
        }
        if (fallback != null) return fallback as RenderBinding<P, C>

        throw NotImplementedError("No renderer registered for ${prompt::class.simpleName}")
    }

    /**
     * Extends this renderer by importing bindings from another builder block.
     * Existing bindings are overwritten by those in [other].
     *
     * @param other A builder block defining additional bindings.
     * @return A new [Renderer] with merged bindings.
     */
    fun extend(other: RendererBuilder<C>.() -> Unit): Renderer<C> {
        val builder = RendererBuilderImpl(contextType, bindings.toMutableList())
        builder.other()
        return builder.build()
    }
}