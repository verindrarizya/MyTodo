package com.verindrzya.mytodo.constant

enum class PriorityLevel {
    High, Medium, Low, All
}

object PriorityLevelHelper {
    val filterPriorityLevel = PriorityLevel.values()
        .map { it.name }
}