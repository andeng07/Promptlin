package me.centauri07.promptlin.console

import me.centauri07.promptlin.core.renderer.RenderContext
import java.io.PrintStream
import java.util.Scanner

/**
 * A basic [RenderContext] implementation for console-based interactions.
 *
 * This class provides an abstraction for input/output operations in a console environment
 * using a [Scanner] for reading user input and a [PrintStream] for displaying output.
 *
 * @property scanner The [Scanner] instance used to read input from the console.
 * @property outputStream The [PrintStream] instance used to write output to the console.
 */
class ConsoleContext(val scanner: Scanner, val outputStream: PrintStream) : RenderContext {
    /**
     * Suspends execution until user input is received from the console.
     *
     * @return The line of input entered by the user.
     */
    fun awaitInput(): String = scanner.nextLine()

    /**
     * Sends a line of output to the console.
     *
     * @param output The message or response to display.
     */
    fun sendOutput(output: String) = outputStream.println(output)
}