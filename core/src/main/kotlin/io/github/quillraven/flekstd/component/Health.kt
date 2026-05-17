package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Health(
    val base: Float,
    val regeneration: Float,
) : Component<Health> {
    var current: Float = base

    override fun type() = Health

    companion object : ComponentType<Health>()
}