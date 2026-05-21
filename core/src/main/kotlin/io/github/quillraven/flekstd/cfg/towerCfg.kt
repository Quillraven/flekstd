package io.github.quillraven.flekstd.cfg

import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.Attack
import io.github.quillraven.flekstd.component.GdxAnimation
import io.github.quillraven.flekstd.component.Perimeter
import ktx.app.gdxError
import java.util.EnumMap

data class TowerCfg(
    val perimeter: () -> Perimeter,
    val attack: () -> Attack,
    val scale: Float,
    val gdxAnimations: EnumMap<AnimationType, GdxAnimation>,
) {
    companion object {
        fun byTowerKey(key: String): TowerCfg = when (key) {
            "warrior" -> TowerCfg(
                perimeter = { Perimeter(range = 1f) },
                attack = { Attack(cooldown = 0.5f, damage = 1f, damageDelay = 0.25f) },
                scale = 2f,
                gdxAnimations = animationMapOf("warrior", AnimationType.IDLE, AnimationType.ATTACK),
            )

            "archer" -> TowerCfg(
                perimeter = { Perimeter(range = 2.5f) },
                attack = { Attack(cooldown = 0.9f, damage = 2f, damageDelay = 0.6f) },
                scale = 2.25f,
                gdxAnimations = animationMapOf("archer", AnimationType.IDLE, AnimationType.ATTACK),
            )

            "monk" -> TowerCfg(
                perimeter = { Perimeter(range = 4f) },
                attack = { Attack(cooldown = 1.2f, damage = 1f, damageDelay = 0.55f) },
                scale = 2.5f,
                gdxAnimations = animationMapOf("monk", AnimationType.IDLE, AnimationType.ATTACK),
            )

            else -> gdxError("$key is not a tower key")
        }
    }
}
