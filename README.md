# Promptlin

---
Promptlin is a lightweight Kotlin framework for building interactive forms that work across various platforms like the
command line, Discord, or even the web. It cleanly separates form definition, rendering, and platform-specific behavior,
making your codebase modular and reusable.

## Supported Platforms

---

## Dependencies

---

### Promptlin Modules

`Promptlin` consists of the following modules:

- promptlin-core - Contains the base DSL, form engine, input handling, and validation system.

## Examples

---

### Defining a form

Here’s a simple form that asks for a user's name, age, and (optionally) their favorite alcohol:

```kotlin
val form = form {
    val name = input(StringInputHandler, "name", "Name", "Your name") {
        validate("Please enter a valid name (must start with a capital letter)") {
            it.isNotEmpty() && it[0].isUpperCase()
        }
    }

    val age = input(IntInputHandler, "age", "Age", "Your age") {
        validate("Age must be between 1 and 99") {
            it in 1..99
        }
    }

    val alcohol = input(StringInputHandler, "alcohol", "Alcohol", "Your favorite alcohol") {
        includeIf { get(age) >= 18 } // Only shown if user is 18 or older
    }
}
```

### Configuring a platform

Promptlin allows you to configure platforms through `Promptlin.configure { ... }`. A platform connects your forms to a
specific environment like a console, Discord bot, or web UI. You can either register an existing platform, customize it,
or create one from scratch.

#### a. Register an existing platform instance

If you already have a platform implementation (e.g., ConsoleHandler), you can register it directly:

```kotlin
Promptlin.configure {
    platform(ConsoleHandler()) // Registers an existing platform instance
}
```

#### b. Register and override behavior of an existing platform

You can also extend and override the behavior of an existing platform — for example, customizing how certain prompt
types are rendered:

```kotlin
Promptlin.configure {
    platform(ConsoleHandler()) {
        renderer {
            bind<InputPrompt<*>> {
                onInvoke { ctx, prompt ->
                    ctx.sendMessage("Please enter ${prompt.name}: ")
                    attemptSet(ctx.awaitMessage())
                }

                onFailure { ctx, prompt, e ->
                    ctx.sendMessage("An error occurred: ${e::class.simpleName}: ${e.message}")
                }
            }
        }
    }
}
```

#### c. Define a new platform from scratch
```kotlin
Promptlin.configure {
    platform<ConsoleContext> {
        renderer {
            bind<InputPrompt<String>> {
                onInvoke { ctx, prompt ->
                    ctx.sendMessage("Enter ${prompt.name}:")
                    attemptSet(ctx.awaitMessage())
                }

                onFailure { ctx, prompt, e ->
                    ctx.sendMessage("Error: ${e.message}")
                }
            }
        }

        initialize {
            sendMessage("Console platform initialized.")
        }
    }
}
```

### Running a form using a specific context

Now that everything is configured, you can run the form using your context:

```kotlin
fun main() {
    val context = ConsoleContext(Scanner(System.`in`), System.out)

    form.start(context) {
        val name = get<String>("name")
        val age = get<Int>("age")
        val alcohol = getOrElse("alcohol") { "None (not old enough)" }

        sendMessage("Hello, $name! You are $age years old. Favorite drink: $alcohol.")
    }
}
```

### Creating your own platform

You can extend Promptlin to work with other environments by implementing a custom RenderContext and renderer.

```kotlin
class MyCustomContext : RenderContext {
    // Your platform-specific logic here
}

val myRenderer = renderer<MyCustomContext> {
    bind<InputPrompt<String>> {
        onInvoke { ctx, prompt ->
            ctx.sendMessage("Enter ${prompt.name}:")
            attemptSet(ctx.awaitMessage())
        }

        onFailure { ctx, prompt, e ->
            ctx.sendMessage("Error: ${e.message}")
        }
    }
}

val myPlatform = platform<MyCustomContext> {
    renderer(myRenderer)
}
```