package io.github.quillraven.flekstd.ui

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick

class SpawnWaveTable(skin: Skin) : Table(skin) {
    private val btn: TextButton

    init {
        background = skin.getDrawable(GameSkin.RIBBON_TEAL)
        pad(5f, 60f, 15f, 60f)

        btn = TextButton("Spawn Wave", skin, GameSkin.STYLE_SPAWN_BTN).apply {
            label.setAlignment(Align.center)
            onClick { onSpawnClicked() }
        }
        add(btn)

        pack()
    }

    private fun onSpawnClicked() {
        btn.isDisabled = true
        btn.touchable = Touchable.disabled
        println("Spawn Wave clicked")
    }
}