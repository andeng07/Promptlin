package me.centauri07.promptlin.core.prompt.input

import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.PromptBuilderImpl

class InputPromptBuilderImpl<T>(
    private val handler: InputHandler<T>,
    private val id: String,
    private val name: String,
    private val description: String
) : PromptBuilderImpl<T>() {

    fun build(): Prompt<T> {
        return InputPrompt(handler, id, name, description, validators, shouldInclude)
    }

}