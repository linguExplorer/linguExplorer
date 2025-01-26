package com.github.linguExplorer.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.MoveComponent
import com.github.linguExplorer.component.PhysicComponent
import com.github.linguExplorer.component.PlayerComponent
import com.github.linguExplorer.event.ActivateKeyEvent
import com.github.linguExplorer.event.fire
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2


class PlayerKeyboardInputProcessor(
    world: World,
    private val gameStage: Stage,
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val phCmps: ComponentMapper<PhysicComponent>,
    private val stage: Stage

) : KtxInputAdapter {

    private var playerSin = 0f
    private var playerCos = 0f
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    init {
        Gdx.input.inputProcessor = this

    }

    private fun Int.isMovementKey(): Boolean {
        return this == Input.Keys.UP || this == Input.Keys.DOWN || this == Input.Keys.LEFT || this == Input.Keys.RIGHT
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

        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> {
                    playerSin = 1f
                    gameStage.fire(ActivateKeyEvent())
                }

                DOWN -> playerSin = -1f
                RIGHT -> playerCos = 1f
                LEFT -> playerCos = -1f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {

        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSin = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                DOWN -> playerSin = if (Gdx.input.isKeyPressed(UP)) 1f else 0f
                RIGHT -> playerCos = if (Gdx.input.isKeyPressed(LEFT)) -1f else 0f
                LEFT -> playerCos = if (Gdx.input.isKeyPressed(RIGHT)) 1f else 0f
            }
            updatePlayerMovement()
            return true
        }
        return false

    }


    //Klick Steuerung
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {


        if (button == Input.Buttons.LEFT) {

            playerEntities.forEach { player ->
                val playerPosition = phCmps[player].body.position
                val (playerX, playerY) = playerPosition


                val mousePosition = stage.viewport.unproject(vec2(screenX.toFloat(), screenY.toFloat()))
                val mouseWorldX = mousePosition.x
                val mouseWorldY = mousePosition.y

                println("Mouse: $mouseWorldX, $mouseWorldY Spieler: $playerX, $playerY")
                // Richtung berechnen
                val deltaX = mouseWorldX - playerX
                val deltaY = mouseWorldY - playerY

                val distance = kotlin.math.sqrt(deltaX * deltaX + deltaY * deltaY)


                if (distance > 0.1f) {
                    playerCos = deltaX / distance
                    playerSin = deltaY / distance
                } else {
                    playerCos = 0f
                    playerSin = 0f
                }

                println("Bewegung Richtung Maus: cos=$playerCos, sin=$playerSin, Distabce: $distance")
            }

        }
        updatePlayerMovement()

        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button == Input.Buttons.LEFT) {
            println("Linke Maustaste losgelassen bei ($screenX, $screenY)")
        }
        updatePlayerMovement()

        return true

    }

}
