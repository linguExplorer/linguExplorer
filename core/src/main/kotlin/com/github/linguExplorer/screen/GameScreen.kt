package com.github.linguExplorer.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class GameScreen : Screen {

    private val batch = SpriteBatch()

    private val strawberry = Texture(Gdx.files.internal("assets/Minigames/Essen/phraseImages/test_strawberry.png"))
    private val basket = Texture(Gdx.files.internal("assets/Minigames/Essen/phraseImages/pixel_basket.jpg"))

    private var strawberryX = 200f
    private var strawberryY = 200f

    private val basketX = 300f
    private val basketY = 300f

    private var isDragging = false
    private var offsetX = 0f
    private var offsetY = 0f
    private var isCollected = false
    private var animationAlpha = 1f

    override fun show() {}

    override fun render(delta: Float) {
        handleInput()

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(basket, basketX, basketY)

        if (!isCollected) {
            batch.draw(strawberry, strawberryX, strawberryY)
        } else {
            if (animationAlpha > 0) {
                batch.setColor(1f, 1f, 1f, animationAlpha)
                batch.draw(strawberry, strawberryX, strawberryY)
                batch.setColor(1f, 1f, 1f, 1f)
                animationAlpha -= delta
            }
        }



        batch.end()
    }

    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat()
        val mouseY = Gdx.graphics.height - Gdx.input.y.toFloat()

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!isDragging && isMouseInsideImage(mouseX, mouseY, strawberryX, strawberryY, strawberry)) {
                isDragging = true
                offsetX = mouseX - strawberryX
                offsetY = mouseY - strawberryY
            }

            if (isDragging && !isCollected) {
                strawberryX = mouseX - offsetX
                strawberryY = mouseY - offsetY
            }
        } else {
            if (isDragging) {
                // Überprüfen, ob Erdbeere im Korb ist
                if (isMouseInsideImage(strawberryX, strawberryY, basketX, basketY, basket)) {
                    isCollected = true // Sammelstatus setzen
                }
            }
            isDragging = false
        }
    }

    private fun isMouseInsideImage(mouseX: Float, mouseY: Float, imageX: Float, imageY: Float, image: Texture): Boolean {
        return mouseX in imageX..(imageX + image.width) && mouseY in imageY..(imageY + image.height)
    }

    override fun resize(width: Int, height: Int) {}

    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        batch.dispose()
        strawberry.dispose()
        basket.dispose()
    }
}
