package io.github.quillraven.flekstd.system

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ObjectMap
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import io.github.quillraven.flekstd.cfg.EnemyCfg
import io.github.quillraven.flekstd.component.Animation
import io.github.quillraven.flekstd.component.AnimationType
import io.github.quillraven.flekstd.component.FollowPath
import io.github.quillraven.flekstd.component.Render
import io.github.quillraven.flekstd.component.Spawn
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.component.Transform
import io.github.quillraven.flekstd.component.Transform.Companion.Z_OBJECT
import ktx.collections.GdxArray
import ktx.collections.getOrPut
import ktx.math.vec2

class SpawnSystem(private val onWaveDone: () -> Unit) : IteratingSystem(
    family = family { all(Spawn, Tag.SPAWNING) }
) {
    private val enemyCfgCache: ObjectMap<String, EnemyCfg> = ObjectMap()

    override fun onTickEntity(entity: Entity) {
        val spawnCmp = entity[Spawn]

        spawnCmp.timer += deltaTime
        if (spawnCmp.timer < SPAWN_INTERVAL) return
        spawnCmp.timer = 0f

        val enemyKey = spawnCmp.queue.removeIndex(0)
        spawnEnemy(spawnCmp.path, enemyKey)

        if (spawnCmp.queue.isEmpty) {
            entity.configure { it -= Tag.SPAWNING }
            onWaveDone()
        }
    }

    private fun spawnEnemy(
        path: GdxArray<Vector2>,
        enemyKey: String,
    ) {
        val cfg = enemyCfgCache.getOrPut(enemyKey) { EnemyCfg.byEnemyKey(enemyKey) }
        val start = path.first()
        world.entity {
            it += Transform(position = start.cpy(), size = vec2(1f, 1f), z = Z_OBJECT)
            it += Render(Render.EMPTY_REGION, cfg.scale)
            it += Animation(AnimationType.RUN, cfg.gdxAnimations)
            it += FollowPath(path)
            it += Tag.ENEMY
            it += cfg.speed()
            it += cfg.health()
        }
    }

    override fun onDispose() {
        enemyCfgCache.values()
            .flatMap { it.gdxAnimations.values }
            .forEach { it.keyFrames.first().texture.dispose() }
    }

    companion object {
        const val SPAWN_INTERVAL = 1f
    }
}