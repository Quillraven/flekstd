package io.github.quillraven.flekstd.cfg

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.Vector2
import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.Attack
import io.github.quillraven.flekstd.component.DamageEffectComponent
import io.github.quillraven.flekstd.component.GdxAnimation
import io.github.quillraven.flekstd.component.Perimeter
import io.github.quillraven.flekstd.component.SlowEffect
import io.github.quillraven.flekstd.component.SplashEffect
import ktx.app.gdxError
import ktx.assets.toInternalFile
import ktx.math.vec2
import java.util.EnumMap

data class TowerCfg(
    val perimeter: () -> Perimeter,
    val attack: (snd: Sound) -> Attack,
    val attackSnd: Sound,
    val scale: Float,
    val gdxAnimations: EnumMap<AnimationType, GdxAnimation>,
    val effects: List<() -> DamageEffectComponent<*>> = emptyList(),
) {
    companion object {
        fun byTowerKey(key: String): TowerCfg = when (key) {
            "warrior" -> TowerCfg(
                perimeter = { Perimeter(range = 1f) },
                attack = { snd ->
                    Attack(
                        cooldown = 0.5f,
                        damage = 1f,
                        ProjectileCfg.byProjectileKey(ProjectileCfg.INSTANT_KEY),
                        projectileDelay = 0.25f,
                        projectileOffset = Vector2.Zero,
                        sound = snd,
                    )
                },
                attackSnd = Gdx.audio.newSound("sound/sword.wav".toInternalFile()),
                scale = 2f,
                gdxAnimations = animationMapOf("warrior", AnimationType.IDLE, AnimationType.ATTACK),
                effects = listOf { SplashEffect(radius = 1f, percentage = 0.5f, applyEffects = false) },
            )

            "archer" -> TowerCfg(
                perimeter = { Perimeter(range = 2.5f) },
                attack = { snd ->
                    Attack(
                        cooldown = 0.9f,
                        damage = 2f,
                        ProjectileCfg.byProjectileKey("arrow"),
                        projectileDelay = 0.6f,
                        projectileOffset = vec2(0.3f, 0.4f),
                        sound = snd,
                    )
                },
                attackSnd = Gdx.audio.newSound("sound/arrow-shoot.wav".toInternalFile()),
                scale = 2.25f,
                gdxAnimations = animationMapOf("archer", AnimationType.IDLE, AnimationType.ATTACK),
            )

            "monk" -> TowerCfg(
                perimeter = { Perimeter(range = 4f) },
                attack = { snd ->
                    Attack(
                        cooldown = 1.2f,
                        damage = 1f,
                        ProjectileCfg.byProjectileKey("water"),
                        projectileDelay = 0.55f,
                        projectileOffset = vec2(0.2f, 0.2f),
                        sound = snd,
                    )
                },
                attackSnd = Gdx.audio.newSound("sound/water-shoot.wav".toInternalFile()),
                scale = 2.5f,
                gdxAnimations = animationMapOf("monk", AnimationType.IDLE, AnimationType.ATTACK),
                effects = listOf(
                    { SlowEffect(percentage = 0.5f, duration = 2f) },
                    { SplashEffect(radius = 2f, percentage = 0.25f, applyEffects = true) },
                ),
            )

            else -> gdxError("$key is not a tower key")
        }
    }
}
