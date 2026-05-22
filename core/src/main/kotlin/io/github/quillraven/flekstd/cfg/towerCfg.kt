package io.github.quillraven.flekstd.cfg

import com.badlogic.gdx.math.Vector2
import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.Attack
import io.github.quillraven.flekstd.component.DamageEffect
import io.github.quillraven.flekstd.component.GdxAnimation
import io.github.quillraven.flekstd.component.Perimeter
import io.github.quillraven.flekstd.component.SlowEffect
import ktx.app.gdxError
import ktx.math.vec2
import java.util.EnumMap

data class TowerCfg(
    val perimeter: () -> Perimeter,
    val attack: () -> Attack,
    val scale: Float,
    val gdxAnimations: EnumMap<AnimationType, GdxAnimation>,
    val effects: List<() -> DamageEffect> = emptyList(),
) {
    companion object {
        fun byTowerKey(key: String): TowerCfg = when (key) {
            "warrior" -> TowerCfg(
                perimeter = { Perimeter(range = 1f) },
                attack = {
                    Attack(
                        cooldown = 0.5f,
                        damage = 1f,
                        ProjectileCfg.byProjectileKey(ProjectileCfg.INSTANT_KEY),
                        projectileDelay = 0.25f,
                        projectileOffset = Vector2.Zero
                    )
                },
                scale = 2f,
                gdxAnimations = animationMapOf("warrior", AnimationType.IDLE, AnimationType.ATTACK),
            )

            "archer" -> TowerCfg(
                perimeter = { Perimeter(range = 2.5f) },
                attack = {
                    Attack(
                        cooldown = 0.9f,
                        damage = 2f,
                        ProjectileCfg.byProjectileKey("arrow"),
                        projectileDelay = 0.6f,
                        projectileOffset = vec2(0.3f, 0.4f)
                    )
                },
                scale = 2.25f,
                gdxAnimations = animationMapOf("archer", AnimationType.IDLE, AnimationType.ATTACK),
            )

            "monk" -> TowerCfg(
                perimeter = { Perimeter(range = 4f) },
                attack = {
                    Attack(
                        cooldown = 1.2f,
                        damage = 1f,
                        ProjectileCfg.byProjectileKey("water"),
                        projectileDelay = 0.55f,
                        projectileOffset = vec2(0.2f, 0.2f)
                    )
                },
                scale = 2.5f,
                gdxAnimations = animationMapOf("monk", AnimationType.IDLE, AnimationType.ATTACK),
                effects = listOf({ SlowEffect(percentage = 0.5f, duration = 2f) }),
            )

            else -> gdxError("$key is not a tower key")
        }
    }
}
