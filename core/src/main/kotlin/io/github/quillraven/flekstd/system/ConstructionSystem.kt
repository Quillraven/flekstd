package io.github.quillraven.flekstd.system

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import io.github.quillraven.flekstd.cfg.TowerCfg
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.AnimationType
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
    family = family { all(Transform, Tag.CONSTRUCTING, Render) }
), KtxInputAdapter {
    private val mouseWorldPos = vec2()
    private val levelChangeRequestEntities = family { all(LevelChangeRequest) }
    private val pathEntities = family { all(Transform, Tag.PATH) }
    private val blockedTiles = gdxArrayOf<Vector2>()
    private var construct = false
    private val towerRegionCache: ObjectMap<String, TextureRegion> = ObjectMap()
    private val towerCfgCache: ObjectMap<String, TowerCfg> = ObjectMap()

    override fun onTick() {
        if (levelChangeRequestEntities.isNotEmpty) {
            updatePath()
        }
        super.onTick()
        construct = false
    }

    override fun onTickEntity(entity: Entity) {
        val (position) = entity[Transform]
        val renderCmp = entity[Render]

        position.set(floor(mouseWorldPos.x), floor(mouseWorldPos.y))
        if (construct && position !in blockedTiles) {
            entity.configure { it -= Tag.CONSTRUCTING }
            renderCmp.color.set(Color.WHITE)
            return
        }

        if (position in blockedTiles) {
            renderCmp.color.set(Color.RED)
        } else {
            renderCmp.color.set(Color.WHITE)
        }
        renderCmp.color.a = 0.5f
    }

    private fun updatePath() {
        blockedTiles.clear()
        pathEntities.forEach { entity ->
            blockedTiles.add(entity[Transform].position)
        }
    }

    fun spawnConstructionTower(towerKey: String) = world.entity {
        it += Tag.CONSTRUCTING
        it += Transform(
            position = vec2(floor(mouseWorldPos.x), floor(mouseWorldPos.y)),
            size = vec2(1f, 1f),
            z = Z_OBJECT
        )

        val cfg = towerCfgCache.getOrPut(towerKey) { TowerCfg.byTowerKey(towerKey) }
        it += Render(Render.EMPTY_REGION, cfg.scale)
        it += Animation(towerKey, AnimationType.IDLE, PlayMode.LOOP)

        it += cfg.perimeter()
        it += cfg.attack()
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