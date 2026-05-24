package io.github.quillraven.flekstd.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Queue
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.collections.GdxArray

data class Spawn(
    val path: GdxArray<Vector2>,
) : Component<Spawn> {
    var timer = 0f
    val queue = Queue<String>(32)

    override fun type() = Spawn

    companion object : ComponentType<Spawn>()
}