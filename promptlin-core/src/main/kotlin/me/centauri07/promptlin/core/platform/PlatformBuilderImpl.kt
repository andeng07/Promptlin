package me.centauri07.promptlin.core.platform

import me.centauri07.promptlin.core.renderer.RenderContext
import me.centauri07.promptlin.core.renderer.Renderer
import me.centauri07.promptlin.core.renderer.RendererBuilder
import kotlin.reflect.KClass

class PlatformBuilderImpl<C : RenderContext>(
    contextType: KClass<C>,
) : PlatformBuilder<C>(contextType) {
    private lateinit var renderer: Renderer<C>
    private var initializer: () -> Unit = {}

    override fun initialize(platformHandler: () -> Unit) {
        initializer = platformHandler
    }

    override fun renderer(
        renderer: Renderer<C>,
        rendererBuilder: RendererBuilder<C>.() -> Unit
    ) {
        this.renderer = renderer.extend(rendererBuilder)
    }

    fun build(): PlatformHandler<C> = object : PlatformHandler<C>(contextType, renderer)  {
        override fun initialize() {
            this@PlatformBuilderImpl.initializer.invoke()
        }
    }
}