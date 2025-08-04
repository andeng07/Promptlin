package me.centauri07.promptlin.core.form

import me.centauri07.promptlin.core.renderer.RenderContext

/**
 * A global registry for managing all active [FormSession] instances.
 *
 * This registry provides utilities to register, unregister, query, and clear form sessions
 * during the application's runtime. It enables tracking of currently running forms and
 * performing operations on them (e.g., searching, filtering, or bulk removal).
 */
@Suppress("UNCHECKED_CAST")
object FormSessionRegistry {

    /** Holds all active form sessions */
    private val sessions = mutableSetOf<FormSession<*>>()

    /**
     * Returns a snapshot of all currently registered [FormSession]s.
     */
    fun getSessions(): List<FormSession<*>> = sessions.toList()

    /** Registers a new [FormSession] into the registry. */
    fun register(session: FormSession<*>) {
        sessions += session
    }

    /** Unregisters an existing [FormSession] from the registry. */
    fun unregister(session: FormSession<*>) {
        sessions -= session
    }

    /** Finds and unregisters the first [FormSession] that matches the given [predicate]. */
    fun unregister(predicate: (FormSession<*>) -> Boolean) {
        sessions.removeIf(predicate)
    }

    /** Finds and unregisters the first [FormSession] of type [T] that matches the [predicate]. */
    @JvmName("unregisterTyped")
    inline fun <reified T : RenderContext> unregister(predicate: (FormSession<T>) -> Boolean) {
        getSessions().firstOrNull { it.context is T && predicate(it as FormSession<T>) }?.also { unregister(it) }
    }

    /** Returns whether any session in the registry matches the given [predicate]. */
    fun contains(predicate: (FormSession<*>) -> Boolean): Boolean =
        sessions.any(predicate)

    /** Returns whether any session of type [T] matches the given [predicate]. */
    @JvmName("containsTyped")
    inline fun <reified T : RenderContext> contains(predicate: (FormSession<T>) -> Boolean): Boolean =
        getSessions().any { it.context is T && predicate(it as FormSession<T>) }

    /** Returns all [FormSession]s that match the given [predicate]. */
    fun filter(predicate: (FormSession<*>) -> Boolean): List<FormSession<*>> =
        sessions.filter(predicate)

    /** Returns all [FormSession]s of type [T] that match the given [predicate]. */
    @JvmName("filterTyped")
    inline fun <reified T : RenderContext> filter(predicate: (FormSession<T>) -> Boolean): List<FormSession<*>> =
        getSessions().filter { it.context is T && predicate(it as FormSession<T>) }

    /** Clears all sessions from the registry. */
    fun clear() {
        sessions.clear()
    }
}
