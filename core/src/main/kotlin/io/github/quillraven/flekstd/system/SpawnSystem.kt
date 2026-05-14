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
import io.github.quillraven.flekstd.component.Transform
import io.github.quillraven.flekstd.component.Transform.Companion.Z_OBJECT
import ktx.collections.GdxArray
import ktx.collections.getOrPut
import ktx.collections.set
import ktx.collections.toGdxArray
import ktx.math.vec2

class SpawnSystem : IteratingSystem(
    family = family { all(Spawn) }
) {
    private var timer = 0f
    private var waveIdx = 0
    private val disposableTextures: ObjectMap<String, Texture> = ObjectMap()
    private val animationMap: ObjectMap<String, GdxAnimation> = ObjectMap()

    init {
        animationMap["pawn"] = GdxAnimation(1 / 10f, regions("pawn.png"))
    }

    private fun regions(fileName: String): GdxArray<TextureRegion> {
        val filePath = "graphic/$fileName"
        val sheet = disposableTextures.getOrPut(filePath) { Texture(filePath) }
        val regions = TextureRegion.split(sheet, 66, 77)
        return regions.flatten().toGdxArray()
    }

    override fun onTickEntity(entity: Entity) {
        val (start, wavesInfo, path) = entity[Spawn]
        val currentWaveInfo = wavesInfo[waveIdx]

        // update timer and wave index
        timer += deltaTime
        if (timer < currentWaveInfo.interval) {
            // not enough time has passed yet -> do nothing
            return
        }
        timer = 0f
        waveIdx++
        if (waveIdx >= wavesInfo.size) {
            // all entities spawned -> remove spawner
            entity.remove()
        }

        // spawn a new entity
        val gdxAnimation = animationMap[currentWaveInfo.type]
        world.entity {
            it += Transform(position = start.cpy(), size = vec2(1f, 1f), z = Z_OBJECT)
            it += Render(gdxAnimation.keyFrames.first())
            it += Animation(gdxAnimation)
            it += FollowPath(path)
        }
        // TODO when finish reached -> stop animation, fade out and remove the entity
    }

    override fun onDispose() {
        disposableTextures.values().forEach { it.dispose() }
    }
}