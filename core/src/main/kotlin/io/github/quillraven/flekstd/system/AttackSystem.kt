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
import ktx.collections.gdxArrayOf

class AttackSystem : IteratingSystem(
    family = family { all(Attack, Perimeter) }
) {
    override fun onTickEntity(entity: Entity) {
        val attackCmp = entity[Attack]
        val animationCmp = entity.getOrNull(Animation)

        if (attackCmp.timer > 0f) {
            // attack on cooldown
            attackCmp.timer -= deltaTime
            if (animationCmp?.currentType == AnimationType.ATTACK && animationCmp.isFinished()) {
                animationCmp.changeTo(AnimationType.IDLE, PlayMode.LOOP)
            }
            return
        }

        // attack ready -> check if there is a target
        val target = entity[Perimeter].target
        if (target.wasRemoved()) {
            // target already removed (e.g. killed by a tower) or there is no target
            animationCmp?.changeTo(AnimationType.IDLE, PlayMode.LOOP)
            return
        }

        // damage target
        animationCmp?.changeTo(AnimationType.ATTACK, PlayMode.NORMAL)
        attackCmp.timer = attackCmp.cooldown
        target.configure {
            val damageRequestCmp = it.getOrAdd(DamageRequest) { DamageRequest(gdxArrayOf()) }
            damageRequestCmp.requests.add(DamageInfo(entity, attackCmp.damage))
        }

    }
}