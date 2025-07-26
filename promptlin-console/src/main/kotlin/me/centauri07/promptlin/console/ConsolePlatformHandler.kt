package me.centauri07.promptlin.console

import me.centauri07.promptlin.core.platform.PlatformHandler
import me.centauri07.promptlin.core.prompt.input.InputPrompt
import me.centauri07.promptlin.core.renderer.renderer

/**
 * A [PlatformHandler] implementation for handling console-based rendering and interaction logic
 * using the [ConsoleContext].
 */
class ConsolePlatformHandler : PlatformHandler<ConsoleContext>(ConsoleContext::class, renderer) {
    companion object {
        val renderer = renderer<ConsoleContext> {
            bind<InputPrompt<*>> {
                onInvoke { ctx, prompt ->
                    ctx.sendOutput("Enter ${prompt.name} (${prompt.description}):")
                    attemptSet(ctx.awaitInput())
                }

                onComplete { ctx, prompt ->
                    ctx.sendOutput("Field `${prompt.name}` has been successfully set.")
                }

                onFailure { ctx, prompt, e ->
                    ctx.sendOutput("Error encountered: ${e.message}")
                }
            }
        }
    }
}