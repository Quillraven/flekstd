package io.github.quillraven.flekstd.component

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Projectile(
    val source: Entity,
    val target: Entity,
    val originalTargetPosition: Vector2,
    val damage: Float,
    var delay: Float,
    val scale: Float,
    val flipX: Boolean,
    val spawnSnd: Sound,
) : Component<Projectile> {
    override fun type() = Projectile

    companion object : ComponentType<Projectile>()
}