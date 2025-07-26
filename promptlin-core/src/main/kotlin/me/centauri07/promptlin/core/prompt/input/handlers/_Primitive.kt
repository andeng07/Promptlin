package me.centauri07.promptlin.core.prompt.input.handlers

import me.centauri07.promptlin.core.prompt.input.InputHandler

/**
 * A [InputHandler] that handles [String] input to [String]
 */
open class StringInputHandler : InputHandler<String> {
    companion object : StringInputHandler()
    override fun handle(input: String): Result<String> = Result.success(input)
}

/**
 * A [InputHandler] that handles [String] input to [Int]
 */
open class IntInputHandler : InputHandler<Int> {
    companion object : IntInputHandler()
    override fun handle(input: String): Result<Int> =
        runCatching { input.toInt() }
}

/**
 * A [InputHandler] that handles [String] input to [Long]
 */
open class LongInputHandler : InputHandler<Long> {
    companion object : LongInputHandler()
    override fun handle(input: String): Result<Long> =
        runCatching { input.toLong() }
}

/**
 * A [InputHandler] that handles [String] input to [Double]
 */
open class DoubleInputHandler : InputHandler<Double> {
    companion object : DoubleInputHandler()
    override fun handle(input: String): Result<Double> =
        runCatching { input.toDouble() }
}

/**
 * A [InputHandler] that handles [String] input to [Float]
 */
open class FloatInputHandler : InputHandler<Float> {
    companion object : FloatInputHandler()
    override fun handle(input: String): Result<Float> =
        runCatching { input.toFloat() }
}

/**
 * A [InputHandler] that handles [String] input to [Boolean]
 */
open class BooleanInputHandler(
    private val truthValues: Set<String>,
    private val falseValues: Set<String>
) : InputHandler<Boolean> {

    companion object : BooleanInputHandler(setOf("true", "t"), setOf("false", "f"))

    override fun handle(input: String): Result<Boolean> =
        runCatching {
            val normalized = input.lowercase()
            when (normalized) {
                in truthValues -> true
                in falseValues -> false
                else -> throw IllegalArgumentException("Invalid boolean input: $input")
            }
        }
}

/**
 * A [InputHandler] that handles [String] input to [Short]
 */
open class ShortInputHandler : InputHandler<Short> {
    companion object : ShortInputHandler()
    override fun handle(input: String): Result<Short> =
        runCatching { input.toShort() }
}

/**
 * A [InputHandler] that handles [String] input to [Byte]
 */
open class ByteInputHandler : InputHandler<Byte> {
    companion object : ByteInputHandler()
    override fun handle(input: String): Result<Byte> =
        runCatching { input.toByte() }
}

/**
 * A [InputHandler] that handles [String] input to [Char]
 */
open class CharInputHandler : InputHandler<Char> {
    companion object : CharInputHandler()
    override fun handle(input: String): Result<Char> =
        runCatching {
            if (input.length == 1) input[0]
            else throw IllegalArgumentException("Input must be a single character: $input")
        }
}