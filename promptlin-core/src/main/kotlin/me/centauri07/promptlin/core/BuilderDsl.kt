package me.centauri07.promptlin.core

/**
 * DSL marker annotation for Promptlin builder scopes.
 *
 * This annotation is used to restrict the scope of DSL builder functions to prevent
 * unintentional access to outer receivers in nested DSL contexts.
 * @see [DslMarker] for more information on how DSL scoping works in Kotlin.
 */
@DslMarker
annotation class BuilderDsl
