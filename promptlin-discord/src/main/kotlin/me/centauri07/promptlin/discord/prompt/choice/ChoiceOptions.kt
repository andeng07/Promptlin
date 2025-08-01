package me.centauri07.promptlin.discord.prompt.choice

import me.centauri07.promptlin.core.prompt.choice.Option

class SelectOption(value: String, label: String, description: String, emoji: String) :
    Option(value, label, description) {
    override fun toString(): String = label
}

class ButtonOption(value: String, label: String, description: String, val emoji: String? = null, val style: Int = 1) :
    Option(value, label, description) {
    override fun toString(): String = label
}