package me.centauri07.promptlin.console

import me.centauri07.promptlin.core.platform.PlatformHandler
import me.centauri07.promptlin.core.prompt.choice.Option
import me.centauri07.promptlin.core.prompt.choice.ChoicePrompt
import me.centauri07.promptlin.core.prompt.input.InputPrompt
import me.centauri07.promptlin.core.renderer.renderer

/**
 * A [PlatformHandler] implementation for handling console-based rendering and interaction logic
 * using the [ConsoleContext].
 */
class ConsolePlatformHandler : PlatformHandler<ConsoleContext>(ConsoleContext::class, renderer) {
    companion object {
        val renderer = renderer<ConsoleContext> {
            bind<Any, InputPrompt<Any>> {
                onInvoke { ctx, prompt ->
                    ctx.sendOutput("Enter ${prompt.name} (${prompt.description}):")
                    attemptSet(ctx.awaitInput())
                }

                onComplete { value, ctx, prompt ->
                    ctx.sendOutput("Field `${prompt.name}` has been successfully set to `${value}`.")
                }

                onFailure { ctx, prompt, e ->
                    ctx.sendOutput("Error encountered: ${e.message}")
                }
            }

            bind<Option, ChoicePrompt<Option>> {
                onInvoke { ctx, prompt ->
                    ctx.sendOutput("Enter ${prompt.name} (${prompt.description}):\nChoices: ${
                        prompt.getOptions(sessionScope).joinToString { it.label }
                    }")
                    attemptSet(ctx.awaitInput())
                }

                onComplete { value, ctx, prompt ->
                    ctx.sendOutput("Field `${prompt.name}` has been successfully set to `${value.label}`.")
                }

                onFailure { ctx, prompt, e ->
                    ctx.sendOutput("Error encountered: ${e.message}")
                }
            }
        }
    }
}