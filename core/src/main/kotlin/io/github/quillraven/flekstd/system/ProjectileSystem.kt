package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.DamageInfo
import io.github.quillraven.flekstd.component.DamageRequest
import io.github.quillraven.flekstd.component.Homing
import io.github.quillraven.flekstd.component.Projectile
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Transform
import ktx.collections.gdxArrayOf

class ProjectileSystem : IteratingSystem(
    family = family { all(Transform, Projectile) }
) {
    override fun onTickEntity(entity: Entity) {
        val projectileCmp = entity[Projectile]

        if (projectileCmp.delay > 0f) {
            projectileCmp.delay -= deltaTime
            if (projectileCmp.delay <= 0f) {
                activateProjectile(entity, projectileCmp)
            }
            return
        }
    }

    private fun activateProjectile(projectile: Entity, projectileCmp: Projectile) {
        projectile.configure {
            if (projectile has Animation) {
                // make the projectile visible now by adding a Render component to it
                it += Render(Render.EMPTY_REGION, projectileCmp.scale)
            }
            it += Homing(projectileCmp.target, projectileCmp.originalTargetPosition) {
                // onReached lambda
                if (!projectileCmp.target.wasRemoved()) {
                    // target still alive -> damage it
                    projectileCmp.target.configure { target ->
                        val damageRequestCmp = target.getOrAdd(DamageRequest) { DamageRequest(gdxArrayOf()) }
                        damageRequestCmp.requests.add(DamageInfo(projectileCmp.source, projectileCmp.damage))
                    }
                }

                projectile.remove()
            }
        }
        projectileCmp.spawnSnd.play()
    }
}