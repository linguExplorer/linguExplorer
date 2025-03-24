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

class ActivateKeyEvent() : Event()

class GameEndEvent(val game: String) : Event()

class ClickDownEvent(val mouseX : Float, val mouseY: Float) : Event()

class GamePause(val game: linguExplorer) : Event()


class Interact() : Event()
//Events für locked und celebrate

class LockScreenEvent(val miniGame: String) : Event ()

class UnlockedEvent(val type: String) : Event()

