package io.github.quillraven.flekstd.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.collections.GdxArray

data class WaveInfo(
    val amount: Int,
    val type: String,
    val interval: Float,
)

data class Spawn(
    val path: GdxArray<Vector2>,
    val wavesInfo: GdxArray<WaveInfo>,
) : Component<Spawn> {
    var timer = wavesInfo.first().interval
    var waveIdx = 0
    var numSpawns = 0

    val currentWaveInfo: WaveInfo
        get() = wavesInfo[waveIdx]

    override fun type() = Spawn

    fun nextWave() {
        waveIdx++
        numSpawns = 0
        if (waveIdx >= wavesInfo.size) return
        timer = wavesInfo[waveIdx].interval

    }

    companion object : ComponentType<Spawn>()
}