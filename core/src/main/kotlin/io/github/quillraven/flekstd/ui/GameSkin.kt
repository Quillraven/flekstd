package io.github.quillraven.flekstd.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import ktx.assets.toInternalFile
import ktx.freetype.generateFont

class GameSkin : Skin() {
    init {
        // generate  and register fonts
        val ftfGenerator = FreeTypeFontGenerator("ui/alagard.ttf".toInternalFile())
        add(FONT_SMALL, ftfGenerator.generateFont { size = 32 })
        add(FONT_DEFAULT, ftfGenerator.generateFont { size = 48 })
        ftfGenerator.dispose()

        // register textures
        add(RIBBON_GRAY, Texture("ui/ribbon_gray.png"))
        add(RIBBON_YELLOW, Texture("ui/ribbon_yellow.png"))
        add(RIBBON_TEAL, Texture("ui/ribbon_small_teal.png"))
        add(SWORD_PURPLE, Texture("ui/sword_purple.png"))
        add(BANNER, Texture("ui/banner.png"))
        add(AVATAR_WARRIOR, Texture("ui/avatar_warrior.png"))
        add(AVATAR_ARCHER, Texture("ui/avatar_archer.png"))
        add(AVATAR_MONK, Texture("ui/avatar_monk.png"))
        add(AVATAR_PAWN, Texture("ui/avatar_pawn.png"))
        add(AVATAR_LANCER, Texture("ui/avatar_lancer.png"))
        add(CURSOR_SELECT, Texture("ui/cursor_select.png"))
        add(ARROW_GREEN_UP, Texture("ui/arrow_green_up.png"))
        add(ARROW_GREEN_DOWN, Texture("ui/arrow_green_down.png"))

        // register styles
        add(STYLE_LABEL, LabelStyle(getFont(FONT_SMALL), Color.WHITE))
        add(STYLE_SPAWN_BTN, TextButtonStyle().apply {
            font = getFont(FONT_DEFAULT)
            fontColor = Color.WHITE
        })
        add(STYLE_ARROW_UP_BTN, ImageButtonStyle().apply { imageUp = getDrawable(ARROW_GREEN_UP) })
        add(STYLE_ARROW_DOWN_BTN, ImageButtonStyle().apply { imageUp = getDrawable(ARROW_GREEN_DOWN) })
        add(STYLE_WARRIOR_BTN, ImageButtonStyle().apply { imageUp = getDrawable(AVATAR_WARRIOR) })
        add(STYLE_ARCHER_BTN, ImageButtonStyle().apply { imageUp = getDrawable(AVATAR_ARCHER) })
        add(STYLE_MONK_BTN, ImageButtonStyle().apply { imageUp = getDrawable(AVATAR_MONK) })
        add(STYLE_MENU_BTN, TextButtonStyle().apply {
            font = getFont(FONT_DEFAULT)
            fontColor = Color.BLACK
            up = getDrawable(SWORD_PURPLE)
        })
    }

    companion object {
        // graphics
        const val RIBBON_GRAY = "ribbon_gray"
        const val RIBBON_YELLOW = "ribbon_yellow"
        const val RIBBON_TEAL = "ribbon_teal"
        const val SWORD_PURPLE = "sword_purple"
        const val BANNER = "banner"
        private const val AVATAR_WARRIOR = "avatar_warrior"
        private const val AVATAR_ARCHER = "avatar_archer"
        private const val AVATAR_MONK = "avatar_monk"
        const val AVATAR_PAWN = "avatar_pawn"
        const val AVATAR_LANCER = "avatar_lancer"
        const val CURSOR_SELECT = "cursor_select"
        private const val ARROW_GREEN_UP = "arrow_green_up"
        private const val ARROW_GREEN_DOWN = "arrow_green_down"

        // fonts
        private const val FONT_SMALL = "font_small"
        private const val FONT_DEFAULT = "font_default"

        // style names
        const val STYLE_LABEL = "default"
        const val STYLE_SPAWN_BTN = "spawn"
        const val STYLE_ARROW_UP_BTN = "arrow_up"
        const val STYLE_ARROW_DOWN_BTN = "arrow_down"
        const val STYLE_WARRIOR_BTN = "warrior"
        const val STYLE_ARCHER_BTN = "archer"
        const val STYLE_MONK_BTN = "monk"
        const val STYLE_MENU_BTN = "menu"
    }
}
