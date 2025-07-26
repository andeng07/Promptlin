package me.centauri07.promptlin.core.prompt

import me.centauri07.promptlin.core.form.FormSessionScope

abstract class PromptBuilderImpl<T>(
    val validators: MutableList<Prompt.Validator<T>> = mutableListOf(),
    var shouldInclude: FormSessionScope.() -> Boolean = { true }
) : PromptBuilder<T>() {

    override fun validate(message: String, predicate: FormSessionScope.(T) -> Boolean) {
        validators.add(Prompt.Validator(predicate, message))
    }

    override fun includeIf(predicate: FormSessionScope.() -> Boolean) {
        shouldInclude = predicate
    }

}