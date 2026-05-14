package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.LevelChange
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Transform
import ktx.app.gdxError
import ktx.assets.toInternalFile
import ktx.log.logger
import ktx.math.vec2

class LevelChangeSystem : IteratingSystem(
    family = family { all(LevelChange) }
) {
    private val grassRegion = TextureRegion(Texture("graphic/grass.png"))
    private val pathRegion = TextureRegion(Texture("graphic/path.png"))

    override fun onTickEntity(entity: Entity) {
        val (levelFile) = entity[LevelChange]
        entity.remove()

        log.debug { "Loading level '$levelFile'" }

        val fileHandle = "level/$levelFile".toInternalFile()
        val lines = fileHandle.reader().readLines().map { it.trim() }
        if (lines.isEmpty() || lines.all { it.isBlank() }) {
            gdxError("Level $levelFile has no content")
        }

        val mapHeight = lines.size - 1
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                spawnGroundEntity(x, mapHeight - y, char)
            }
        }
    }

    private fun spawnGroundEntity(x: Int, y: Int, groundType: Char) {
        val region = when (groundType) {
            'S', 'F', '#' -> pathRegion
            else -> grassRegion
        }

        world.entity {
            it += Transform(position = vec2(x.toFloat(), y.toFloat()), size = vec2(1f, 1f))
            it += Render(region)
        }
    }

    override fun onDispose() {
        grassRegion.texture.dispose()
        pathRegion.texture.dispose()
    }

    companion object {
        private val log = logger<LevelChangeSystem>()
    }
}