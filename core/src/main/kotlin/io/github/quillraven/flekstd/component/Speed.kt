package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Speed(val base: Float) : Component<Speed> {
    var current: Float = base

    override fun type() = Speed

    companion object : ComponentType<Speed>()
}