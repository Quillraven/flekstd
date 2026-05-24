package io.github.quillraven.flekstd

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.github.quillraven.flekstd.screen.GameScreen
import io.github.quillraven.flekstd.ui.GameSkin
import ktx.app.KtxGame
import ktx.app.KtxScreen

class GdxGame : KtxGame<KtxScreen>() {
    val batch: Batch by lazy { SpriteBatch() }
    val gameViewport: Viewport = FitViewport(16f, 9f)
    val uiViewport: Viewport = FitViewport(1920f, 1080f)
    val stage: Stage by lazy { Stage(uiViewport, batch) }
    val skin: Skin by lazy { GameSkin() }
    val inputMultiplexer = InputMultiplexer()

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        Gdx.input.inputProcessor = inputMultiplexer

        addScreen(GameScreen(this))
        setScreen<GameScreen>()
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        uiViewport.update(width, height, true)
        super.resize(width, height)
    }

    override fun render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f, true)
        currentScreen.render(Gdx.graphics.deltaTime.coerceIn(0f, 1 / 30f))
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        stage.dispose()
        skin.dispose()
    }

    companion object {
        fun Int.toWorldUnits() = this / 64f
    }
}