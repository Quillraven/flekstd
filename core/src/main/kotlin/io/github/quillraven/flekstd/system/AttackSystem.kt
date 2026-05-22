package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.Attack
import io.github.quillraven.flekstd.component.Perimeter
import io.github.quillraven.flekstd.component.Projectile
import io.github.quillraven.flekstd.component.Speed
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.component.Transform
import ktx.math.vec2

class AttackSystem : IteratingSystem(
    family = family { all(Attack, Perimeter, Transform).none(Tag.CONSTRUCTING) }
) {
    override fun onTickEntity(entity: Entity) {
        val attackCmp = entity[Attack]
        val animationCmp = entity.getOrNull(Animation)

        // tick cooldown and animation
        if (attackCmp.timer > 0f) {
            attackCmp.timer -= deltaTime
            if (animationCmp?.currentType == AnimationType.ATTACK && animationCmp.isFinished()) {
                animationCmp.changeTo(AnimationType.IDLE, PlayMode.LOOP)
            }
        }

        if (attackCmp.timer > 0f) return

        // attack ready -> check if there is a target
        val target = entity[Perimeter].target
        if (target.wasRemoved()) {
            if (animationCmp?.currentType != AnimationType.IDLE) {
                animationCmp?.changeTo(AnimationType.IDLE, PlayMode.LOOP)
            }
            return
        }

        // start attack animation and spawn projectile for damage
        animationCmp?.changeTo(AnimationType.ATTACK, PlayMode.NORMAL)
        attackCmp.timer = attackCmp.cooldown

        // spawn projectile entity
        spawnProjectile(attackCmp, entity, target)
    }

    private fun spawnProjectile(
        attackCmp: Attack,
        tower: Entity,
        target: Entity
    ) {
        val projectileCfg = attackCmp.projectileCfg
        world.entity {
            it += Transform(
                position = tower[Transform].position.cpy().add(attackCmp.projectileOffset),
                size = vec2(1f, 1f),
                z = Transform.Z_PROJECTILE
            )
            it += Speed(projectileCfg.speed)
            it += Projectile(
                source = tower,
                target = target,
                originalTargetPosition = target[Transform].position.cpy(),
                attackCmp.damage,
                attackCmp.projectileDelay,
                projectileCfg.scale,
                projectileCfg.flipX,
            )
            if (projectileCfg.scale > 0f) {
                // visible projectile
                it += Animation(AnimationType.IDLE, projectileCfg.gdxAnimations)
            }
        }
    }
}