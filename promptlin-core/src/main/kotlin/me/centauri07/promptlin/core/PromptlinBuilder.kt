package me.centauri07.promptlin.core

import me.centauri07.promptlin.core.platform.PlatformBuilder
import me.centauri07.promptlin.core.platform.PlatformBuilderImpl
import me.centauri07.promptlin.core.platform.PlatformHandler
import me.centauri07.promptlin.core.renderer.RenderContext

/**
 * DSL entrypoint for configuring Promptlin.
 *
 * The [PromptlinBuilder] provides a structured way to register platform-specific behavior
 * through [PlatformHandler]s and their associated renderers, bindings, and lifecycle hooks.
 *
 * It is designed to be subclassed and used within a DSL-style configuration block, such as:
 *
 * ```
 * promptlin {
 *     platform<MyRenderContext> {
 *         renderer { ... }
 *         bind<MyPrompt> { ... }
 *     }
 * }
 * ```
 *
 */
@BuilderDsl
abstract class PromptlinBuilder {

    /**
     * Registers a [PlatformHandler] for the given [RenderContext] type.
     *
     * Use this method to manually pass an already built [PlatformHandler] and further configure
     * it via [extension]. This gives full control over construction and mutation of the handler.
     *
     * @param platformHandler The platform handler to register.
     * @param extension Optional block to further configure the handler after construction.
     */
    abstract fun <C : RenderContext> platform(
        platformHandler: PlatformHandler<C>,
        extension: PlatformBuilder<C>.() -> Unit = {}
    )

    /**
     * Registers a new [PlatformHandler] for the given [RenderContext] type using a builder-style DSL.
     *
     * This is the more idiomatic way of declaring a platform in Promptlin. It will automatically
     * infer the platform’s [RenderContext] type via reified generics and apply the configuration block.
     *
     * @param platformHandlerBuilder A builder block used to configure the [PlatformHandler] for type [C].
     */
    inline fun <reified C : RenderContext> platform(noinline platformHandlerBuilder: PlatformBuilder<C>.() -> Unit) {
        val platform = PlatformBuilderImpl(C::class).build()
        platform(platform, platformHandlerBuilder)
    }

}