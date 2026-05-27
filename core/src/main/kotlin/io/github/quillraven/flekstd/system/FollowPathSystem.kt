package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.FollowPath
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Speed
import io.github.quillraven.flekstd.component.Transform

class FollowPathSystem : IteratingSystem(
    family = family { all(FollowPath, Transform, Speed, Render) }
) {
    override fun onTickEntity(entity: Entity) {
        val followPathCmp = entity[FollowPath]
        val (position) = entity[Transform]
        val speed = entity[Speed].current

        val from = followPathCmp.path[followPathCmp.currentPathIndex - 1]
        val to = followPathCmp.path[followPathCmp.currentPathIndex]

        if (followPathCmp.alpha == 0f) {
            entity[Render].flipX = to.x < from.x
        }

        followPathCmp.alpha += deltaTime * speed / from.dst(to)
        position.set(from).lerp(to, followPathCmp.alpha.coerceAtMost(1f))

        if (followPathCmp.alpha >= 1f) {
            followPathCmp.alpha = 0f
            followPathCmp.currentPathIndex++
            if (followPathCmp.currentPathIndex >= followPathCmp.path.size) {
                entity.remove()
            }
        }
    }
}
