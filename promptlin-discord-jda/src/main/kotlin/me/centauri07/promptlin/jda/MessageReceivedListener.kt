package me.centauri07.promptlin.jda

import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateData

object MessageReceivedListener : ListenerAdapter() {

    private val queue: MutableMap<InputSessionKey, (MessageCreateData, String) -> Unit> = mutableMapOf()

    fun queue(userId: Long, channelId: Long, block: (MessageCreateData, String) -> Unit) {
        val key = InputSessionKey(userId, channelId)

        queue[key] = block
    }

    @SubscribeEvent
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val key = InputSessionKey(event.author.idLong, event.channel.idLong)

        queue.remove(key)?.also {
            if (event.channelType != ChannelType.PRIVATE) event.message.delete().queue()
            it.invoke(MessageCreateData.fromMessage(event.message), event.message.contentRaw)
        }
    }

    private data class InputSessionKey(
        val userId: Long,
        val channelId: Long
    )

}