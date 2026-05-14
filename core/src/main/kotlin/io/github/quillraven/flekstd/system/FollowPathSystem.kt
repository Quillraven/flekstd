package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.FollowPath
import io.github.quillraven.flekstd.component.Transform

class FollowPathSystem : IteratingSystem(
    family = family { all(FollowPath, Transform) }
) {
    override fun onTickEntity(entity: Entity) {
        val followPathCmp = entity[FollowPath]
    }
}