package io.github.quillraven.flekstd.screen

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import io.github.quillraven.flekstd.GdxGame
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
    private val stage: Stage = game.stage,
    private val inputMultiplexer: InputMultiplexer = game.inputMultiplexer,
    private val skin: Skin = game.skin,
) : KtxScreen {
    private val world = ecsWorld()

    private fun ecsWorld(): World = configureWorld {
        injectables {
            add(batch)
            add(stage)
            add(gameViewport)
        }

        systems {
            add(LevelChangeSystem())
            add(ConstructionSystem())
            add(SpawnSystem())
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
        val gameUI = GameUI(skin, onTowerClicked = this::constructTower, onSpawnClicked = this::spawnWave)
        stage.addActor(gameUI)
        inputMultiplexer.addProcessor(stage)
    }

    fun constructTower(towerKey: String) {
        world.family { all(Tag.CONSTRUCTING) }.forEach { it.remove() }
        world.system<ConstructionSystem>().spawnConstructionTower(towerKey)
    }

    fun spawnWave(enemies: ObjectMap<String, Int>) {
        world.family { all(Spawn) }.forEach { entity ->
            entity.configure {
                it += Tag.SPAWNING
            }
        }
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