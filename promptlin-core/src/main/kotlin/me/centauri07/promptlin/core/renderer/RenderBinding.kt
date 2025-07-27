package me.centauri07.promptlin.core.renderer

import me.centauri07.promptlin.core.prompt.PromptInstanceScope
import me.centauri07.promptlin.core.prompt.Prompt
import kotlin.reflect.KClass

data class RenderBinding<T : Any, P : Prompt<*>, C : RenderContext>(
    val valueType: KClass<T>,
    val promptType: KClass<out P>,
    val contextType: KClass<out C>,
    val onInvoke: PromptInstanceScope.(C, P) -> Unit,
    val onComplete: PromptInstanceScope.(T, C, P) -> Unit,
    val onFailure: PromptInstanceScope.(C, P, Throwable) -> Unit
)