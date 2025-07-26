package me.centauri07.promptlin.core

import me.centauri07.promptlin.core.platform.PlatformBuilder
import me.centauri07.promptlin.core.platform.PlatformBuilderImpl
import me.centauri07.promptlin.core.platform.PlatformHandler
import me.centauri07.promptlin.core.renderer.RenderContext

class PromptlinBuilderImpl : PromptlinBuilder() {
    val handlers = mutableListOf<PlatformHandler<*>>()

    override fun <C : RenderContext> platform(
        platformHandler: PlatformHandler<C>,
        extension: PlatformBuilder<C>.() -> Unit
    ) {
        val builder = PlatformBuilderImpl(platformHandler.contextType)
        builder.renderer(platformHandler.renderer)
        builder.extension()

        handlers += builder.build()
    }
}