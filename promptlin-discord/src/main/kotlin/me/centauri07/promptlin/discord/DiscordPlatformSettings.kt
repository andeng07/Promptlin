package me.centauri07.promptlin.discord

import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.choice.ChoicePrompt
import me.centauri07.promptlin.core.prompt.input.InputPrompt
import me.centauri07.promptlin.discord.prompt.choice.ButtonOption
import me.centauri07.promptlin.discord.prompt.choice.SelectOption

/**
 * A configuration class that defines how Discord prompts are handled and transformed
 * into messages of type [M] for a specific Discord context [C].
 *
 * @param C The type of [DiscordContext] representing the context for Discord interactions.
 * @param M The type of message object used in the Discord library or wrapper.
 *
 *
 * @property inputPromptMessage Function that generates the initial prompt message.
 * @property buttonPromptMessage Function that generates the initial button prompt message.
 * @property selectPromptMessage Function that generates the initial select prompt message.
 * @property completePromptMessage Function that generates the completion message for a prompt.
 * @property failurePromptMessage Function that generates the failure message for a prompt.
 */
data class DiscordPlatformSettings<C : DiscordContext<M>, M>(
    val inputPromptMessage: (C, InputPrompt<*>) -> M,
    val buttonPromptMessage: (C, ChoicePrompt<ButtonOption>, List<ButtonOption>) -> M,
    val selectPromptMessage: (C, ChoicePrompt<SelectOption>, List<SelectOption>) -> M,
    val completePromptMessage: (Any, C, Prompt<*>) -> M,
    val failurePromptMessage: (C, Prompt<*>, Throwable) -> M
) {
    /**
     * Creates a new [DiscordPlatformSettings] by copying the current settings and
     * applying additional customizations via a builder DSL.
     *
     * This allows overriding specific prompt message functions without
     * rebuilding the entire configuration from scratch.
     *
     * Example usage:
     * ```
     * val newSettings = oldSettings.override {
     *     completeMessage { value, ctx, prompt ->
     *         ctx.sendCustomMessage("Prompt completed with: $value")
     *     }
     * }
     * ```
     *
     * @param block A DSL function for configuring the [DiscordPlatformSettingsBuilder].
     * @return A new [DiscordPlatformSettings] instance with applied overrides.
     */
    fun override(block: DiscordPlatformSettingsBuilder<C, M>.() -> Unit) : DiscordPlatformSettings<C, M> {
        val builderImpl = DiscordPlatformSettingsBuilderImpl<C, M>()

        builderImpl.inputPromptMessage(inputPromptMessage)
        builderImpl.buttonPromptMessage(buttonPromptMessage)
        builderImpl.selectPromptMessage(selectPromptMessage)
        builderImpl.completeMessage(completePromptMessage)
        builderImpl.failureMessage(failurePromptMessage)

        builderImpl.block()

        return builderImpl.build()
    }
}