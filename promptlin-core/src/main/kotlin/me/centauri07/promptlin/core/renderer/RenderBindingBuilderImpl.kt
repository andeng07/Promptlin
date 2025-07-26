package me.centauri07.promptlin.core.renderer

import me.centauri07.promptlin.core.prompt.PromptInstanceScope
import me.centauri07.promptlin.core.prompt.Prompt
import kotlin.reflect.KClass

class RenderBindingBuilderImpl<P : Prompt<*>, C : RenderContext>(
    private val promptType: KClass<P>, private val contextType: KClass<C>
) : RenderBindingBuilder<P, C>() {

    var onInvoke: PromptInstanceScope.(C, P) -> Unit = { _, _ -> }
    var onComplete: PromptInstanceScope.(C, P) -> Unit = { _, _ -> }
    var onFailure: PromptInstanceScope.(C, P, Throwable) -> Unit = { _, _, _-> }

    override fun onInvoke(block: PromptInstanceScope.(C, P) -> Unit) {
        onInvoke = block
    }

    override fun onComplete(block: PromptInstanceScope.(C, P) -> Unit) {
        onComplete = block
    }

    override fun onFailure(block: PromptInstanceScope.(C, P, Throwable) -> Unit) {
        onFailure = block
    }

    fun build(): RenderBinding<P, C> = RenderBinding(
        promptType, contextType, onInvoke, onComplete, onFailure
    )
}