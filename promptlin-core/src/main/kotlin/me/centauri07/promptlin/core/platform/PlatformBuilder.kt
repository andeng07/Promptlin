package me.centauri07.promptlin.core.platform

import me.centauri07.promptlin.core.BuilderDsl
import me.centauri07.promptlin.core.renderer.RenderContext
import me.centauri07.promptlin.core.renderer.Renderer
import me.centauri07.promptlin.core.renderer.RendererBuilder
import me.centauri07.promptlin.core.renderer.RendererBuilderImpl
import kotlin.reflect.KClass

/**
 * DSL builder for defining a platform-specific rendering pipeline.
 *
 * Used to register renderers and lifecycle initialization logic tied to a specific [RenderContext] type.
 *
 * @param C The type of [RenderContext] associated with this platform.
 * @property contextType The class of the supported context.
 */
@BuilderDsl
abstract class PlatformBuilder<C : RenderContext>(
    val contextType: KClass<C>
) {
    /**
     * Registers a platform-specific initialization routine.
     *
     * This block is called once during platform setup. Use it to register resources,
     * attach event listeners, or perform any other initialization tasks required
     * before rendering begins.
     *
     * @param platformHandler A lambda that contains platform-specific setup logic.
     */
    abstract fun initialize(platformHandler: () -> Unit)

    /**
     * Registers a [Renderer] and provides additional configuration through a [RendererBuilder].
     *
     * @param renderer The renderer responsible for rendering prompts on this platform.
     * @param rendererBuilder A lambda used to configure how individual prompts are rendered.
     */
    abstract fun renderer(
        renderer: Renderer<C>,
        rendererBuilder: RendererBuilder<C>.() -> Unit = {}
    )

    /**
     * Registers a [Renderer] using a [RendererBuilder] DSL.
     *
     * This is a shorthand version of [renderer], allowing inline construction and configuration.
     *
     * @param builder A lambda to build and configure the renderer.
     */
    fun renderer(
        builder: RendererBuilder<C>.() -> Unit = {}
    ) {
        val impl = RendererBuilderImpl(contextType)
        impl.builder()
        renderer(impl.build(), builder)
    }
}