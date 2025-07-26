package me.centauri07.promptlin.core

import me.centauri07.promptlin.core.platform.PlatformHandler
import me.centauri07.promptlin.core.renderer.RenderContext

/**
 * Singleton entry point for configuring and managing Promptlin's platform handlers.
 *
 * This object holds global state and logic for initializing and retrieving [PlatformHandler]s
 * bound to specific [RenderContext] types.
 *
 * Usage example:
 * ```
 * Promptlin.configure {
 *     platform<ConsoleContext> {
 *         renderer {
 *             bind<InputPrompt<*>> {
 *                 onInvoke { ctx, prompt -> ... }
 *             }
 *         }
 *     }
 * }
 *
 * val handler = Promptlin.handlerFor<ConsoleContext>()
 * ```
 */
object Promptlin {
    /**
     * The list of all registered [PlatformHandler]s across different [RenderContext]s.
     */
    val handlers = mutableListOf<PlatformHandler<*>>()

    /**
     * Configures Promptlin using the provided DSL [builder].
     *
     * This function creates an internal [PromptlinBuilderImpl], applies the DSL configuration,
     * initializes all registered [PlatformHandler]s, and stores them in [handlers].
     *
     * @param builder the DSL configuration block for defining platforms and their renderers
     */
    fun configure(builder: PromptlinBuilder.() -> Unit) {
        val builderImpl = PromptlinBuilderImpl()
        builderImpl.builder()

        builderImpl.handlers.forEach { it.initialize() }

        handlers += builderImpl.handlers
    }

    /**
     * Retrieves a [PlatformHandler] for the given [renderContext] instance.
     *
     * @param C the type of [RenderContext]
     * @param renderContext the actual context instance
     * @return the matching [PlatformHandler] for the given context, or null if not found
     */
    fun <C : RenderContext> handlerFor(renderContext: C): PlatformHandler<C>? =
        handlers.firstOrNull { it.canHandle(renderContext) } as? PlatformHandler<C>
}