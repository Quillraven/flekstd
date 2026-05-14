package io.github.quillraven.flekstd.screen

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import io.github.quillraven.flekstd.GdxGame
import io.github.quillraven.flekstd.component.LevelChange
import io.github.quillraven.flekstd.system.AnimationSystem
import io.github.quillraven.flekstd.system.FollowPathSystem
import io.github.quillraven.flekstd.system.LevelChangeSystem
import io.github.quillraven.flekstd.system.RenderSystem
import io.github.quillraven.flekstd.system.SpawnSystem
import io.github.quillraven.flekstd.system.UiRenderSystem
import ktx.app.KtxScreen

class GameScreen(
    game: GdxGame,
    private val gameViewport: Viewport = game.gameViewport,
    private val batch: Batch = game.batch,
    private val stage: Stage = game.stage,
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
            add(SpawnSystem())
            add(AnimationSystem())
            add(FollowPathSystem())
            add(RenderSystem())
            add(UiRenderSystem())
        }
    }

    override fun show() {
        world.entity {
            it += LevelChange("level_1")
        }
    }

    override fun hide() {
        stage.clear()
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        world.dispose()
    }
}