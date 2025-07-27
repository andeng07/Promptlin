package me.centauri07.promptlin.core.form

import me.centauri07.promptlin.core.prompt.Prompt

class FormBuilderImpl(
    private val prompts: MutableList<Prompt<*>> = mutableListOf()
) : FormBuilder() {

    override fun <T : Any> prompt(prompt: Prompt<T>): Prompt<T> {
        prompts += prompt
        return prompt
    }

    fun build(): Form = Form(prompts)

}