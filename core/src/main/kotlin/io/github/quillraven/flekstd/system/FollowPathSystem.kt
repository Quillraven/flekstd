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
        val (position) = entity[Transform]

        val from = followPathCmp.path[followPathCmp.currentPathIndex - 1]
        val to = followPathCmp.path[followPathCmp.currentPathIndex]

        followPathCmp.alpha += deltaTime * SPEED / from.dst(to)
        position.set(from).lerp(to, followPathCmp.alpha.coerceAtMost(1f))

        if (followPathCmp.alpha >= 1f) {
            followPathCmp.alpha = 0f
            followPathCmp.currentPathIndex++
            if (followPathCmp.currentPathIndex >= followPathCmp.path.size) {
                entity.remove()
            }
        }
    }

    companion object {
        const val SPEED = 3f // 3 world units per second
    }
}
