package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.LevelChangeRequest

class RequestCleanupSystem : IteratingSystem(
    family = family { any(LevelChangeRequest) }
) {
    override fun onTickEntity(entity: Entity) {
        entity.remove()
    }
}