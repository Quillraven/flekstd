package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Construction(val towerKey: String) : Component<Construction> {
    override fun type() = Construction

    companion object : ComponentType<Construction>()
}