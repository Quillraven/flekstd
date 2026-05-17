package io.github.quillraven.flekstd.component

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.ObjectMap
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.app.gdxError

typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

enum class AnimationType {
    IDLE, ATTACK, RUN;
}

class Animation(
    val key: String,
    initialType: AnimationType,
    var playMode: PlayMode,
) : Component<Animation> {
    var stateTime: Float = 0f
    val gdxAnimations = ObjectMap<AnimationType, GdxAnimation>()
    var current: GdxAnimation = NO_ANIMATION
        private set
    var currentType: AnimationType = initialType
        private set

    override fun type() = Animation

    fun changeTo(type: AnimationType, playMode: PlayMode) {
        current = gdxAnimations[type] ?: gdxError("There is no animation for $type and key $key")
        currentType = type
        stateTime = 0f
        this.playMode = playMode
    }

    fun isFinished(): Boolean = current.isAnimationFinished(stateTime)

    companion object : ComponentType<Animation>() {
        private val NO_ANIMATION = GdxAnimation(1f)
    }
}