package me.centauri07.promptlin.jda

import me.centauri07.promptlin.discord.DiscordPlatformHandler
import me.centauri07.promptlin.discord.DiscordPlatformSettings
import me.centauri07.promptlin.discord.discordSettings
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import java.awt.Color

class JDAPlatform(
    jda: JDA,
    discordPlatformSettings: DiscordPlatformSettings<JDAContext, MessageCreateData> = defaultSettings
) : DiscordPlatformHandler<JDAContext, MessageCreateData>(JDAContext::class, discordPlatformSettings) {
    companion object {
        val defaultSettings = discordSettings<JDAContext, MessageCreateData> {
            // Input Prompt (Blue)
            inputPromptMessage { ctx, prompt ->
                MessageCreateBuilder()
                    .addEmbeds(
                        EmbedBuilder()
                            .setColor(Color(0x3B82F6)) // Info Blue
                            .setTitle("Enter ${prompt.name}")
                            .setDescription(prompt.description)
                            .build()
                    )
                    .build()
            }

            buttonPromptMessage { ctx, prompt, options ->
                MessageCreateBuilder()
                    .addEmbeds(
                        EmbedBuilder()
                            .setColor(Color(0x3B82F6)) // Info Blue
                            .setTitle("Choose ${prompt.name}")
                            .setDescription(prompt.description)
                            .build()
                    )
                    .setActionRow(options.map { option ->
                        val buttonStyle = ButtonStyle.fromKey(option.style)
                        val id = option.value
                        val label = option.label
                        val emoji = option.emoji?.let { Emoji.fromUnicode(it) }

                        Button.of(buttonStyle, id, label, emoji)
                    })
                    .build()
            }

            selectPromptMessage { ctx, prompt, options ->
                MessageCreateBuilder()
                    .setContent("TO BE IMPLEMENTED!")
                    .build()
            }

            // Successful Input (Green)
            completeMessage { value, ctx, prompt ->
                MessageCreateBuilder()
                    .addEmbeds(
                        EmbedBuilder()
                            .setColor(Color(0x22C55E)) // Success Green
                            .setDescription("${prompt.name} set to `${value}`")
                            .build()
                    )
                    .build()
            }

            // Failed Input (Red)
            failureMessage { ctx, prompt, err ->
                MessageCreateBuilder()
                    .addEmbeds(
                        EmbedBuilder()
                            .setColor(Color(0xEF4444)) // Error Red
                            .setTitle("Invalid Input")
                            .setDescription("**${prompt.name}**: ${err.message}\nPlease try again.")
                            .build()
                    )
                    .build()
            }
        }
    }

    init {
        jda.addEventListener(
            MessageReceivedListener, ButtonClickedListener
        )
    }
}