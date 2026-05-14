package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class LevelChange(
    val levelFile: String,
) : Component<LevelChange> {
    override fun type() = LevelChange

    companion object : ComponentType<LevelChange>()
}