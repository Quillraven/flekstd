package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.DamageRequest
import io.github.quillraven.flekstd.component.Health
import io.github.quillraven.flekstd.component.SlowEffect
import io.github.quillraven.flekstd.component.SplashEffect
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.component.Transform

class DamageSystem : IteratingSystem(
    family = family { all(DamageRequest, Health) }
) {
    private val enemyFamily = family { all(Transform, Health, Tag.ENEMY) }

    override fun onTickEntity(entity: Entity) {
        val (requests) = entity[DamageRequest]
        val healthCmp = entity[Health]

        requests.forEach { request ->
            val tower = request.source
            if (tower.wasRemoved()) return@forEach

            healthCmp.current -= request.amount
            applyDamageEffects(tower, entity)
            applySplash(tower, entity, request.amount)
        }

        entity.configure { it -= DamageRequest }
    }

    private fun applyDamageEffects(tower: Entity, target: Entity) {
        applySlowEffect(tower, target)
        // future DamageEffects (except SplashEffect itself) go here
    }

    private fun applySplash(tower: Entity, primaryTarget: Entity, damage: Float) {
        val splashCmp = tower.getOrNull(SplashEffect) ?: return
        val splashDamage = damage * splashCmp.percentage
        val targetPos = primaryTarget[Transform].position

        enemyFamily.forEach { enemy ->
            if (enemy == primaryTarget) return@forEach
            if (enemy[Transform].position.dst(targetPos) <= splashCmp.radius) {
                enemy[Health].current -= splashDamage
                if (splashCmp.applyEffects) {
                    applyDamageEffects(tower, enemy)
                }
            }
        }
    }

    private fun applySlowEffect(tower: Entity, target: Entity) {
        val towerSlowCmp = tower.getOrNull(SlowEffect) ?: return
        val targetSlowCmp = target.getOrNull(SlowEffect)

        if (targetSlowCmp != null) {
            if (towerSlowCmp.percentage > targetSlowCmp.percentage) {
                // new effect has higher slow percentage -> apply it instead
                targetSlowCmp.percentage = towerSlowCmp.percentage
                targetSlowCmp.duration = towerSlowCmp.duration
            } else if (towerSlowCmp.percentage == targetSlowCmp.percentage) {
                // new effect has same slow percentage -> restore duration
                targetSlowCmp.duration = maxOf(targetSlowCmp.duration, towerSlowCmp.duration)
            }
            return
        }

        // no slow effect yet on target -> add it
        target.configure {
            target += SlowEffect(towerSlowCmp.percentage, towerSlowCmp.duration)
        }
    }
}
