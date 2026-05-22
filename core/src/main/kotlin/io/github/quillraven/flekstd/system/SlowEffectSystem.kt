package io.github.quillraven.flekstd.system

import com.badlogic.gdx.graphics.Color
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.SlowEffect
import io.github.quillraven.flekstd.component.Speed

class SlowEffectSystem : IteratingSystem(
    family = family { all(SlowEffect, Speed, Render) }
) {
    override fun onTickEntity(entity: Entity) {
        val slowCmp = entity[SlowEffect]
        val speedCmp = entity[Speed]
        val color = entity[Render].color

        slowCmp.duration -= deltaTime
        if (slowCmp.duration <= 0f) {
            speedCmp.current = speedCmp.base
            color.set(Color.WHITE)
            entity.configure { it -= SlowEffect }
        } else {
            speedCmp.current = speedCmp.base * (1f - slowCmp.percentage)
            color.set(Color.CYAN)
        }
    }
}
