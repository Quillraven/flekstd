package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Remove

class RemoveSystem : IteratingSystem(
    family = family { all(Remove) }
) {
    override fun onTickEntity(entity: Entity) {
        val removeCmp = entity[Remove]

        if (removeCmp.delay <= 0f) {
            entity.remove()
            return
        }

        removeCmp.delay -= deltaTime
    }
}