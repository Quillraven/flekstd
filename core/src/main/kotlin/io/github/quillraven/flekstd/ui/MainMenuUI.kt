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
        center()

        val banner = Image(skin.getDrawable(GameSkin.BANNER))
        val startBtn = TextButton("Start Game", skin, GameSkin.STYLE_MENU_BTN)
        val quitBtn = TextButton("Quit Game", skin, GameSkin.STYLE_MENU_BTN)

        startBtn.labelCell.padLeft(40f)
        quitBtn.labelCell.padLeft(40f)

        startBtn.onClick { onStartGame() }
        quitBtn.onClick { onQuitGame() }

        add(banner).pad(10f).padBottom(150f).row()
        add(startBtn).pad(10f).row()
        add(quitBtn).pad(10f)
        pack()
    }
}
