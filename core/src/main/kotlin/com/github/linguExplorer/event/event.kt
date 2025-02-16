package com.github.linguExplorer.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.linguExplorer
import kotlin.reflect.jvm.internal.impl.incremental.components.Position

fun Stage.fire(event: Event) {
    this.root.fire(event)
}

data class MapChangeEvent(val map:TiledMap) : Event()

class CollisionDespawnEvent(val cell: Cell) : Event()

class GameChangeEvent(val game: linguExplorer) : Event()

class ActivateKeyEvent() : Event()

class GameEndEvent(val game: String) : Event()

class ClickDownEvent(val mouseX : Float, val mouseY: Float) : Event()
