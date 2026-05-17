package io.github.quillraven.flekstd.cfg

import io.github.quillraven.flekstd.component.Perimeter
import ktx.app.gdxError

data class TowerCfg(
    val perimeter: () -> Perimeter,
) {
    companion object {
        fun byTowerKey(key: String): TowerCfg = when (key) {
            "warrior" -> TowerCfg(
                perimeter = { Perimeter(range = 1f) },
            )

            "archer" -> TowerCfg(
                perimeter = { Perimeter(range = 2.5f) },
            )

            "monk" -> TowerCfg(
                perimeter = { Perimeter(range = 4f) },
            )

            else -> gdxError("${key} is not a tower key")
        }
    }
}
