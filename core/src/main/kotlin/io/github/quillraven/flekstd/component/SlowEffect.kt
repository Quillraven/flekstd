package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.ComponentType

data class SlowEffect(
    var percentage: Float,
    var duration: Float,
) : DamageEffectComponent<SlowEffect> {
    override fun type() = SlowEffect

    companion object : ComponentType<SlowEffect>()
}
