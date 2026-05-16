package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.LevelChangeRequest
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Spawn
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.component.Transform
import io.github.quillraven.flekstd.component.Transform.Companion.Z_GROUND
import io.github.quillraven.flekstd.component.WaveInfo
import ktx.app.gdxError
import ktx.assets.toInternalFile
import ktx.collections.gdxArrayOf
import ktx.log.logger
import ktx.math.vec2

class LevelChangeSystem : IteratingSystem(
    family = family { all(LevelChangeRequest) }
) {
    private val grassRegion = TextureRegion(Texture("graphic/grass.png"))
    private val pathRegion = TextureRegion(Texture("graphic/path.png"))

    override fun onTickEntity(entity: Entity) {
        val (levelFile) = entity[LevelChangeRequest]

        // load file content
        log.debug { "Loading level '$levelFile'" }
        val fileHandle = "level/$levelFile".toInternalFile()
        val lines = fileHandle.reader().readLines().map { it.trim() }
        if (lines.isEmpty() || lines.all { it.isBlank() }) {
            gdxError("Level $levelFile has no content")
        }

        // parse wave + spawn interval info
        createSpawnEntity(lines)
        // create map by spawning ground entities
        createGroundEntities(lines)
    }

    private fun createGroundEntities(lines: List<String>) {
        val linesToSkip = 4 // waveInfo, spawnTime, path, ...
        val mapHeight = lines.size - linesToSkip - 1
        lines.forEachIndexed { y, line ->
            if (y < linesToSkip) return@forEachIndexed

            line.forEachIndexed { x, char ->
                spawnGroundEntity(x, mapHeight - y + linesToSkip, char)
            }
        }
    }

    private fun createSpawnEntity(lines: List<String>) {
        val waveInfoLine = lines[0]
        val spawnTimeLine = lines[1]
        val pathLine = lines[2]
        val startLine = lines[3]

        val wavesSplits = waveInfoLine.substringAfter("waves=").split(",")
        val spawnTimeSplits = spawnTimeLine.substringAfter("spawnTime=").split(",")
        if (wavesSplits.size != spawnTimeSplits.size) {
            gdxError("'waves' (${wavesSplits.size} and 'spawnTime' (${spawnTimeSplits.size} info must have the same number of entries")
        }

        // parse wave info
        val wavesInfo = gdxArrayOf<WaveInfo>()
        repeat(spawnTimeSplits.size) { idx ->
            val (numEntities, entityType) = wavesSplits[idx].split(":")
            val time = spawnTimeSplits[idx].trim().toFloat()
            wavesInfo.add(WaveInfo(numEntities.trim().toInt(), entityType.trim(), time))
        }
        // parse start
        val (startX, startY) = startLine.substringAfter("start=").split(",").map { it.trim() }
        val start = vec2(startX.toFloat(), startY.toFloat())
        // parse path info
        val pathSplit = pathLine.substringAfter("path=").split(",").map { it.trim() }
        val path = gdxArrayOf<Vector2>(start.cpy())
        val waypoint = start.cpy()
        var currentDirection = pathSplit.first()
        for ((index, direction) in pathSplit.withIndex()) {
            if (direction != currentDirection) {
                path.add(waypoint.cpy())
                currentDirection = direction
            }

            when (direction) {
                "r" -> waypoint.x++
                "l" -> waypoint.x--
                "u" -> waypoint.y++
                "d" -> waypoint.y--
            }

            if (index == pathSplit.lastIndex) {
                path.add(waypoint.cpy())
            }
        }

        // create spawn entity
        world.entity {
            it += Spawn(path, wavesInfo)
        }
    }

    private fun spawnGroundEntity(x: Int, y: Int, groundType: Char) {
        world.entity {
            it += Transform(position = vec2(x.toFloat(), y.toFloat()), size = vec2(1f, 1f), z = Z_GROUND)
            if (groundType == 'S' || groundType == '#') {
                // path
                it += Render(pathRegion)
                it += Tag.PATH
            } else {
                // ground
                it += Render(grassRegion)
            }
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