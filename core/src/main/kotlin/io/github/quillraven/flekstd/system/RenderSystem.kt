package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Transform
import ktx.graphics.use

class RenderSystem(
    private val batch: Batch = inject(),
    private val gameViewport: Viewport = inject(),
) : IteratingSystem(
    family = family { all(Transform, Render) },
    comparator = compareEntityBy(Transform),
) {
    private val camera: OrthographicCamera = gameViewport.camera as OrthographicCamera

    override fun onTick() {
        gameViewport.apply()
        batch.use(camera) {
            super.onTick() // renders all entities
        }
        batch.color = Color.WHITE
    }

    override fun onTickEntity(entity: Entity) {
        val (region, regionSize, color) = entity[Render]
        val (position, size) = entity[Transform]

        // scale texture inside transform size by keeping aspect ratio
        val realSize = Scaling.fill.apply(regionSize.x, regionSize.y, size.x, size.y)

        batch.color = color
        batch.draw(region, position.x, position.y, realSize.x, realSize.y)
    }
}