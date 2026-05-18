package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.ObjectMap
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.GdxAnimation
import io.github.quillraven.flekstd.component.Render
import ktx.app.gdxError
import ktx.collections.getOrPut
import ktx.collections.set
import ktx.collections.toGdxArray

class AnimationSystem : IteratingSystem(
    family = family { all(Animation, Render) }
) {
    private val animationCache: ObjectMap<String, GdxAnimation> = ObjectMap()
    private val animationMapCache: ObjectMap<String, ObjectMap<AnimationType, GdxAnimation>> = ObjectMap()

    private fun getAnimation(key: String): GdxAnimation = animationCache.getOrPut(key) {
        val sheet = Texture("graphic/$key.png")
        val (tileWidth, tileHeight) = when (key) {
            "pawn_run" -> 192 to 192
            "lancer_run" -> 320 to 320
            "warrior_idle" -> 192 to 192
            "warrior_attack" -> 192 to 192
            "archer_idle" -> 192 to 192
            "archer_attack" -> 192 to 192
            "monk_idle" -> 192 to 192
            "monk_attack" -> 192 to 192
            else -> gdxError("Unknown animation key: $key")
        }
        val regions = TextureRegion.split(sheet, tileWidth, tileHeight)
        GdxAnimation(1 / 10f, regions.flatten().toGdxArray())
    }

    private fun getAnimations(key: String): ObjectMap<AnimationType, GdxAnimation> = animationMapCache.getOrPut(key) {
        ObjectMap<AnimationType, GdxAnimation>().apply {
            when (key) {
                "pawn", "lancer" -> this[AnimationType.RUN] = getAnimation("${key}_run")
                else -> {
                    // tower animations
                    this[AnimationType.IDLE] = getAnimation("${key}_idle")
                    this[AnimationType.ATTACK] = getAnimation("${key}_attack")
                }
            }
        }
    }

    override fun onTickEntity(entity: Entity) {
        val animationCmp = entity[Animation]

        if (animationCmp.gdxAnimations.isEmpty) {
            // initialize animations of entity
            animationCmp.gdxAnimations.putAll(getAnimations(animationCmp.key))
            animationCmp.changeTo(animationCmp.currentType, animationCmp.playMode)
        }

        animationCmp.current.playMode = animationCmp.playMode
        val keyFrame = animationCmp.current.getKeyFrame(animationCmp.stateTime)
        entity[Render].region = keyFrame
        animationCmp.stateTime += deltaTime
    }

    override fun onDispose() {
        animationCache.values().forEach { it.keyFrames.first().texture.dispose() }
    }
}