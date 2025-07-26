package me.centauri07.promptlin.core.renderer

import me.centauri07.promptlin.core.BuilderDsl
import me.centauri07.promptlin.core.prompt.PromptInstanceScope
import me.centauri07.promptlin.core.prompt.Prompt

/**
 * DSL builder for defining how a specific [Prompt] should behave within a given [RenderContext].
 *
 * This class allows consumers to customize the rendering lifecycle of a prompt.
 *
 * Typically used inside the [RendererBuilder.bind] DSL block.
 *
 * Example:
 * ```
 * bind<InputPrompt<*>> {
 *     onInvoke { ctx, prompt ->
 *         ctx.sendMessage("Enter ${prompt.name}")
 *         attemptSet(ctx.awaitMessage())
 *     }
 *     onComplete { _, _ -> println("Prompt completed!") }
 *     onFailure { _, _, e -> println("Error: ${e.message}") }
 * }
 * ```
 *
 * @param P the type of [Prompt] being rendered
 * @param C the type of [RenderContext] in which rendering occurs
 *
 * @see RendererBuilder
 * @see RenderBinding
 */
@BuilderDsl
abstract class RenderBindingBuilder<P : Prompt<*>, C : RenderContext> {
    /**
     * Defines the behavior that occurs when the prompt is first invoked.
     *
     * @param block the logic to execute when the prompt is shown
     */
    abstract fun onInvoke(block: PromptInstanceScope.(C, P) -> Unit)

    /**
     * Defines the behavior that occurs when the prompt is completed successfully.
     *
     * @param block the logic to execute on successful completion
     */
    abstract fun onComplete(block: PromptInstanceScope.(C, P) -> Unit)

    /**
     * Defines the behavior that occurs when an exception is thrown during prompt processing.
     *
     * @param block the logic to execute on failure
     */
    abstract fun onFailure(block: PromptInstanceScope.(C, P, e: Throwable) -> Unit)
}