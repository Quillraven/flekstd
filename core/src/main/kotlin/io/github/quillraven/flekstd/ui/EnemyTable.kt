package io.github.quillraven.flekstd.ui

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onClick

class EnemyTable(skin: Skin) : Table(skin) {
    init {
        background = skin.getDrawable(GameSkin.RIBBON_GRAY)
        pad(-20f, 120f, -10f, 120f)

        val enemies = listOf(
            Triple(GameSkin.AVATAR_PAWN, "pawn", 5),
            Triple(GameSkin.AVATAR_LANCER, "lancer", 1),
        )
        enemies.forEach { (styleName, enemyKey, count) -> add(enemyTable(enemyKey, styleName, count)) }

        pack()
    }

    private fun enemyTable(
        enemyKey: String,
        avatarKey: String,
        count: Int,
    ): Table {
        val countLabel = Label("$count", skin, GameSkin.STYLE_LABEL).apply {
            userObject = enemyKey
        }
        val arrowDown = ImageButton(skin, GameSkin.STYLE_ARROW_DOWN_BTN)
        arrowDown.onClick {
            val current = countLabel.text.toString().toInt()
            countLabel.setText((current - 1).coerceAtLeast(0))
        }
        val arrowUp = ImageButton(skin, GameSkin.STYLE_ARROW_UP_BTN)
        arrowUp.onClick {
            val current = countLabel.text.toString().toInt()
            countLabel.setText((current + 1).coerceAtMost(99))
        }

        return Table(skin).apply {
            add(Image(skin.getDrawable(avatarKey))).size(80f, 80f)
            add(arrowDown).size(36f, 36f).padLeft(-15f)
            add(countLabel).width(30f)
            add(arrowUp).size(36f, 36f)
        }
    }
}