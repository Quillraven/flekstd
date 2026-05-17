package io.github.quillraven.flekstd.cfg

import io.github.quillraven.flekstd.component.Health
import io.github.quillraven.flekstd.component.Speed
import ktx.app.gdxError

data class EnemyCfg(
    val health: () -> Health,
    val speed: () -> Speed,
) {
    companion object {
        fun byEnemyKey(key: String): EnemyCfg = when (key) {
            "pawn" -> EnemyCfg(
                health = { Health(base = 1f, regeneration = 0f) },
                speed = { Speed(3f) }
            )

            "lancer" -> EnemyCfg(
                health = { Health(base = 10f, regeneration = 0.5f) },
                speed = { Speed(1.5f) }
            )

            else -> gdxError("Unsupported enemy key: $key")
        }
    }
}