package io.github.quillraven.flekstd.ui

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import ktx.actors.onClick

class MainMenuUI(
    skin: Skin,
    onStartGame: () -> Unit,
    onQuitGame: () -> Unit,
) : Table(skin) {

    init {
        setFillParent(true)
        top()

        val banner = Image(skin.getDrawable(GameSkin.BANNER))

        val startBtn = TextButton("Start Game", skin, GameSkin.STYLE_MENU_BTN)
        startBtn.onClick { onStartGame() }

        val quitBtn = TextButton("Quit Game", skin, GameSkin.STYLE_MENU_BTN)
        quitBtn.onClick { onQuitGame() }

        add(banner).pad(40f, 0f, 150f, 0f).row()
        add(startBtn).padBottom(30f).row()
        add(quitBtn)
    }
}
