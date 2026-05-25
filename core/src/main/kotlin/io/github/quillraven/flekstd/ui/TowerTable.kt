package io.github.quillraven.flekstd.ui

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onClick
import ktx.actors.onClickEvent

class TowerTable(
    skin: Skin,
    private val onTowerClicked: (towerKey: String) -> Unit
) : Table(skin) {
    init {
        background = skin.getDrawable(GameSkin.RIBBON_YELLOW)
        pad(-20f, 0f, -10f, 100f)

        val towers = listOf(
            GameSkin.STYLE_WARRIOR_BTN to "warrior",
            GameSkin.STYLE_ARCHER_BTN to "archer",
            GameSkin.STYLE_MONK_BTN to "monk",
        )
        towers.forEachIndexed { index, [styleName, towerKey] ->
            // selection cursor on top of tower avatar
            val cursor = Image(skin.getDrawable(GameSkin.CURSOR_SELECT))
            cursor.isVisible = false
            cursor.setScale(0.5f)
            cursor.addAction(
                Actions.forever(
                    Actions.sequence(
                        Actions.parallel(
                            Actions.scaleTo(0.65f, 0.65f, 0.25f),
                            Actions.moveBy(-5f, -5f, 0.25f)
                        ),
                        Actions.parallel(
                            Actions.scaleTo(0.5f, 0.5f, 0.25f),
                            Actions.moveBy(5f, 5f, 0.25f)
                        ),
                    )
                )
            )

            // tower button
            val towerBtn = ImageButton(skin, styleName)
            towerBtn.imageCell.size(80f, 80f)
            towerBtn.onClickEvent { event ->
                event.handle()
                onTowerButtonClicked(towerBtn, towerKey)
            }

            val stack = Stack(towerBtn, cursor)
            stack.pack()
            stack.getChild(1).moveBy(30f, 30f)

            add(stack).padRight(-40f).padLeft(if (index == 0) 70f else 0f)
        }

        pack()
    }

    private fun onTowerButtonClicked(towerBtn: ImageButton, towerKey: String) {
        // hide all selection cursors
        this.children.filterIsInstance<Stack>().forEach { stack ->
            stack.getChild(1).isVisible = false
        }
        // show current selection
        towerBtn.parent.getChild(1).isVisible = true

        // call lambda
        this.onTowerClicked(towerKey)
    }
}