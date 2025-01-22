package com.github.linguExplorer.system

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.MGComponent
import com.github.linguExplorer.component.PhysicComponent
import com.github.linguExplorer.component.PhysicComponent.Companion.physicCmpFromShape2D
import com.github.linguExplorer.event.MapChangeEvent
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.screen.MainMenuScreen
import com.github.linguExplorer.screen.MapScreen
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.assets.disposeSafely

import ktx.tiled.id
import ktx.tiled.layer
import ktx.tiled.property
import ktx.tiled.shape

@AllOf([MGComponent::class])
class MapChangeSystem (
    private val  phWorld: World,
    private val mgCmps: ComponentMapper<MGComponent>,
  // private val game: linguExplorer?,

    ): IteratingSystem(), EventListener {


        private var game : linguExplorer? = null;
    private var currentMap: TiledMap? = null;

    override fun onTickEntity(entity: Entity) {
        val (id, toGame, triggerEntities) = mgCmps[entity]
        if(triggerEntities.isNotEmpty()) {
            println("Collision to mini Game")
            setMap()

            triggerEntities.clear()
        }
    }

 fun setMap() {
        currentMap?.disposeSafely()
        game?.addScreen(MainMenuScreen(game!!))
        game?.setScreen<MainMenuScreen>()

    }

    override fun handle(event: Event): Boolean {
        if (event is MapChangeEvent) {

            val tlayer =  event.map.layer("trigger")
            tlayer.objects.forEach {
                mapObj -> val toGame = mapObj.property("toGame","")

                if(toGame.isBlank()) {
                    return@forEach
                }

                world.entity {
                    add<MGComponent> {
                        this.id = mapObj.id
                        this.toGame = toGame
                    }
                   physicCmpFromShape2D(phWorld, 0,0, mapObj.shape, true)
                }
            }

            return true
        }
        return false
    }

}
