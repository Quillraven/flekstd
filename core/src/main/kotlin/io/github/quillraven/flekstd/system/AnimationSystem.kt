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
            "pawn" -> 66 to 77
            "lancer" -> 70 to 138
            "warrior_idle" -> 79 to 89
            "warrior_attack" -> 110 to 101
            "archer_idle" -> 70 to 88
            "archer_attack" -> 87 to 90
            "monk_idle" -> 58 to 69
            "monk_attack" -> 121 to 71
            else -> gdxError("Unknown animation key: $key")
        }
        val regions = TextureRegion.split(sheet, tileWidth, tileHeight)
        GdxAnimation(1 / 10f, regions.flatten().toGdxArray())
    }

    private fun getAnimations(key: String): ObjectMap<AnimationType, GdxAnimation> = animationMapCache.getOrPut(key) {
        ObjectMap<AnimationType, GdxAnimation>().apply {
            when (key) {
                "pawn", "lancer" -> this[AnimationType.RUN] = getAnimation(key)
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