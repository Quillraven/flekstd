package io.github.quillraven.flekstd.system

import com.badlogic.gdx.math.MathUtils
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityRef.Companion.isValid
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Homing
import io.github.quillraven.flekstd.component.Projectile
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Speed
import io.github.quillraven.flekstd.component.Transform

class HomingMoveSystem : IteratingSystem(
    family = family { all(Homing, Speed, Transform) }
) {
    override fun onTickEntity(entity: Entity) {
        val (target, targetPosition, onReached) = entity[Homing]
        val speed = entity[Speed].current

        // update target position if target is still alive
        if (target.isValid()) {
            targetPosition.set(target[Transform].position)
        }

        // move entity towards targetPosition
        val position = entity[Transform].position
        val dist = targetPosition.dst(position)

        val moveDist = speed * deltaTime
        if (MathUtils.isEqual(dist, 0f, 0.05f) || dist <= moveDist) {
            // reached target destination -> fire 'onReached' action
            onReached()
            return
        }

        // move towards target position by normalizing the direction vector (dx/dist, dy/dist) and scale by speed
        val dx = targetPosition.x - position.x
        val dy = targetPosition.y - position.y
        position.add(dx / dist * moveDist, dy / dist * moveDist)

        // flip rendering if necessary
        if (entity.getOrNull(Projectile)?.flipX == true) {
            entity.getOrNull(Render)?.flipX = dx < 0f
        }
    }
}
