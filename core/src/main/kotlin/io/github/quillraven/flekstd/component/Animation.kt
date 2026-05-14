package io.github.quillraven.flekstd.component

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

data class Animation(
    val gdxAnimation: GdxAnimation,
) : Component<Animation> {
    var stateTime: Float = 0f

    override fun type() = Animation

    companion object : ComponentType<Animation>()
}