package io.github.quillraven.flekstd.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.cfg.animationMapOf
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.Health
import io.github.quillraven.flekstd.component.Remove
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Transform
import ktx.assets.toInternalFile

class HealthSystem : IteratingSystem(
    family = family { all(Transform, Render, Health) }
) {
    private val dustAnimations = animationMapOf("dust", AnimationType.IDLE)
    private val deathSnd = Gdx.audio.newSound("sound/death.wav".toInternalFile())

    override fun onTickEntity(entity: Entity) {
        val healthCmp = entity[Health]

        if (healthCmp.current <= 0f) {
            // entity has no life left -> remove it and play dust effect
            world.entity {
                val transformCmp = entity[Transform]
                it += Transform(transformCmp.position.cpy(), transformCmp.size.cpy(), Transform.Z_PROJECTILE)
                it += Render(Render.EMPTY_REGION, entity[Render].scale * 0.5f)
                it += Animation(AnimationType.IDLE, dustAnimations, PlayMode.NORMAL)
                it += Remove(dustAnimations[AnimationType.IDLE]?.animationDuration ?: 0f)
            }
            deathSnd.play()
            entity.remove()
            return
        }

        healthCmp.current = (healthCmp.current + healthCmp.regeneration * deltaTime).coerceAtMost(healthCmp.base)
    }

    override fun onDispose() {
        dustAnimations.values.forEach {
            it.keyFrames.first().texture.dispose()
        }
        deathSnd.dispose()
    }
}
