package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class LevelChangeRequest(
    val levelFile: String,
) : Component<LevelChangeRequest> {
    override fun type() = LevelChangeRequest

    companion object : ComponentType<LevelChangeRequest>()
}