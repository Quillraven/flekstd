package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class SlowEffect(
    var percentage: Float,
    var duration: Float,
) : Component<SlowEffect>, DamageEffect {
    override fun type() = SlowEffect

    companion object : ComponentType<SlowEffect>()
}
