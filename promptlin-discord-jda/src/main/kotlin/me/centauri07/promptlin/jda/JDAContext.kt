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
                channel.retrieveMessageById(promptMessageId).queue {
                    it.editMessage(
                        MessageEditBuilder.fromCreateData(message).build()
                    ).queue()
                }
            }
            return
        }

        channel.sendMessage(
            message
        ).queue {
            if (type != MessageType.FAIL) promptMessages[prompt.id] = it.idLong
        }
    }

    override fun onMessageReceived(block: (MessageCreateData, String) -> Unit) {
        MessageReceivedListener.queue(user.idLong, channel.idLong, block)
    }
}