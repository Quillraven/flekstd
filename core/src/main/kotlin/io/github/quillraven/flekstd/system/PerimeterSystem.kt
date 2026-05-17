package io.github.quillraven.flekstd.system

import com.badlogic.gdx.math.Rectangle
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Perimeter
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.component.Transform

class PerimeterSystem : IteratingSystem(
    family = family { all(Perimeter, Transform) },
    interval = Fixed(1 / 10f),
) {
    private val enemies = family { all(Tag.ENEMY, Transform) }
    private val perimeterRect = Rectangle()

    override fun onTickEntity(entity: Entity) {
        val perimeterCmp = entity[Perimeter]
        val (position, size) = entity[Transform]

        perimeterRect.set(
            position.x - perimeterCmp.range, position.y - perimeterCmp.range,
            2 * perimeterCmp.range + size.x, 2 * perimeterCmp.range + size.y
        )

        if (!perimeterCmp.target.wasRemoved()) {
            // valid target -> check if it is out of range
            val (targetPos, targetSize) = perimeterCmp.target[Transform]
            val targetCenterX = targetPos.x + targetSize.x * 0.5f
            val targetCenterY = targetPos.y + targetSize.y * 0.5f
            if (perimeterRect.contains(targetCenterX, targetCenterY)) {
                // target still in range -> do nothing
                return
            }
        }

        // target is out of range or no target set yet -> find a new target
        perimeterCmp.target = enemies.firstOrNull { enemy ->
            val (enemyPos, enemySize) = enemy[Transform]
            val enemyCenterX = enemyPos.x + enemySize.x * 0.5f
            val enemyCenterY = enemyPos.y + enemySize.y * 0.5f
            perimeterRect.contains(enemyCenterX, enemyCenterY)
        } ?: Entity.NONE
    }
}