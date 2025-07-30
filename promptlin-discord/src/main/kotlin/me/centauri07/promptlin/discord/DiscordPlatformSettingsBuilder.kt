package me.centauri07.promptlin.discord

import me.centauri07.promptlin.core.BuilderDsl
import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.core.prompt.input.InputPrompt

/**
 * Builder DSL for creating or overriding [DiscordPlatformSettings].
 *
 * @param C Discord context type used for sending and receiving messages.
 * @param M Discord message type (e.g., `Message` in JDA or `MessageCreateData` in Kord).
 */
@BuilderDsl
interface DiscordPlatformSettingsBuilder<C : DiscordContext<M>, M> {

    /**
     * Sets the function used to create the message shown when an [InputPrompt] is triggered.
     *
     * @param block Produces the Discord message for an input prompt.
     */
    fun inputPromptMessage(block: (C, InputPrompt<*>) -> M)

    /**
     * Sets the function used to create the message shown when a prompt completes successfully.
     *
     * @param block Produces the Discord message for a completed prompt.
     */
    fun completeMessage(block: (Any, C, Prompt<*>) -> M)

    /**
     * Sets the function used to create the message shown when a prompt fails.
     *
     * @param block Produces the Discord message for a failed prompt.
     */
    fun failureMessage(block: (C, Prompt<*>, Throwable) -> M)
}

