package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Attack(
    val cooldown: Float,
    val damage: Float,
    val damageDelay: Float,
) : Component<Attack> {
    var timer = 0f
    var damageTimer = 0f

    override fun type() = Attack

    companion object : ComponentType<Attack>()
}