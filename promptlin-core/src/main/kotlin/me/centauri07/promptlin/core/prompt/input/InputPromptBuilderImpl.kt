package me.centauri07.promptlin.core.prompt.input

import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.PromptBuilderBaseImpl
import kotlin.reflect.KClass

class InputPromptBuilderImpl<T : Any>(
    private val handler: InputHandler<T>,
    valueType: KClass<T>,
    id: String,
    name: String,
    description: String
) : PromptBuilderBaseImpl<T>(valueType, id, name, description) {

    override fun build(): Prompt<T> {
        return InputPrompt(handler, valueType, id, name, description, validators, shouldInclude, onComplete)
    }

}