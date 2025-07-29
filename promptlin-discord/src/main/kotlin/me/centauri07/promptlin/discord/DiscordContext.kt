package me.centauri07.promptlin.discord

import me.centauri07.promptlin.core.renderer.RenderContext

/**
 * Represents the runtime context for handling Discord interactions in a prompt-based system.
 *
 * A [DiscordContext] provides the ability to:
 * 1. Send messages of type [M] to the Discord platform.
 * 2. Listen for incoming messages and react to user responses.
 *
 * This context serves as the bridge between your prompt rendering logic
 * and the underlying Discord library or API.
 *
 * @param M The type of message object used in the Discord library (e.g., `Message` in JDA or `MessageCreateData`).
 */
abstract class DiscordContext<M> : RenderContext {
    /**
     * Sends a message to the Discord platform.
     *
     * Implementations should handle message delivery through the chosen Discord library or API.
     *
     * @param message The message object to send to Discord.
     */
    abstract fun sendMessage(message: M)

    /**
     * Registers a listener to be invoked when a new message is received.
     *
     * Implementations should call [block] whenever a message event occurs in the Discord channel
     * relevant to this context.
     *
     * @param block Callback invoked with the received message object and its raw string content.
     * The first parameter is the raw message object [M], and the second is the message content [String].
     */
    abstract fun onMessageReceived(block: (M, String) -> Unit)
}