package me.centauri07.promptlin.core.prompt

import me.centauri07.promptlin.core.form.FormSessionScope
import kotlin.reflect.KClass

abstract class PromptBuilderBaseImpl<T : Any>(
    protected val valueType: KClass<T>,
    protected val id: String,
    protected val name: String,
    protected val description: String,
    protected val validators: MutableList<Prompt.Validator<T>> = mutableListOf(),
    protected var shouldInclude: FormSessionScope.() -> Boolean = { true },
    protected var onComplete: (T) -> Unit = { }
) : PromptBuilderBase<T> {

    override fun validate(message: String, predicate: FormSessionScope.(T) -> Boolean) {
        validators.add(Prompt.Validator(predicate, message))
    }

    override fun includeIf(predicate: FormSessionScope.() -> Boolean) {
        shouldInclude = predicate
    }

    override fun onComplete(block: (T) -> Unit) {
        onComplete = block
    }

    abstract fun build(): Prompt<T>

}