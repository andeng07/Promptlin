package me.centauri07.promptlin.jda

import me.centauri07.promptlin.core.prompt.Prompt
import me.centauri07.promptlin.discord.DiscordContext
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder

/**
 * A [DiscordContext] implementation for JDA.
 *
 * @property channel The Discord channel where messages will be sent.
 * @property user The Discord user this context is associated with.
 */
class JDAContext(val channel: MessageChannel, val user: User) : DiscordContext<MessageCreateData>() {
    /**
     * Maps a prompt ID to the ID of the Discord message it created.
     *
     * This allows messages related to the same prompt to be edited instead of
     * always sending new messages.
     */
    private val promptMessages: MutableMap<String, Long> = mutableMapOf()

    override fun sendMessage(message: MessageCreateData) {
        channel.sendMessage(message).queue()
    }

    override fun sendMessage(
        type: MessageType,
        prompt: Prompt<*>,
        message: MessageCreateData
    ) {
        val promptMessageId = promptMessages[prompt.id]

        if (promptMessageId != null && type != MessageType.FAIL) {
            if (type != MessageType.PROMPT) {
                val retrievedMessage = channel.retrieveMessageById(promptMessageId).complete()
                retrievedMessage.editMessage(
                    MessageEditBuilder.fromCreateData(message).build()
                ).complete()
            }
            return
        }

        val message = channel.sendMessage(
            message
        ).complete()

        if (type != MessageType.FAIL) promptMessages[prompt.id] = message.idLong
    }

    override fun onMessageReceived(block: (MessageCreateData, String) -> Unit) {
        MessageReceivedListener.queue(user.idLong, channel.idLong, block)
    }

    override fun onButtonClicked(prompt: Prompt<*>, block: (MessageCreateData, String) -> Unit) {
        val promptMessageId = promptMessages[prompt.id]
            ?: throw IllegalArgumentException("Prompt with ID '${prompt.id}' has not been sent to the user yet.")

        ButtonClickedListener.queue(user.idLong, channel.idLong, promptMessageId, block)
    }
}