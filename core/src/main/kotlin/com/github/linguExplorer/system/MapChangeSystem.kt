package com.github.linguExplorer.system

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.*
import com.github.linguExplorer.component.PhysicComponent.Companion.physicCmpFromShape2D
import com.github.linguExplorer.database.checkIfTopicAvailable
import com.github.linguExplorer.event.*
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.saveNumber
import com.github.linguExplorer.screen.*
import com.github.linguExplorer.userId
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
    private val lastTriggerTimes = mutableMapOf<String, Float>()
    private  val COOLDOWN = 5f

    override fun onTickEntity(entity: Entity) {
        val (id, toGame, topicId, triggerEntities) = mgCmps[entity]


        if (triggerEntities.isNotEmpty()) {
            val currentTime = System.currentTimeMillis() / 1000f // Aktuelle Zeit in Sekunden
            val lastTriggerTime = lastTriggerTimes[toGame] ?: 0f

            // Prüfe ob Cooldown abgelaufen ist
            if (currentTime - lastTriggerTime >= COOLDOWN) {
                lastTriggerTimes[toGame] = currentTime // Update Triggerzeit

                println("Collision to $toGame and $topicId")

                if (!checkIfTopicAvailable(userId, saveNumber, topicId)) {
                    gameStage.fire(LockScreenEvent(toGame))
                    triggerEntities.clear()
                    return
                }

                when (toGame) {
                    "SM" -> {
                        game.removeScreen<MinigameEssenScreen>()
                        game.addScreen(MinigameEssenScreen(game, gameStage))
                        game.setScreen<MinigameEssenScreen>()
                    }
                    "CL" -> {
                        game.removeScreen<MinigameKleidungScreen>()
                        game.addScreen(MinigameKleidungScreen(game))
                        game.setScreen<MinigameKleidungScreen>()
                    }
                    "FM" -> {
                        game.removeScreen<MinigameFamilieScreen>()
                        game.addScreen(MinigameFamilieScreen(game))
                        game.setScreen<MinigameFamilieScreen>()
                    }

                    "SC" -> {
                        game.removeScreen<MinigameSchuleScreen>()
                        game.addScreen(MinigameSchuleScreen())
                        game.setScreen<MinigameSchuleScreen>()
                    }
                }
                lastTriggerTimes[toGame] = currentTime

            }

            triggerEntities.clear()
        }
    }




 fun setMap(game : linguExplorer?) {
        currentMap?.disposeSafely()



         if (game!!.containsScreen<MinigameEssenScreen>()) {
             game.removeScreen<MinigameEssenScreen>()
          }
     game.addScreen(MinigameEssenScreen(game, gameStage))
     game.setScreen<MinigameEssenScreen>()

    }

    override fun handle(event: Event): Boolean {
        if (event is MapChangeEvent) {

            lastTriggerTimes.clear()

            val tlayer =  event.map.layer("trigger")
            tlayer.objects.forEach {
                mapObj -> val toGame = mapObj.property("toGame","")
                val topicId= mapObj.property("topicId",0)

                if(toGame.isBlank()) {
                    return@forEach
                }

                world.entity {
                    add<MGComponent> {
                        this.id = mapObj.id
                        this.toGame = toGame
                        this.topicId = topicId
                    }
                   physicCmpFromShape2D(phWorld, 0,0, mapObj.shape, true)
                }
            }

            return true
        }

        if (event is GameEndEvent) {
            println("Spiel beendet neuer Spawn")
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
