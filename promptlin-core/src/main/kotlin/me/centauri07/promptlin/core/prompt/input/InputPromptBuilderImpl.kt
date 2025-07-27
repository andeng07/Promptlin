package me.centauri07.promptlin.core.prompt.input

import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.PromptBuilderImpl
import kotlin.reflect.KClass

class InputPromptBuilderImpl<T : Any>(
    private val handler: InputHandler<T>,
    private val valueType: KClass<T>,
    private val id: String,
    private val name: String,
    private val description: String
) : PromptBuilderImpl<T>() {

    fun build(): Prompt<T> {
        return InputPrompt(handler, valueType, id, name, description, validators, shouldInclude)
    }

}