package me.centauri07.promptlin.core.platform

import me.centauri07.promptlin.core.renderer.RenderContext
import me.centauri07.promptlin.core.renderer.Renderer
import kotlin.reflect.KClass

/**
 * A platform-specific handler that manages rendering logic for a particular [RenderContext] type.
 *
 * @param C The type of [RenderContext] this handler supports.
 * @property contextType The specific context class this handler is responsible for.
 * @property renderer The renderer used to render prompts within this context.
 */
abstract class PlatformHandler<C : RenderContext>(val contextType: KClass<C>, val renderer: Renderer<C>) {
    /**
     * Performs any platform-specific initialization logic.
     *
     * Override this to configure resources, listeners, or other platform-level setup
     * required before rendering begins. This is typically called once when the platform
     * is registered.
     */
    open fun initialize() {}

    /**
     * Checks if this handler can process the given [RenderContext] instance.
     *
     * @param context The render context to evaluate.
     * @return `true` if the handler supports this specific context instance.
     */
    fun canHandle(context: RenderContext): Boolean = canHandle(context::class)

    /**
     * Checks if this handler supports the given context type.
     *
     * @param contextType The type of [RenderContext] to evaluate.
     * @return `true` if this handler was registered for the given context type.
     */
    fun canHandle(contextType: KClass<*>): Boolean = this.contextType == contextType
}