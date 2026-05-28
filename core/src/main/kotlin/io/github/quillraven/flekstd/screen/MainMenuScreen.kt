package io.github.quillraven.flekstd.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import io.github.quillraven.flekstd.GdxGame
import io.github.quillraven.flekstd.ui.MainMenuUI
import ktx.app.KtxScreen
import ktx.assets.toInternalFile

class MainMenuScreen(
    game: GdxGame,
    private val stage: Stage = game.stage,
    private val inputMultiplexer: InputMultiplexer = game.inputMultiplexer,
    skin: Skin = game.skin,
) : KtxScreen {
    private val btnClickSnd = Gdx.audio.newSound("sound/btn-click.wav".toInternalFile())
    private val mainMenuUI = MainMenuUI(
        skin,
        onStartGame = {
            btnClickSnd.play()
            game.setScreen<GameScreen>()
        },
        onQuitGame = { Gdx.app.exit() },
    )

    override fun show() {
        stage.addActor(mainMenuUI)
        inputMultiplexer.addProcessor(stage)
    }

    override fun hide() {
        stage.clear()
        inputMultiplexer.clear()
    }

    override fun render(delta: Float) {
        stage.viewport.apply()
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        btnClickSnd.dispose()
    }
}
