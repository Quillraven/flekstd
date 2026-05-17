package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.DamageRequest
import io.github.quillraven.flekstd.component.Health

class DamageSystem : IteratingSystem(
    family = family { all(DamageRequest, Health) }
) {

    override fun onTickEntity(entity: Entity) {
        val (requests) = entity[DamageRequest]
        val healthCmp = entity[Health]

        requests.forEach { request ->
            if (request.source.wasRemoved()) {
                return@forEach
            }

            healthCmp.current -= request.amount
        }

        entity.configure { it -= DamageRequest }
    }
}