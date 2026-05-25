package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import io.github.quillraven.flekstd.component.Perimeter
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.component.Transform
import ktx.graphics.use

class RangeRenderSystem(
    private val shapeRenderer: ShapeRenderer = inject(),
    private val gameViewport: Viewport = inject(),
) : IteratingSystem(
    family = family { all(Tag.CONSTRUCTING, Perimeter, Transform) }
) {
    override fun onTick() {
        gameViewport.apply()
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled, gameViewport.camera) {
            shapeRenderer.color.set(0f, 0.8f, 1f, 1f)
            super.onTick()
        }
    }

    override fun onTickEntity(entity: Entity) {
        val (position, size) = entity[Transform]
        val range = entity[Perimeter].range
        val boxThickness = 0.05f

        val x = position.x - range
        val y = position.y - range
        val w = 2 * range + size.x
        val h = 2 * range + size.y

        shapeRenderer.rect(x, y, w, boxThickness)         // bottom
        shapeRenderer.rect(x, y + h - boxThickness, w, boxThickness) // top
        shapeRenderer.rect(x, y, boxThickness, h)         // left
        shapeRenderer.rect(x + w - boxThickness, y, boxThickness, h) // right
    }
}
