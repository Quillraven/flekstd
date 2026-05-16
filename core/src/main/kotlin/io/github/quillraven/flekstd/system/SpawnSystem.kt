package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.ObjectMap
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.FollowPath
import io.github.quillraven.flekstd.component.GdxAnimation
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Spawn
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.component.Transform
import io.github.quillraven.flekstd.component.Transform.Companion.Z_OBJECT
import ktx.collections.GdxArray
import ktx.collections.getOrPut
import ktx.collections.set
import ktx.collections.toGdxArray
import ktx.log.logger
import ktx.math.vec2

class SpawnSystem : IteratingSystem(
    family = family { all(Spawn, Tag.SPAWNING) }
) {
    private val disposableTextures: ObjectMap<String, Texture> = ObjectMap()
    private val animationMap: ObjectMap<String, GdxAnimation> = ObjectMap()

    init {
        animationMap["pawn"] = GdxAnimation(1 / 10f, regions("pawn.png", 66, 77))
        animationMap["lancer"] = GdxAnimation(1 / 10f, regions("lancer.png", 70, 138))
    }

    private fun regions(fileName: String, tileWidth: Int, tileHeight: Int): GdxArray<TextureRegion> {
        val filePath = "graphic/$fileName"
        val sheet = disposableTextures.getOrPut(filePath) { Texture(filePath) }
        val regions = TextureRegion.split(sheet, tileWidth, tileHeight)
        return regions.flatten().toGdxArray()
    }

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
        val gdxAnimation = animationMap[currentWaveInfo.type]
        val start = spawnCmp.path.first()
        world.entity {
            it += Transform(position = start.cpy(), size = vec2(1f, 1f), z = Z_OBJECT)
            it += Render(gdxAnimation.keyFrames.first())
            it += Animation(gdxAnimation)
            it += FollowPath(spawnCmp.path)
        }
        // TODO move animation cache outside of this system
        // TODO maybe use DisposableRegistry for the textures?
    }

    override fun onDispose() {
        disposableTextures.values().forEach { it.dispose() }
    }

    companion object {
        private val log = logger<SpawnSystem>()
    }
}