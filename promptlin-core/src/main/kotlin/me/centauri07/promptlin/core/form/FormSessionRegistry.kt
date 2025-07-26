package me.centauri07.promptlin.core.form

/**
 * A global registry for managing all active [FormSession] instances.
 *
 * This registry provides utilities to register, unregister, query, and clear form sessions
 * during the application's runtime. It enables tracking of currently running forms and
 * performing operations on them (e.g., searching, filtering, or bulk removal).
 */
object FormSessionRegistry {
    private val sessions = mutableSetOf<FormSession<*>>()

    /**
     * Registers a new [FormSession] into the registry.
     *
     * @param session The form session to register.
     */
    fun register(session: FormSession<*>) {
        sessions += session
    }

    /**
     * Unregisters an existing [FormSession] from the registry.
     *
     * @param session The form session to unregister.
     */
    fun unregister(session: FormSession<*>) {
        sessions -= session
    }

    /**
     * Finds and unregisters the first [FormSession] that matches the given [predicate].
     *
     * @param predicate A lambda to determine which session to remove.
     */
    fun unregister(predicate: (FormSession<*>) -> Boolean) {
        filter(predicate).firstOrNull()?.also { unregister(it) }
    }

    /**
     * Returns a list of all currently registered [FormSession]s.
     *
     * @return A list of form sessions.
     */
    fun all(): List<FormSession<*>> = sessions.toList()

    /**
     * Clears all sessions from the registry.
     */
    fun clear() {
        sessions.clear()
    }

    /**
     * Returns whether any session in the registry matches the given [predicate].
     *
     * @param predicate A lambda used to match sessions.
     * @return `true` if any session matches; `false` otherwise.
     */
    fun contains(predicate: (FormSession<*>) -> Boolean): Boolean {
        return sessions.any(predicate)
    }

    /**
     * Returns all [FormSession]s that match the given [predicate].
     *
     * @param predicate A lambda used to filter sessions.
     * @return A list of matching form sessions.
     */
    fun filter(predicate: (FormSession<*>) -> Boolean): List<FormSession<*>> {
        return sessions.filter(predicate)
    }
}