package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.EntityRef

data class Perimeter(
    val range: Float,
) : Component<Perimeter> {
    var target = EntityRef.NONE

    override fun type() = Perimeter

    companion object : ComponentType<Perimeter>()
}
