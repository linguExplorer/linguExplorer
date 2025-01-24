package com.github.linguExplorer.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.linguExplorer

fun Stage.fire(event: Event) {
    this.root.fire(event)
}

data class MapChangeEvent(val map:TiledMap) : Event()

class CollisionDespawnEvent(val cell: Cell) : Event()

class GameChangeEvent(val game: linguExplorer) : Event()
