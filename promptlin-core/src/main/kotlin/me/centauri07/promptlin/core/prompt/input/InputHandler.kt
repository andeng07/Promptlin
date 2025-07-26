package me.centauri07.promptlin.core.prompt.input

/**
 * Defines a contract for parsing a [String] input into a value of type [T].
 *
 * Implementations of this interface are responsible for validating and converting
 * raw text input into strongly typed values, returning a [Result] to indicate success or failure.
 *
 * @param T The target type to which the input will be converted.
 */
interface InputHandler<T> {
    /**
     * Attempts to parse the given [input] string into a value of type [T].
     *
     * @param input The raw string input to parse.
     * @return A [Result] containing the parsed value on success, or an error on failure.
     */
    fun handle(input: String): Result<T>
}