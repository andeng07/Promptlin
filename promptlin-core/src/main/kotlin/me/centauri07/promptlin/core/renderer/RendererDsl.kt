package me.centauri07.promptlin.core.renderer

import kotlin.reflect.KClass

/**
 * Creates a [Renderer] for the specified [RenderContext] subtype using a DSL-style builder block.
 *
 * This is an inline function that leverages Kotlin's `reified` type parameters to infer the context type.
 * It is typically used when building renderers in a concise DSL format.
 *
 * Example usage:
 * ```
 * renderer<ConsoleContext> {
 *     bind<InputPrompt<*>> {
 *         onInvoke { ctx, prompt ->
 *             ctx.sendMessage("Enter ${prompt.name}: ")
 *             attemptSet(ctx.awaitMessage())
 *         }
 *         onFailure { ctx, prompt, e ->
 *             ctx.sendMessage("Error: ${e::class.simpleName}: ${e.message}")
 *         }
 *     }
 * }
 * ```
 *
 * @param C the type of [RenderContext] this renderer supports
 * @param builder the builder block used to configure the renderer
 * @return a constructed [Renderer] for the specified context
 */
inline fun <reified C : RenderContext> renderer(
    builder: RendererBuilder<C>.() -> Unit
): Renderer<C> {
    val impl = RendererBuilderImpl(C::class)
    impl.builder()
    return impl.build()
}

/**
 * Creates a [Renderer] for the specified [RenderContext] subtype using a class reference and builder block.
 *
 * This version is useful in contexts where inline functions or reified types are not available, such as when calling from Java code.
 *
 * Example usage:
 * ```
 * val renderer = renderer(ConsoleContext::class) {
 *     bind<InputPrompt<*>> {
 *         onInvoke { ctx, prompt ->
 *             ctx.sendMessage("Enter ${prompt.name}: ")
 *             attemptSet(ctx.awaitMessage())
 *         }
 *     }
 * }
 * ```
 *
 * @param C the type of [RenderContext] this renderer supports
 * @param contextType the class reference of the context type
 * @param builder the builder block used to configure the renderer
 * @return a constructed [Renderer] for the specified context
 */
fun <C : RenderContext> renderer(
    contextType: KClass<C>,
    builder: RendererBuilder<C>.() -> Unit
): Renderer<C> {
    val impl = RendererBuilderImpl(contextType)
    impl.builder()
    return impl.build()
}