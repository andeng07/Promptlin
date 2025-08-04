package me.centauri07.promptlin.jda

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateData

object SelectionMenuListener : ListenerAdapter() {
    private val queue: MutableMap<SelectionSessionKey, (MessageCreateData, String) -> Unit> = mutableMapOf()

    fun queue(userId: Long, channelId: Long, messageId: Long, block: (MessageCreateData, String) -> Unit) {
        val key = SelectionSessionKey(userId, channelId, messageId)
        queue[key] = block
    }

    @SubscribeEvent
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        val key = SelectionSessionKey(event.user.idLong, event.channel.idLong, event.message.idLong)

        queue.remove(key)?.also { handler ->
            event.deferEdit().queue()

            handler.invoke(MessageCreateData.fromMessage(event.message), event.values.joinToString(","))
        }
    }

    private data class SelectionSessionKey(
        val userId: Long,
        val channelId: Long,
        val messageId: Long
    )
}