package me.centauri07.promptlin.discord

import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.input.InputPrompt

open class DiscordPlatformSettingsBuilderImpl<C: DiscordContext<M>, M> : DiscordPlatformSettingsBuilder<C, M> {
    private lateinit var inputPromptMessage: (C, InputPrompt<*>) -> M
    private lateinit var completePromptMessage: (Any, C, Prompt<*>) -> M
    private lateinit var failurePromptMessage: (C, Prompt<*>, Throwable) -> M

    override fun inputPromptMessage(block: (C, InputPrompt<*>) -> M) {
        inputPromptMessage = block
    }

    override fun completeMessage(block: (Any, C, Prompt<*>) -> M) {
        completePromptMessage = block
    }

    override fun failureMessage(block: (C, Prompt<*>, Throwable) -> M) {
        failurePromptMessage = block
    }

    fun build() : DiscordPlatformSettings<C, M> {
        return DiscordPlatformSettings(
            inputPromptMessage,
            completePromptMessage,
            failurePromptMessage
        )
    }
}