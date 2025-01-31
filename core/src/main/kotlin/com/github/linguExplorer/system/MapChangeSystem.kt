package com.github.linguExplorer.system

import com.badlogic.gdx.Input
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.*
import com.github.linguExplorer.component.PhysicComponent.Companion.physicCmpFromShape2D
import com.github.linguExplorer.event.ActivateKeyEvent
import com.github.linguExplorer.event.GameChangeEvent
import com.github.linguExplorer.event.MapChangeEvent
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.input.PlayerKeyboardInputProcessor
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.screen.LoadingScreen
import com.github.linguExplorer.screen.MainMenuScreen
import com.github.linguExplorer.screen.MapScreen
import com.github.linguExplorer.screen.MinigameEssenScreen
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.math.component1
import ktx.math.component2

import ktx.tiled.id
import ktx.tiled.layer
import ktx.tiled.property
import ktx.tiled.shape
import java.util.*
import kotlin.concurrent.schedule
import kotlin.reflect.KClass

@AllOf([MGComponent::class])
class MapChangeSystem (
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val  phWorld: World,
    private val mgCmps: ComponentMapper<MGComponent>,
    private val phCmps: ComponentMapper<PhysicComponent>,
    private val gameStage: Stage,
    private val game: linguExplorer,

    ): IteratingSystem(), EventListener {

    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private var currentMap: TiledMap? = null;

    val cfg = cachedCfgs.getOrPut("Player") { SpawnCfg(AnimationModel.PLAYER) }
    var key = false;

    override fun onTickEntity(entity: Entity) {
        val (id, toGame, triggerEntities) = mgCmps[entity]
        if(triggerEntities.isNotEmpty()) {
            println("Collision to mini Game")


            if (key) {
               println("ja")
               game.setScreen<LoadingScreen>()
           }



            triggerEntities.clear()
        }
    }




 fun setMap(game : linguExplorer?) {
        currentMap?.disposeSafely()



         if (game!!.containsScreen<MinigameEssenScreen>()) {
             game.removeScreen<MinigameEssenScreen>()
          }
     game.addScreen(MinigameEssenScreen(game))
     game.setScreen<MinigameEssenScreen>()

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

        if (event is ActivateKeyEvent) {

                key = true
              //  println("ActivateKeyEvent: key = true")

        } else {
                key = false
              //  println("Andere Ereignisse: key = false")

        }

        return false
    }

}
