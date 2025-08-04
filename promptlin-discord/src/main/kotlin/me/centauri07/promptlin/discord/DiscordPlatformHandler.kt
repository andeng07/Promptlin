package me.centauri07.promptlin.discord

import me.centauri07.promptlin.core.platform.PlatformHandler
import me.centauri07.promptlin.core.prompt.choice.ChoicePrompt
import me.centauri07.promptlin.core.prompt.input.InputPrompt
import me.centauri07.promptlin.core.renderer.Renderer
import me.centauri07.promptlin.core.renderer.renderer
import me.centauri07.promptlin.discord.prompt.choice.ButtonOption
import me.centauri07.promptlin.discord.prompt.choice.SelectOption
import kotlin.reflect.KClass

/**
 * A Discord-specific implementation of [PlatformHandler] for handling prompts
 * via Discord message interactions.
 *
 * This handler is generic over both the context type [C] and message type [M],
 * allowing it to support various Discord libraries or abstractions that define
 * their own [DiscordContext] implementations.
 *
 * @param C The specific subtype of [DiscordContext] used for Discord interactions.
 * @param M The message type specific to the Discord library being used.
 * @param contextType The [KClass] representing the [DiscordContext] subtype.
 */
abstract class DiscordPlatformHandler<C : DiscordContext<M>, M>(
    contextType: KClass<C>,
    settings: DiscordPlatformSettings<C, M>
) : PlatformHandler<C>(contextType, renderer(contextType, settings)) {
    companion object {
        /**
         * The default renderer behavior for Discord platforms.
         *
         * @return A [Renderer] tailored for [DiscordContext] input flow.
         */
        fun <C : DiscordContext<M>, M> renderer(
            contextType: KClass<C>,
            settings: DiscordPlatformSettings<C, M>
        ): Renderer<C> = renderer(contextType) {
            bind<Any, InputPrompt<Any>> {
                onInvoke { ctx, prompt ->
                    ctx.sendMessage(
                        DiscordContext.MessageType.PROMPT,
                        prompt,
                        settings.inputPromptMessage(ctx, prompt)
                    )
                    ctx.onMessageReceived { _, content ->
                        attemptSet(content)
                    }
                }

                onComplete { value, ctx, prompt ->
                    ctx.sendMessage(
                        DiscordContext.MessageType.COMPLETE,
                        prompt,
                        settings.completePromptMessage(value, ctx, prompt)
                    )
                }

                onFailure { ctx, prompt, e ->
                    ctx.sendMessage(
                        DiscordContext.MessageType.FAIL,
                        prompt,
                        settings.failurePromptMessage(ctx, prompt, e)
                        )
                }
            }

            bind<ButtonOption, ChoicePrompt<ButtonOption>> {
                onInvoke { ctx, prompt ->
                    ctx.sendMessage(
                        DiscordContext.MessageType.PROMPT,
                        prompt,
                        settings.buttonPromptMessage(ctx, prompt, prompt.getOptions(sessionScope))
                    )

                    ctx.onButtonClicked(prompt) { _, value ->
                        attemptSet(value)
                    }
                }

                onComplete { value, ctx, prompt ->
                    ctx.sendMessage(
                        DiscordContext.MessageType.COMPLETE,
                        prompt,
                        settings.completePromptMessage(value, ctx, prompt)
                    )
                }

                onFailure { ctx, prompt, e ->
                    ctx.sendMessage(
                        DiscordContext.MessageType.FAIL,
                        prompt,
                        settings.failurePromptMessage(ctx, prompt, e)
                    )
                }
            }

            bind<SelectOption, ChoicePrompt<SelectOption>> {
                onInvoke { ctx, prompt ->
                    ctx.sendMessage(
                        DiscordContext.MessageType.PROMPT,
                        prompt,
                        settings.selectPromptMessage(ctx, prompt, prompt.getOptions(sessionScope))
                    )

                    ctx.onSelect(prompt) { _, value ->
                        attemptSet(value)
                    }
                }

                onComplete { value, ctx, prompt ->
                    ctx.sendMessage(
                        DiscordContext.MessageType.COMPLETE,
                        prompt,
                        settings.completePromptMessage(value, ctx, prompt)
                    )
                }

                onFailure { ctx, prompt, e ->
                    ctx.sendMessage(
                        DiscordContext.MessageType.FAIL,
                        prompt,
                        settings.failurePromptMessage(ctx, prompt, e)
                    )
                }
            }
        }
    }
}