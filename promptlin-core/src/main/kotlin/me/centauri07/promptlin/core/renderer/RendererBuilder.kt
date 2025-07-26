package me.centauri07.promptlin.core.renderer

import me.centauri07.promptlin.core.BuilderDsl
import me.centauri07.promptlin.core.prompt.Prompt
import kotlin.reflect.KClass

/**
 * Builder for configuring render behavior for a specific [RenderContext] type.
 *
 * This DSL class allows the user to define how each [Prompt] type should be rendered
 * using [RenderBinding]s. It provides both a generic and a reified overload for convenience.
 *
 * Example usage:
 * ```
 * renderer<ConsoleContext> {
 *     bind<InputPrompt<*>> {
 *         onInvoke { ctx, prompt -> ... }
 *         onComplete { ctx, prompt -> ... }
 *         onFailure { ctx, prompt, e -> ... }
 *     }
 * }
 * ```
 *
 * @param C the type of [RenderContext] this builder is targeting
 */
@BuilderDsl
abstract class RendererBuilder<C : RenderContext>() {
    /**
     * Registers a render binding for the given [Prompt] type using its runtime [KClass].
     *
     * @param P the type of [Prompt]
     * @param promptType the [KClass] of the prompt
     * @param block the configuration block for building the render logic
     */
    abstract fun <P : Prompt<*>> bind(promptType: KClass<P>, block: RenderBindingBuilder<P, C>.() -> Unit)

    /**
     * Registers a render binding for the given [Prompt] type using a reified type parameter.
     *
     * This overload simplifies usage by removing the need to pass a [KClass] explicitly.
     *
     * @param P the type of [Prompt]
     * @param block the configuration block for building the render logic
     */
    inline fun <reified P : Prompt<*>> bind(
        noinline block: RenderBindingBuilder<P, C>.() -> Unit
    ) {
        bind(P::class, block)
    }
}