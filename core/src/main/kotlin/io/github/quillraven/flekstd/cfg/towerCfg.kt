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
                attack = { Attack(1f, 1f) },
            )

            "archer" -> TowerCfg(
                perimeter = { Perimeter(range = 2.5f) },
                attack = { Attack(0.75f, 2f) },
            )

            "monk" -> TowerCfg(
                perimeter = { Perimeter(range = 4f) },
                attack = { Attack(0.5f, 1f) },
            )

            else -> gdxError("$key is not a tower key")
        }
    }
}
