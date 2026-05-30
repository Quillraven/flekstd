package io.github.quillraven.flekstd.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.Render

class AnimationSystem : IteratingSystem(
    family = family { all(Animation, Render) }
) {
    override fun onTickEntity(entity: Entity) {
        val animationCmp = entity[Animation]

        // GdxAnimation objects are shared across all entities of the same type (cached in cfg).
        // playMode must be set every frame so each entity's intended mode is applied before getKeyFrame.
        animationCmp.current.playMode = animationCmp.playMode
        val keyFrame = animationCmp.current.getKeyFrame(animationCmp.stateTime)
        entity[Render].region = keyFrame
        animationCmp.stateTime += deltaTime
    }
}