package me.centauri07.promptlin.core.form

import me.centauri07.promptlin.core.BuilderDsl
import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.PromptBuilderBase
import me.centauri07.promptlin.core.prompt.input.InputPromptBuilderImpl
import me.centauri07.promptlin.core.prompt.input.InputHandler

/**
 * DSL entry point for building a [Form] composed of one or more [Prompt]s.
 *
 * Implementations of this class are responsible for collecting prompts that define the structure
 * of the form. You typically use the [input] function to define new input prompts and configure
 * them using a [PromptBuilderBase].
 *
 * Marked with [@BuilderDsl] to scope DSL usage within a [FormBuilder] block.
 */
@BuilderDsl
abstract class FormBuilder {
    /**
     * Adds a prebuilt [Prompt] to the form.
     *
     * This is useful if you have created a prompt elsewhere and want to include it in the form manually.
     * This function returns the same instance for convenience and chaining.
     *
     * @param T The type of value returned by the prompt.
     * @param prompt The [Prompt] instance to add.
     * @return The same [prompt] that was added.
     */
    abstract fun <T : Any> prompt(prompt: Prompt<T>): Prompt<T>

    /**
     * Creates and registers a new [me.centauri07.promptlin.core.prompt.input.InputPrompt] with the form using the given [InputHandler], ID, name,
     * and description. Additional configuration can be done through a [PromptBuilderBase] DSL block.
     *
     * Example:
     * ```
     * input(IntInputHandler, "age", "Age", "Enter your age") {
     *     validate("Must be 18 or older") { it >= 18 }
     *     includeIf { get("country") == "USA" }
     * }
     * ```
     *
     * @param T The type of input this prompt handles.
     * @param handler The [InputHandler] responsible for parsing and validating user input.
     * @param id The unique identifier for this prompt.
     * @param name The display name of the prompt.
     * @param description A brief explanation or instruction for the user.
     * @param promptBuilderBase A DSL block to configure validation, conditional inclusion, etc.
     * @return The constructed and registered [Prompt] instance.
     */
    inline fun <reified T : Any> input(
        handler: InputHandler<T>,
        id: String,
        name: String,
        description: String,
        promptBuilderBase: PromptBuilderBase<T>.() -> Unit = {}
    ): Prompt<T> {
        val promptBuilderImpl = InputPromptBuilderImpl(handler, T::class, id, name, description)

        promptBuilderImpl.promptBuilderBase()

        val prompt = promptBuilderImpl.build()

        prompt(prompt)

        return prompt
    }
}