package me.centauri07.promptlin.core.renderer

import me.centauri07.promptlin.core.BuilderDsl
import me.centauri07.promptlin.core.prompt.Prompt
import kotlin.reflect.KClass

/**
 * Internal builder implementation that holds mutable bindings.
 */
@BuilderDsl
class RendererBuilderImpl<C : RenderContext>(
    private val contextType: KClass<C>,
    private val bindings: MutableList<RenderBinding<*, out Prompt<*>, C>> = mutableListOf()
) : RendererBuilder<C>() {

    override fun <T : Any, P : Prompt<T>> bind(
        valueType: KClass<T>,
        promptType: KClass<P>,
        block: RenderBindingBuilder<T, P, C>.() -> Unit
    ) {
        val renderBinding = RenderBindingBuilderImpl(valueType, promptType, contextType)

        renderBinding.block()

        bindings.add(renderBinding.build())
    }

    /**
     * Finalizes and builds the [Renderer] instance.
     */
    fun build(): Renderer<C> = Renderer(contextType, bindings.toList())
}