package me.centauri07.promptlin.core.renderer

import me.centauri07.promptlin.core.prompt.PromptInstanceScope
import me.centauri07.promptlin.core.prompt.Prompt
import kotlin.reflect.KClass

data class RenderBinding<P : Prompt<*>, C : RenderContext>(
    val promptType: KClass<out P>,
    val contextType: KClass<out C>,
    val onInvoke: PromptInstanceScope.(C, P) -> Unit,
    val onComplete: PromptInstanceScope.(C, P) -> Unit,
    val onFailure: PromptInstanceScope.(C, P, Throwable) -> Unit
)