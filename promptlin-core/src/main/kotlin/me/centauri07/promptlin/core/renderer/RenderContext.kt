package me.centauri07.promptlin.core.renderer

/**
 * Represents the context in which a [me.centauri07.promptlin.core.prompt.Prompt] is rendered.
 *
 * Subclasses of [RenderContext] define the platform-specific rendering environment and behavior—
 * such as console, web UI, Discord, or other I/O mechanisms.
 *
 * This context is passed into render logic blocks such as `onInvoke`, `onComplete`, and `onFailure`
 * so that prompts can be handled appropriately depending on the platform.
 */
interface RenderContext