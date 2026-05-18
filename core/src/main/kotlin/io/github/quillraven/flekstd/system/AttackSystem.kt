package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.Attack
import io.github.quillraven.flekstd.component.DamageInfo
import io.github.quillraven.flekstd.component.DamageRequest
import io.github.quillraven.flekstd.component.Perimeter
import io.github.quillraven.flekstd.component.Tag
import ktx.collections.gdxArrayOf

class AttackSystem : IteratingSystem(
    family = family { all(Attack, Perimeter).none(Tag.CONSTRUCTING) }
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

        // tick pending damage
        if (attackCmp.damageTimer > 0f) {
            attackCmp.damageTimer -= deltaTime
            if (attackCmp.damageTimer <= 0f) {
                val target = entity[Perimeter].target
                if (!target.wasRemoved()) {
                    target.configure {
                        val damageRequestCmp = it.getOrAdd(DamageRequest) { DamageRequest(gdxArrayOf()) }
                        damageRequestCmp.requests.add(DamageInfo(entity, attackCmp.damage))
                    }
                }
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

        // start attack: schedule damage after delay
        animationCmp?.changeTo(AnimationType.ATTACK, PlayMode.NORMAL)
        attackCmp.timer = attackCmp.cooldown
        attackCmp.damageTimer = attackCmp.damageDelay
    }
}