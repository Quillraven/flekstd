package io.github.quillraven.flekstd.component

import com.github.quillraven.fleks.ComponentType

data class SplashEffect(
    val radius: Float,
    val percentage: Float,
    val applyEffects: Boolean,
) : DamageEffectComponent<SplashEffect> {
    override fun type() = SplashEffect

    companion object : ComponentType<SplashEffect>()
}
