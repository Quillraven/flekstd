package io.github.quillraven.flekstd.cfg

import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.GdxAnimation
import ktx.app.gdxError
import java.util.EnumMap

data class ProjectileCfg(
    val gdxAnimations: EnumMap<AnimationType, GdxAnimation>,
    val speed: Float,
    val scale: Float,
    val flipX: Boolean,
) {
    companion object {
        const val INSTANT_KEY = ""

        fun byProjectileKey(key: String): ProjectileCfg = when (key) {
            INSTANT_KEY -> ProjectileCfg(
                gdxAnimations = EnumMap<AnimationType, GdxAnimation>(AnimationType::class.java),
                speed = 9999f,
                scale = 0f,
                flipX = false,
            )

            "arrow" -> ProjectileCfg(
                gdxAnimations = animationMapOf("arrow", AnimationType.IDLE),
                speed = 10f,
                scale = 1f,
                flipX = true,
            )

            "water" -> ProjectileCfg(
                gdxAnimations = animationMapOf("water", AnimationType.IDLE),
                speed = 7f,
                scale = 2f,
                flipX = false,
            )

            else -> gdxError("$key is not a projectile key")
        }

    }
}