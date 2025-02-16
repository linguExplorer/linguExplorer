package com.github.linguExplorer.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.MoveComponent
import com.github.linguExplorer.component.PhysicComponent
import com.github.linguExplorer.component.PlayerComponent
import com.github.linguExplorer.event.ActivateKeyEvent
import com.github.linguExplorer.event.ClickDownEvent
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.system.PathSystem
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.world
import kotlinx.coroutines.*
import ktx.app.KtxInputAdapter
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2
import kotlin.math.sqrt


class PlayerKeyboardInputProcessor(
    world: World,
    private val gameStage: Stage,
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val phCmps: ComponentMapper<PhysicComponent>,
    private val stage: Stage,
    private val pathSys : PathSystem

) : KtxInputAdapter {

    private var playerSin = 0f
    private var playerCos = 0f
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    private var isClick = false

    private var mouseX: Float = 0f
    private var mouseY: Float = 0f
    private var isCheckingDistance: Boolean = false
    private var dist  = 0f
    private var currentPath: List<Pair<Float, Float>>? = null
    private var currentWaypointIndex = 0
    private val waypointThreshold = 0.5f

    init {
        Gdx.input.inputProcessor = this

    }

    private fun Int.isMovementKey(): Boolean {
        return this == Input.Keys.UP || this == Input.Keys.DOWN || this == Input.Keys.LEFT || this == Input.Keys.RIGHT || this == Input.Keys.W
            || this == Input.Keys.S || this == Input.Keys.A || this == Input.Keys.D
    }

    private fun updatePlayerMovement() {
        playerEntities.forEach { player ->
            with(moveCmps[player]) {
                cos = playerCos
                sin = playerSin
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {

if (isCheckingDistance) {

    playerEntities.forEach { player ->

        playerCos = 0f
        playerSin = 0f

        phCmps[player].body.linearVelocity = vec2(0f,0f)
    }

    isCheckingDistance = false

}

        if (keycode.isMovementKey()) {
            when (keycode) {
                UP, W -> {
                    playerSin = 1f
                    gameStage.fire(ActivateKeyEvent())
                }

                DOWN, S -> playerSin = -1f
                RIGHT, D -> playerCos = 1f
                LEFT, A -> playerCos = -1f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {

        if (keycode.isMovementKey()) {
            when (keycode) {
                UP, W -> playerSin = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                DOWN, S -> playerSin = if (Gdx.input.isKeyPressed(UP)) 1f else 0f
                RIGHT, D  -> playerCos = if (Gdx.input.isKeyPressed(LEFT)) -1f else 0f
                LEFT, A  -> playerCos = if (Gdx.input.isKeyPressed(RIGHT)) 1f else 0f
            }
            updatePlayerMovement()
            return true
        }
        return false

    }


    //Klick Steuerung


    @OptIn(DelicateCoroutinesApi::class)
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {




        if (button == Input.Buttons.LEFT) {


            isCheckingDistance = true
            GlobalScope.launch {
                checkDistanceContinuously()
            }


            val mousePosition = stage.viewport.unproject(vec2(screenX.toFloat(), screenY.toFloat()))
            mouseX= mousePosition.x
            mouseY = mousePosition.y

            println("Mouse: $mouseX, $mouseY ")

            stage.fire(ClickDownEvent(mouseX, mouseY))



        }

        return true
    }



        suspend fun checkDistanceContinuously() {
            while (isCheckingDistance) {
                playerEntities.forEach { player ->
                    val playerPosition = phCmps[player].body.position
                    val (playerX, playerY) = playerPosition

                    // Distanz berechnen
                    val deltaX = mouseX - playerX
                    val deltaY = mouseY - playerY
                    dist = kotlin.math.sqrt(deltaX * deltaX + deltaY * deltaY)


                    if (dist > 0.5f) {
                        playerCos = deltaX / dist
                        playerSin = deltaY / dist
                    } else {
                        playerCos = 0f
                        playerSin = 0f

                        phCmps[player].body.linearVelocity = vec2(0f,0f)

                        isCheckingDistance = false
                    }


                }
                updatePlayerMovement()

                delay(1)
                yield()
            }
        }



    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button == Input.Buttons.LEFT) {
            println("Linke Maustaste losgelassen bei ($screenX, $screenY)")
        }
        updatePlayerMovement()

        return true

    }

}
