package io.github.quillraven.flekstd.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Homing(
    val target: Entity,
    val targetPosition: Vector2,
    val onReached: () -> Unit,
) : Component<Homing> {
    override fun type() = Homing

    companion object : ComponentType<Homing>()
}