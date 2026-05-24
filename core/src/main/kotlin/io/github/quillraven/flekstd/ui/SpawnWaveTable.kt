package io.github.quillraven.flekstd.ui

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.ObjectMap
import ktx.actors.onClick

class SpawnWaveTable(
    skin: Skin,
    private val onSpawnClicked: (enemies: ObjectMap<String, Int>) -> Unit
) : Table(skin) {
    private val btn: TextButton

    init {
        background = skin.getDrawable(GameSkin.RIBBON_TEAL)
        pad(5f, 60f, 15f, 60f)

        btn = TextButton("Spawn Wave", skin, GameSkin.STYLE_SPAWN_BTN).apply {
            label.setAlignment(Align.center)
            onClick { onSpawnButtonClicked() }
        }
        add(btn)

        pack()
    }

    private fun onSpawnButtonClicked() {
        btn.isDisabled = true
        btn.touchable = Touchable.disabled
        btn.color.a = 0.5f

        val enemyTable = this.parent.children.single { it is EnemyTable } as EnemyTable
        val enemyInfo: ObjectMap<String, Int> = enemyTable.getEnemyInfo()
        this.onSpawnClicked(enemyInfo)
    }

    fun enableSpawning() {
        btn.isDisabled = false
        btn.touchable = Touchable.enabled
        btn.color.a = 1f
    }
}