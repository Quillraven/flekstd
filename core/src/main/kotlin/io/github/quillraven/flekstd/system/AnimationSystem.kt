package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.ObjectMap
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.GdxAnimation
import io.github.quillraven.flekstd.component.Render
import ktx.app.gdxError
import ktx.collections.getOrPut
import ktx.collections.toGdxArray

class AnimationSystem : IteratingSystem(
    family = family { all(Animation, Render) }
) {
    private val animationCache: ObjectMap<String, GdxAnimation> = ObjectMap()

    private fun getAnimation(key: String): GdxAnimation {
        return animationCache.getOrPut(key) {
            val sheet = Texture("graphic/$key.png")
            val (tileWidth, tileHeight) = when (key) {
                "pawn" -> 66 to 77
                "lancer" -> 70 to 138
                else -> gdxError("Unknown animation key: $key")
            }
            val regions = TextureRegion.split(sheet, tileWidth, tileHeight)
            GdxAnimation(1 / 10f, regions.flatten().toGdxArray())
        }
    }

    override fun onTickEntity(entity: Entity) {
        val animationCmp = entity[Animation]
        var gdxAnimation = animationCmp.gdxAnimation
        if (gdxAnimation == null) {
            gdxAnimation = getAnimation(animationCmp.key)
            animationCmp.gdxAnimation = gdxAnimation
        }

        animationCmp.stateTime += deltaTime
        val keyFrame = gdxAnimation.getKeyFrame(animationCmp.stateTime, true)
        entity[Render].region = keyFrame
    }

    override fun onDispose() {
        animationCache.values().forEach { it.keyFrames.first().texture.dispose() }
    }
}