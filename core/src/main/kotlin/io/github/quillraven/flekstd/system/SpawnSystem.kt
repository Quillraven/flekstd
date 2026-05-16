package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.FollowPath
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Spawn
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.component.Transform
import io.github.quillraven.flekstd.component.Transform.Companion.Z_OBJECT
import ktx.log.logger
import ktx.math.vec2

class SpawnSystem : IteratingSystem(
    family = family { all(Spawn, Tag.SPAWNING) }
) {
    override fun onTickEntity(entity: Entity) {
        val spawnCmp = entity[Spawn]
        val currentWaveInfo = spawnCmp.currentWaveInfo
        if (spawnCmp.numSpawns >= spawnCmp.currentWaveInfo.amount) {
            // all entities of this wave have been spawned -> wait for next wave
            spawnCmp.nextWave()
            log.debug { "Wave ${spawnCmp.waveIdx} of ${spawnCmp.wavesInfo.size} finished" }
            if (spawnCmp.waveIdx >= spawnCmp.wavesInfo.size) {
                // all waves have been spawned -> remove spawning entity
                log.debug { "All waves finished" }
                entity.remove()
            }
            entity.configure { it -= Tag.SPAWNING }
            return
        }

        // update timer and wave index
        spawnCmp.timer += deltaTime
        if (spawnCmp.timer < currentWaveInfo.interval) {
            // not enough time has passed yet -> do nothing
            return
        }
        spawnCmp.timer = 0f
        spawnCmp.numSpawns++

        // spawn a new entity
        val start = spawnCmp.path.first()
        world.entity {
            it += Transform(position = start.cpy(), size = vec2(1f, 1f), z = Z_OBJECT)
            it += Render(Render.EMPTY_REGION)
            it += Animation(currentWaveInfo.type)
            it += FollowPath(spawnCmp.path)
        }
    }

    companion object {
        private val log = logger<SpawnSystem>()
    }
}