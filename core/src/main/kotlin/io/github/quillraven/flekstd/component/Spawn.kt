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
    val start: Vector2,
    val wavesInfo: GdxArray<WaveInfo>,
    val path: GdxArray<Vector2>,
) : Component<Spawn> {
    override fun type() = Spawn

    companion object : ComponentType<Spawn>()
}