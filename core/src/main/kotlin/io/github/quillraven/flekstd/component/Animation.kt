package io.github.quillraven.flekstd.component

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.app.gdxError
import java.util.EnumMap

typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

enum class AnimationType {
    IDLE, ATTACK, RUN;
}

class Animation(
    initialType: AnimationType,
    val gdxAnimations: EnumMap<AnimationType, GdxAnimation>,
    var playMode: PlayMode = PlayMode.LOOP,
) : Component<Animation> {
    var stateTime: Float = 0f
    var current: GdxAnimation = gdxAnimations[initialType] ?: missingAnimationError(initialType)
        private set
    var currentType: AnimationType = initialType
        private set

    override fun type() = Animation

    fun changeTo(type: AnimationType, playMode: PlayMode = PlayMode.LOOP) {
        current = gdxAnimations[type] ?: missingAnimationError(type)
        currentType = type
        stateTime = 0f
        this.playMode = playMode
    }

    fun isFinished(): Boolean = current.isAnimationFinished(stateTime)

    companion object : ComponentType<Animation>() {
        fun missingAnimationError(type: AnimationType): Nothing =
            gdxError("There is no animation for $type in gdxAnimations map")
    }
}