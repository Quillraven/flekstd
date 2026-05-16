package io.github.quillraven.flekstd.component

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import io.github.quillraven.flekstd.GdxGame.Companion.toWorldUnits
import ktx.math.vec2

class Render(
    region: TextureRegion,
) : Component<Render> {
    val regionSize: Vector2 = vec2(region.regionWidth.toWorldUnits(), region.regionHeight.toWorldUnits())

    var region: TextureRegion = region
        set(value) {
            regionSize.x = value.regionWidth.toWorldUnits()
            regionSize.y = value.regionHeight.toWorldUnits()
            field = value
        }

    val color: Color = Color.WHITE.cpy()

    override fun type() = Render

    operator fun component1(): TextureRegion = region
    operator fun component2(): Vector2 = regionSize
    operator fun component3(): Color = color

    companion object : ComponentType<Render>() {
        val EMPTY_REGION = TextureRegion()
    }
}