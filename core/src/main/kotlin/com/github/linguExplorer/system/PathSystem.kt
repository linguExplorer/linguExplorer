package com.github.linguExplorer.system

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.*
import com.github.linguExplorer.component.PhysicComponent.Companion.physicCmpFromShape2D
import com.github.linguExplorer.event.ActivateKeyEvent
import com.github.linguExplorer.event.MapChangeEvent
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.screen.LoadingScreen
import com.github.linguExplorer.screen.MinigameEssenScreen
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.assets.disposeSafely
import ktx.tiled.id
import ktx.tiled.layer
import ktx.tiled.property
import ktx.tiled.shape

@AllOf([PathComponent::class])
class PathSystem (
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val  phWorld: World,
    private val pathCmps: ComponentMapper<PathComponent>,
    private val phCmps: ComponentMapper<PhysicComponent>,
    private val gameStage: Stage,
    private val game: linguExplorer,

    ): IteratingSystem(), EventListener {



    override fun onTickEntity(entity: Entity) {
        val (id, path, triggerEntities) = pathCmps[entity]
        if(triggerEntities.isNotEmpty()) {

            println("Collision with Path $path")



            triggerEntities.clear()
        }
    }



    override fun handle(event: Event): Boolean {
        if (event is MapChangeEvent) {

            val tlayer =  event.map.layer("path")
            tlayer.objects.forEach {
                    mapObj -> val path = mapObj.property("path","")

                if(path.isBlank()) {
                    return@forEach
                }

                world.entity {
                    add<PathComponent> {
                        this.id = mapObj.id
                        this.path = path
                    }
                    physicCmpFromShape2D(phWorld, 0,0, mapObj.shape, true)
                }
            }

            return true
        }


        return false
    }

}
