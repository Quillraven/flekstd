package io.github.quillraven.flekstd.ui

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onClickEvent

class TowerTable(
    skin: Skin,
    private val onTowerClicked: (towerKey: String) -> Unit
) : Table(skin) {
    init {
        background = skin.getDrawable(GameSkin.RIBBON_YELLOW)

        val towers = listOf(
            GameSkin.STYLE_WARRIOR_BTN to "warrior",
            GameSkin.STYLE_ARCHER_BTN to "archer",
            GameSkin.STYLE_MONK_BTN to "monk",
        )
        towers.forEach { [styleName, towerKey] ->
            // selection cursor on top of tower avatar
            val cursor = Image(skin.getDrawable(GameSkin.CURSOR_SELECT))
            cursor.touchable = Touchable.disabled
            cursor.isVisible = false
            cursor.addAction(
                Actions.forever(
                    Actions.sequence(
                        Actions.parallel(
                            Actions.scaleBy(0.15f, 0.15f, 0.25f),
                            Actions.moveBy(-5f, -5f, 0.25f)
                        ),
                        Actions.parallel(
                            Actions.scaleBy(-0.15f, -0.15f, 0.25f),
                            Actions.moveBy(5f, 5f, 0.25f)
                        ),
                    )
                )
            )

            // tower button
            val towerBtn = ImageButton(skin, styleName)
            towerBtn.onClickEvent { event ->
                event.handle() // avoid calling ConstructionSystem
                onTowerButtonClicked(towerBtn, towerKey)
            }

            val stack = Stack(towerBtn, cursor)
            add(stack).size(80f).padBottom(10f)
        }
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