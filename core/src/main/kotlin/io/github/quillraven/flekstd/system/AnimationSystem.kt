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
        val animation = entity[Animation]

        animation.stateTime += deltaTime
        val keyFrame = animation.gdxAnimation.getKeyFrame(animation.stateTime, true)
        entity[Render].region = keyFrame
    }
}