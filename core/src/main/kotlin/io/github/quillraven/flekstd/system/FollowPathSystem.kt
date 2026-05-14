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
        val followPath = entity[FollowPath]
        val (position) = entity[Transform]

        val from = followPath.path[followPath.currentPathIndex - 1]
        val to = followPath.path[followPath.currentPathIndex]

        followPath.alpha += deltaTime * SPEED / from.dst(to)
        position.set(from).lerp(to, followPath.alpha.coerceAtMost(1f))

        if (followPath.alpha >= 1f) {
            followPath.alpha = 0f
            followPath.currentPathIndex++
            if (followPath.currentPathIndex >= followPath.path.size) {
                entity.remove()
            }
        }
    }

    companion object {
        const val SPEED = 3f // 3 world units per second
    }
}
