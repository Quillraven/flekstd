package io.github.quillraven.flekstd.ui

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table

class GameUI(skin: Skin) : Table(skin) {
    init {
        setFillParent(true)
        pad(10f).bottom()
        add(TowerTable(skin)).left().bottom()
        add(SpawnWaveTable(skin)).center().bottom().expandX()
        add(EnemyTable(skin)).right().bottom()
        pack()
    }
}
