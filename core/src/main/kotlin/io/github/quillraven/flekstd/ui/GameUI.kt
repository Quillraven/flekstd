package io.github.quillraven.flekstd.ui

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.ObjectMap

class GameUI(
    skin: Skin,
    onTowerClicked: (towerKey: String) -> Unit,
    onSpawnClicked: (enemies: ObjectMap<String, Int>) -> Unit,
) : Table(skin) {
    private val spawnWaveTable: SpawnWaveTable

    init {
        setFillParent(true)
        pad(10f).bottom()
        add(TowerTable(skin, onTowerClicked)).left().bottom()
        spawnWaveTable = SpawnWaveTable(skin, onSpawnClicked)
        add(spawnWaveTable).center().bottom().expandX()
        add(EnemyTable(skin)).right().bottom()
        pack()
    }

    fun enableSpawning() {
        spawnWaveTable.enableSpawning()
    }
}
