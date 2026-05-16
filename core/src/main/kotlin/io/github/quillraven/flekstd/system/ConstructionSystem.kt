package io.github.quillraven.flekstd.system

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import io.github.quillraven.flekstd.component.Construction
import io.github.quillraven.flekstd.component.LevelChangeRequest
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.component.Transform
import io.github.quillraven.flekstd.component.Transform.Companion.Z_OBJECT
import ktx.app.KtxInputAdapter
import ktx.collections.gdxArrayOf
import ktx.collections.getOrPut
import ktx.math.vec2
import kotlin.math.floor

class ConstructionSystem(
    private val gameViewport: Viewport = inject(),
) : IteratingSystem(
    family = family { all(Transform, Construction) }
), KtxInputAdapter {
    private val mouseWorldPos = vec2()
    private val levelChangeRequestEntities = family { all(LevelChangeRequest) }
    private val pathEntities = family { all(Transform, Tag.PATH) }
    private val blockedTiles = gdxArrayOf<Vector2>()
    private var construct = false
    private val towerRegionCache: ObjectMap<String, TextureRegion> = ObjectMap()

    override fun onTick() {
        if (levelChangeRequestEntities.isNotEmpty) {
            updatePath()
        }
        super.onTick()
        construct = false
    }

    override fun onTickEntity(entity: Entity) {
        val (position) = entity[Transform]

        position.set(floor(mouseWorldPos.x), floor(mouseWorldPos.y))
        if (construct && position !in blockedTiles) {
            spawnTower(entity[Construction].towerKey, position)
            entity.remove()
            return
        }

        if (position in blockedTiles) {
            entity[Render].color.set(Color.RED)
        } else {
            entity[Render].color.set(Color.WHITE)
        }
        entity[Render].color.a = 0.5f
    }

    private fun updatePath() {
        blockedTiles.clear()
        pathEntities.forEach { entity ->
            blockedTiles.add(entity[Transform].position)
        }
    }

    private fun towerIdleTexture(towerKey: String) = towerRegionCache.getOrPut(towerKey) {
        TextureRegion(Texture("graphic/${towerKey}_idle.png"))
    }

    fun spawnTower(towerKey: String, position: Vector2) = world.entity {
        it += Transform(position = position.cpy(), size = vec2(1f, 1f), z = Z_OBJECT)
        it += Render(towerIdleTexture(towerKey))
    }

    fun spawnConstructionEntity(towerKey: String) = world.entity {
        it += Transform(
            position = vec2(floor(mouseWorldPos.x), floor(mouseWorldPos.y)),
            size = vec2(1f, 1f),
            z = Z_OBJECT
        )
        it += Construction(towerKey)
        it += Render(towerIdleTexture(towerKey))
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        mouseWorldPos.set(screenX.toFloat(), screenY.toFloat())
        gameViewport.unproject(mouseWorldPos)
        return true
    }

    override fun touchDown(
        screenX: Int,
        screenY: Int,
        pointer: Int,
        button: Int
    ): Boolean {
        construct = button == Input.Buttons.LEFT
        return mouseMoved(screenX, screenY)
    }

    override fun onDispose() {
        towerRegionCache.values().forEach { it.texture.dispose() }
    }
}