package me.centauri07.promptlin.core.form

/**
 * Entry point for building a [Form] using the DSL.
 *
 * This function creates a new instance of the internal [FormBuilderImpl], applies the [builder] block
 * to register prompts and configure form behavior, and returns the finalized [Form].
 *
 * Example usage:
 * ```
 * val myForm = form {
 *     input(IntInputHandler, "age", "Age", "Enter your age") {
 *         validate("Must be at least 18") { it >= 18 }
 *         includeIf { get("consent") == true }
 *     }
 * }
 * ```
 *
 * @param builder The DSL block that defines the structure and configuration of the [Form].
 * @return The constructed [Form] instance.
 */
fun form(builder: FormBuilder.() -> Unit): Form {
    val impl = FormBuilderImpl()

    impl.builder()

    return impl.build()
}