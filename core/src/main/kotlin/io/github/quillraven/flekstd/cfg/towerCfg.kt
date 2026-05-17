package io.github.quillraven.flekstd.cfg

import io.github.quillraven.flekstd.component.Attack
import io.github.quillraven.flekstd.component.Perimeter
import ktx.app.gdxError

data class TowerCfg(
    val perimeter: () -> Perimeter,
    val attack: () -> Attack,
) {
    companion object {
        fun byTowerKey(key: String): TowerCfg = when (key) {
            "warrior" -> TowerCfg(
                perimeter = { Perimeter(range = 1f) },
                attack = { Attack(cooldown = 0.5f, damage = 1f, damageDelay = 0.25f) },
            )

            "archer" -> TowerCfg(
                perimeter = { Perimeter(range = 2.5f) },
                attack = { Attack(cooldown = 0.9f, damage = 2f, damageDelay = 0.6f) },
            )

            "monk" -> TowerCfg(
                perimeter = { Perimeter(range = 4f) },
                attack = { Attack(cooldown = 1.2f, damage = 1f, damageDelay = 0.55f) },
            )

            else -> gdxError("$key is not a tower key")
        }
    }
}
