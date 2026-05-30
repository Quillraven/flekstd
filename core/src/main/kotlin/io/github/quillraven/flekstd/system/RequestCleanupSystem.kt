package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.LevelChangeRequest

// Removes all "request" entities at the end of each frame.
// Uses 'any' so that adding new request component types here automatically covers them
// without needing to change the family filter.
class RequestCleanupSystem : IteratingSystem(
    family = family { any(LevelChangeRequest) }
) {
    override fun onTickEntity(entity: Entity) {
        entity.remove()
    }
}