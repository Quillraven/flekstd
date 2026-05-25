package io.github.quillraven.flekstd.screen

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import io.github.quillraven.flekstd.GdxGame
import io.github.quillraven.flekstd.cfg.EnemyCfg
import io.github.quillraven.flekstd.component.LevelChangeRequest
import io.github.quillraven.flekstd.component.Spawn
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.system.AnimationSystem
import io.github.quillraven.flekstd.system.AttackSystem
import io.github.quillraven.flekstd.system.ConstructionSystem
import io.github.quillraven.flekstd.system.DamageSystem
import io.github.quillraven.flekstd.system.FollowPathSystem
import io.github.quillraven.flekstd.system.HealthSystem
import io.github.quillraven.flekstd.system.HomingMoveSystem
import io.github.quillraven.flekstd.system.LevelChangeSystem
import io.github.quillraven.flekstd.system.PerimeterSystem
import io.github.quillraven.flekstd.system.ProjectileSystem
import io.github.quillraven.flekstd.system.RangeRenderSystem
import io.github.quillraven.flekstd.system.RenderSystem
import io.github.quillraven.flekstd.system.RequestCleanupSystem
import io.github.quillraven.flekstd.system.SlowEffectSystem
import io.github.quillraven.flekstd.system.SpawnSystem
import io.github.quillraven.flekstd.system.UiRenderSystem
import io.github.quillraven.flekstd.ui.GameUI
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen

class GameScreen(
    game: GdxGame,
    private val gameViewport: Viewport = game.gameViewport,
    private val batch: Batch = game.batch,
    private val shapeRenderer: ShapeRenderer = game.shapeRenderer,
    private val stage: Stage = game.stage,
    private val inputMultiplexer: InputMultiplexer = game.inputMultiplexer,
    skin: Skin = game.skin,
) : KtxScreen {
    private val world = ecsWorld()
    private val gameUI = GameUI(skin, onTowerClicked = this::constructTower, onSpawnClicked = this::spawnWave)

    private fun ecsWorld(): World = configureWorld {
        injectables {
            add(batch)
            add(shapeRenderer)
            add(stage)
            add(gameViewport)
        }

        systems {
            add(LevelChangeSystem())
            add(ConstructionSystem())
            add(SpawnSystem(onWaveDone = this@GameScreen::waveComplete))
            add(FollowPathSystem())
            add(PerimeterSystem())
            add(AttackSystem())
            add(ProjectileSystem())
            add(HomingMoveSystem())
            add(DamageSystem())
            add(SlowEffectSystem())
            add(HealthSystem())
            add(AnimationSystem())
            add(RenderSystem())
            add(RangeRenderSystem())
            add(UiRenderSystem())
            add(RequestCleanupSystem())
        }
    }

    override fun show() {
        // change to first level
        world.entity {
            it += LevelChangeRequest("level_1")
        }
        world.systems.filterIsInstance<KtxInputAdapter>().forEach { inputMultiplexer.addProcessor(it) }

        // setup UI
        stage.addActor(gameUI)
        inputMultiplexer.addProcessor(stage)
    }

    fun constructTower(towerKey: String) {
        world.family { all(Tag.CONSTRUCTING) }.forEach { it.remove() }
        world.system<ConstructionSystem>().spawnConstructionTower(towerKey)
    }

    fun spawnWave(enemies: ObjectMap<String, Int>) {
        world.family { all(Spawn) }.forEach { entity ->
            val spawnCmp = entity[Spawn]
            spawnCmp.queue.clear()
            spawnCmp.timer = 0f

            // expand each enemy key by its count, then sort by health base ascending
            enemies.entries()
                .flatMap { entry -> List(entry.value) { entry.key } }
                .sortedBy { key -> EnemyCfg.byEnemyKey(key).health().base }
                .forEach { spawnCmp.queue.addLast(it) }

            entity.configure { it += Tag.SPAWNING }
        }
    }

    fun waveComplete() {
        gameUI.enableSpawning()
    }

    override fun hide() {
        stage.clear()
        inputMultiplexer.clear()
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        world.dispose()
    }
}