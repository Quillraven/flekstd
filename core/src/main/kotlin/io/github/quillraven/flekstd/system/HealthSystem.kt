package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Health

class HealthSystem : IteratingSystem(
    family = family { all(Health) }
) {
    override fun onTickEntity(entity: Entity) {
        val healthCmp = entity[Health]

        if (healthCmp.current <= 0f) {
            // entity has no life left -> remove it
            entity.remove()
            return
        }

        healthCmp.current = (healthCmp.current + healthCmp.regeneration * deltaTime).coerceAtMost(healthCmp.base)
    }
}
