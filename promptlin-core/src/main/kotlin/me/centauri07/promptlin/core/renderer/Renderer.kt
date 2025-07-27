package me.centauri07.promptlin.core.renderer

import me.centauri07.promptlin.core.form.FormSession
import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.PromptInstance
import me.centauri07.promptlin.core.prompt.PromptInstanceScope
import kotlin.reflect.KClass

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
    private val bindings: List<RenderBinding<*, out Prompt<*>, C>>
) {
    /**
     * Renders the given [prompt] using the associated renderer.
     * Throws a [NotImplementedError] if no renderer is found.
     *
     * @param prompt The prompt to render.
     */
    fun <T : Any, P : PromptInstance<T>> invoke(session: FormSession<*>, prompt: P, context: C) {
        val renderer = resolve(prompt.prompt)
            ?: throw NotImplementedError("No renderer registered for ${prompt::class.simpleName}")

        renderer.onInvoke(PromptInstanceScope(session, prompt), context, prompt.prompt)
    }

    fun <T : Any, P : PromptInstance<T>> complete(session: FormSession<*>, prompt: P, context: C) {
        val renderer = resolve(prompt.prompt)
            ?: throw NotImplementedError("No renderer registered for ${prompt::class.simpleName}")

        renderer.onComplete(PromptInstanceScope(session, prompt), prompt.value as T, context, prompt.prompt)
    }

    fun <T : Any, P : PromptInstance<T>> failure(session: FormSession<*>, prompt: P, context: C, e: Throwable) {
        val renderer = resolve(prompt.prompt)
            ?: throw NotImplementedError("No renderer registered for ${prompt::class.simpleName}")

        renderer.onFailure(PromptInstanceScope(session, prompt), context, prompt.prompt, e)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any, P : Prompt<T>> resolve(prompt: P): RenderBinding<T, P, C>? {    val promptType = prompt::class
        val contextType = contextType
        val valueType = prompt.valueType

        bindings.forEach { binding ->
            if (binding.promptType == promptType &&
                binding.contextType == contextType &&
                binding.valueType == valueType
            ) {
                return binding as? RenderBinding<T, P, C>
            }
        }

        bindings.forEach { binding ->
            if (binding.promptType == promptType &&
                binding.contextType == contextType &&
                binding.valueType.java.isAssignableFrom(valueType.java)
            ) {
                return binding as? RenderBinding<T, P, C>
            }
        }

        bindings.firstOrNull {
            it.promptType == promptType &&
                    it.contextType == contextType
        }?.let { return it as? RenderBinding<T, P, C> }

        return null
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