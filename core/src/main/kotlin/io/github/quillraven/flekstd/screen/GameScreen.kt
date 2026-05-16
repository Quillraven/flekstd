package io.github.quillraven.flekstd.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import io.github.quillraven.flekstd.GdxGame
import io.github.quillraven.flekstd.component.Construction
import io.github.quillraven.flekstd.component.LevelChangeRequest
import io.github.quillraven.flekstd.component.Spawn
import io.github.quillraven.flekstd.component.Tag
import io.github.quillraven.flekstd.system.AnimationSystem
import io.github.quillraven.flekstd.system.ConstructionSystem
import io.github.quillraven.flekstd.system.FollowPathSystem
import io.github.quillraven.flekstd.system.LevelChangeSystem
import io.github.quillraven.flekstd.system.RenderSystem
import io.github.quillraven.flekstd.system.RequestCleanupSystem
import io.github.quillraven.flekstd.system.SpawnSystem
import io.github.quillraven.flekstd.system.UiRenderSystem
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen

class GameScreen(
    game: GdxGame,
    private val gameViewport: Viewport = game.gameViewport,
    private val batch: Batch = game.batch,
    private val stage: Stage = game.stage,
    private val inputMultiplexer: InputMultiplexer = game.inputMultiplexer,
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
            add(AnimationSystem())
            add(FollowPathSystem())
            add(RenderSystem())
            add(UiRenderSystem())
            add(RequestCleanupSystem())
        }
    }

    override fun show() {
        world.entity {
            it += LevelChangeRequest("level_1")
        }
        world.systems.filterIsInstance<KtxInputAdapter>().forEach { inputMultiplexer.addProcessor(it) }
    }

    override fun hide() {
        stage.clear()
        inputMultiplexer.clear()
    }

    override fun render(delta: Float) {
        world.update(delta)

        // later on replaced by a UI button
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER) -> {
                world.family { all(Spawn) }.forEach { entity ->
                    entity.configure {
                        it += Tag.SPAWNING
                    }
                }
            }

            Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) -> {
                world.family { all(Construction) }.forEach { it.remove() }
                world.system<ConstructionSystem>().spawnConstructionEntity("warrior")
            }

            Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) -> {
                world.family { all(Construction) }.forEach { it.remove() }
                world.system<ConstructionSystem>().spawnConstructionEntity("archer")
            }

            Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) -> {
                world.family { all(Construction) }.forEach { it.remove() }
                world.system<ConstructionSystem>().spawnConstructionEntity("monk")
            }
        }
    }

    override fun dispose() {
        world.dispose()
    }
}