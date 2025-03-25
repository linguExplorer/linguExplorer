package com.github.linguExplorer.system


import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.*
import com.github.linguExplorer.event.GameEndEvent
import com.github.linguExplorer.event.Interact
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.event.startDialogEvent
import com.github.quillraven.fleks.*
import kotlin.math.sqrt

@AnyOf([SdComponent::class, PlayerComponent::class])
class InteractSystem(
    world: World,
    private val sdComponent: ComponentMapper<SdComponent>,
    private val phComponent: ComponentMapper<PhysicComponent>,
    private val playerCmps: ComponentMapper<PlayerComponent>,
    private val SpawnCmps: ComponentMapper<SpawnComponent>,
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val gameStage: Stage,

    ) : IteratingSystem(), EventListener {

    private var playerx = 0f
    private var playery = 0f

    // Map zur Speicherung der Positionen jeder Entität mit SdComponent
    private val sdPositions = mutableMapOf<Entity, Pair<Float, Float>>()

    private val interactionDistance = 2.0f

    override fun onTickEntity(entity: Entity) {
        if (playerCmps.contains(entity)) {
            with(phComponent[entity]) {
                playerx = body.position.x
                playery = body.position.y
            }
        }

        if (sdComponent.contains(entity)) {
            with(phComponent[entity]) {


                // Aktualisiere die Position der Entität in der Map
                sdPositions[entity] = body.position.x to body.position.y
            }
        }
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x2 - x1
        val dy = y2 - y1
        return sqrt(dx * dx + dy * dy)
    }

    override fun handle(event: Event): Boolean {
        if (event is Interact) {
            // Überprüfe die Distanz zwischen dem Spieler und jeder Entität mit SdComponent
            for ((entity, position) in sdPositions) {
                val (sdPosx, sdPosy) = position
                val distance = calculateDistance(playerx, playery, sdPosx, sdPosy)
                if (distance <= interactionDistance) {

                    gameStage.fire(startDialogEvent(entity,"tets"  ))
                    println("Interaktion erfolgreich mit Entität $entity bei ($sdPosx, $sdPosy)")
                }
            }
            return true
        }
        return false
    }
}
