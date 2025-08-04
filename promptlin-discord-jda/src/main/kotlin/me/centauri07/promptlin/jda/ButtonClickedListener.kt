package me.centauri07.promptlin.jda

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateData

object ButtonClickedListener : ListenerAdapter() {
    private val queue: MutableMap<ButtonSessionKey, (MessageCreateData, String) -> Unit> = mutableMapOf()

    fun remove(userId: Long) {
        queue.keys.firstOrNull { it.userId == userId }?.also { queue.remove(it) }
    }

    fun queue(userId: Long, channelId: Long, messageId: Long, block: (MessageCreateData, String) -> Unit) {
        val key = ButtonSessionKey(userId, channelId, messageId)

        queue[key] = block
    }

    @SubscribeEvent
    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        val key = ButtonSessionKey(event.user.idLong, event.channel.idLong, event.message.idLong)

        queue.remove(key)?.also {
            event.deferEdit().queue()

            it.invoke(MessageCreateData.fromMessage(event.message), event.button.id!!)
        }

    }

    private data class ButtonSessionKey(
        val userId: Long,
        val channelId: Long,
        val messageId: Long
    )
}