package me.centauri07.promptlin.discord

/**
 * Creates a [DiscordPlatformSettings] instance using a DSL builder.
 *
 * This is the primary way to define how prompts are transformed into Discord messages
 * for a specific [DiscordContext] and message type [M].
 *
 * Example:
 * ```
 * val settings = discordSettings<MyContext, MyMessage> {
 *     inputPromptMessage { ctx, prompt -> createPromptMessage(prompt) }
 *     completeMessage { value, ctx, prompt -> createCompletionMessage(value) }
 *     failureMessage { prompt, error -> createErrorMessage(error) }
 * }
 * ```
 *
 * @param block Configures the [DiscordPlatformSettingsBuilder] for this settings instance.
 * @return A new [DiscordPlatformSettings] with the configured behavior.
 */
inline fun <C : DiscordContext<M>, M> discordSettings(
    block: DiscordPlatformSettingsBuilder<C, M>.() -> Unit
): DiscordPlatformSettings<C, M> {
    val builder = DiscordPlatformSettingsBuilderImpl<C, M>()

    builder.block()

    return builder.build()
}