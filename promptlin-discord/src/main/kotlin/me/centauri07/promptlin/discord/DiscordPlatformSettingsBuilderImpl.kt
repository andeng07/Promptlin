package me.centauri07.promptlin.discord

import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.choice.ChoicePrompt
import me.centauri07.promptlin.core.prompt.input.InputPrompt
import me.centauri07.promptlin.discord.prompt.choice.ButtonOption
import me.centauri07.promptlin.discord.prompt.choice.SelectOption

open class DiscordPlatformSettingsBuilderImpl<C: DiscordContext<M>, M> : DiscordPlatformSettingsBuilder<C, M> {

    private lateinit var inputPromptMessage: (C, InputPrompt<*>) -> M
    private lateinit var buttonPromptMessage: (C, ChoicePrompt<ButtonOption>, List<ButtonOption>) -> M
    private lateinit var selectPromptMessage: (C, ChoicePrompt<SelectOption>, List<SelectOption>) -> M
    private lateinit var completePromptMessage: (Any, C, Prompt<*>) -> M
    private lateinit var failurePromptMessage: (C, Prompt<*>, Throwable) -> M

    override fun inputPromptMessage(block: (C, InputPrompt<*>) -> M) {
        inputPromptMessage = block
    }

    override fun buttonPromptMessage(block: (C, ChoicePrompt<ButtonOption>, List<ButtonOption>) -> M) {
        buttonPromptMessage = block
    }

    override fun selectPromptMessage(block: (C, ChoicePrompt<SelectOption>, List<SelectOption>) -> M) {
        selectPromptMessage = block
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
            buttonPromptMessage,
            selectPromptMessage,
            completePromptMessage,
            failurePromptMessage
        )
    }
}