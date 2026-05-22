package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.DamageRequest
import io.github.quillraven.flekstd.component.Health
import io.github.quillraven.flekstd.component.SlowEffect

class DamageSystem : IteratingSystem(
    family = family { all(DamageRequest, Health) }
) {

    override fun onTickEntity(entity: Entity) {
        val (requests) = entity[DamageRequest]
        val healthCmp = entity[Health]

        requests.forEach { request ->
            val tower = request.source
            if (tower.wasRemoved()) {
                return@forEach
            }

            // reduce target health
            healthCmp.current -= request.amount
            // apply special DamageEffect effects of the tower like slow or splash
            applySlowEffect(tower, entity)
        }

        entity.configure { it -= DamageRequest }
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
