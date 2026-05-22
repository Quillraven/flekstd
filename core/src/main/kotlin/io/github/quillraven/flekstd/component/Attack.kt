package io.github.quillraven.flekstd.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import io.github.quillraven.flekstd.cfg.ProjectileCfg

data class Attack(
    val cooldown: Float,
    val damage: Float,
    val projectileCfg: ProjectileCfg,
    val projectileDelay: Float,
    val projectileOffset: Vector2,
) : Component<Attack> {
    var timer = 0f

    override fun type() = Attack

    companion object : ComponentType<Attack>()
}