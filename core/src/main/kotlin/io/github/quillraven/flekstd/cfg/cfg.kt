package io.github.quillraven.flekstd.cfg

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.GdxAnimation
import ktx.collections.toGdxArray
import java.util.EnumMap

/**
 * Creates an [EnumMap] of [AnimationType] to [GdxAnimation] by loading sprite sheets from the `assets/graphic` folder.
 *
 * For each [AnimationType] in [types], the corresponding PNG file is expected at
 * `graphic/<key>_<type>.png` (e.g. `graphic/warrior_idle.png` for key `"warrior"` and type [AnimationType.IDLE]).
 */
fun animationMapOf(key: String, vararg types: AnimationType): EnumMap<AnimationType, GdxAnimation> {
    fun getAnimation(key: String): GdxAnimation {
        val sheet = Texture("graphic/$key.png")
        val (tileWidth, tileHeight) = when {
            key.startsWith("lancer") -> 320 to 320
            else -> 192 to 192
        }
        val regions = TextureRegion.split(sheet, tileWidth, tileHeight)
        return GdxAnimation(1 / 10f, regions.flatten().toGdxArray())
    }

    val gdxAnimations = EnumMap<AnimationType, GdxAnimation>(AnimationType::class.java)
    types.forEach { type ->
        gdxAnimations[type] = getAnimation("${key}_${type.name.lowercase()}")
    }

    return gdxAnimations
}