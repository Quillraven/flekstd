package io.github.quillraven.flekstd.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Transform(
    val position: Vector2,
    val size: Vector2,
) : Component<Transform>, Comparable<Transform> {
    override fun type() = Transform

    override fun compareTo(other: Transform): Int {
        val yCmp = other.position.y.compareTo(this.position.y)
        if (yCmp != 0) return yCmp
        return this.position.x.compareTo(other.position.x)
    }

    companion object : ComponentType<Transform>()
}