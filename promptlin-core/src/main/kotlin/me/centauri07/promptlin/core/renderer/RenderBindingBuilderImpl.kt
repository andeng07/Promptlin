package me.centauri07.promptlin.core.renderer

import com.sun.jdi.Type
import me.centauri07.promptlin.core.prompt.PromptInstanceScope
import me.centauri07.promptlin.core.prompt.Prompt
import kotlin.reflect.KClass

class RenderBindingBuilderImpl<T : Any, P : Prompt<T>, C : RenderContext>(
    private val valueType: KClass<T>, private val promptType: KClass<P>, private val contextType: KClass<C>
) : RenderBindingBuilder<T, P, C>() {

    var onInvoke: PromptInstanceScope.(C, P) -> Unit = { _, _ -> }
    var onComplete: PromptInstanceScope.(T, C, P) -> Unit = { _, _, _ -> }
    var onFailure: PromptInstanceScope.(C, P, Throwable) -> Unit = { _, _, _ -> }

    override fun onInvoke(block: PromptInstanceScope.(C, P) -> Unit) {
        onInvoke = block
    }

    override fun onComplete(block: PromptInstanceScope.(T, C, P) -> Unit) {
        onComplete = block
    }

    override fun onFailure(block: PromptInstanceScope.(C, P, Throwable) -> Unit) {
        onFailure = block
    }

    fun build(): RenderBinding<T, P, C> = RenderBinding(
        valueType, promptType, contextType, onInvoke, onComplete, onFailure
    )
}