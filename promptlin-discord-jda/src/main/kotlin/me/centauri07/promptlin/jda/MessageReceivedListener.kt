package me.centauri07.promptlin.jda

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.utils.messages.MessageCreateData

open class MessageReceivedListener : ListenerAdapter() {

    companion object {
        private val queue: MutableMap<InputSessionKey, (MessageCreateData, String) -> Unit> = mutableMapOf()

        fun queue(userId: Long, channelId: Long, block: (MessageCreateData, String) -> Unit) {
            val key = InputSessionKey(userId, channelId)

            queue[key] = block
        }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.message.contentRaw.startsWith("!form")) return

        val key = InputSessionKey(event.author.idLong, event.channel.idLong)

        queue.remove(key)?.also {
            event.message.delete().queue()
            it.invoke(MessageCreateData.fromMessage(event.message), event.message.contentRaw)
        }
    }

    private data class InputSessionKey(
        val userId: Long,
        val channelId: Long
    )
}