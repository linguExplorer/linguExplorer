package com.github.linguExplorer.screen

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.repositories.PhraseProgressRepository
import ktx.app.KtxScreen
import ktx.assets.disposeSafely

class PhrasenheftScreen (
): KtxScreen {
    private var font = BitmapFont()
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()

    private enum class SortState {
        ASCENDING_PHRASE, DESCENDING_PHRASE, ASCENDING_TRANSLATION, DESCENDING_TRANSLATION
    }
    private var currentSortState = SortState.ASCENDING_PHRASE
    private var sortText = "Phrase aufsteigend"

    private val phrasesOfProgress = PhraseProgressRepository().getAllPhrasesOfUserProgress(123)
    private var phrases = phrasesOfProgress.map {
        it.phrase to it.translation

    }

    private val lineHeight = 56f
    private val spacing = 260f
    private val padding = 20f
    private var currentY = 778f




    // Texturen
    private val heftTexture = Texture(Gdx.files.internal("Phrasenheft/heft_design.png"))
    private val sortTexture = Texture(Gdx.files.internal("Phrasenheft/Sort.png"))
    private val nextTexture = Texture(Gdx.files.internal("Phrasenheft/weiter.png"))
    private val backTexture = Texture(Gdx.files.internal("Phrasenheft/zurueck.png"))
    private val closeTexture = Texture(Gdx.files.internal("Phrasenheft/red_X.png"))


    private val heftSize = Vector2(heftTexture.width.toFloat()*8, heftTexture.height.toFloat()*8)
    private val nextSize = Vector2(backTexture.width.toFloat()*8, backTexture.height.toFloat()*8)
    private val backSize = Vector2(backTexture.width.toFloat()*8, backTexture.height.toFloat()*8)
    private val sortSize = Vector2(sortTexture.width.toFloat()*6, sortTexture.height.toFloat()*6)
    private val closeSize = Vector2(closeTexture.width.toFloat()*1.25f, closeTexture.height.toFloat()*1.25f)


    private val viewport: Viewport = ExtendViewport(800f, 600f)


    private var maxPages= (phrases.size/10f)//wie viele Phrasen max
    private var currentPage = 1

    private val nextPosition: Vector2
        get() = Vector2(
            viewport.screenWidth/2 + 600f,
            (viewport.screenHeight - heftSize.y) - 200f
        )


    private val backPosition: Vector2
        get() = Vector2(
            viewport.screenWidth/2 - 690f,
            (viewport.screenHeight  - heftSize.y) - 200f
        )


    private val sortPosition: Vector2
        get() = Vector2(
            viewport.screenWidth/2 - 690f,
            (viewport.screenHeight/2f + heftSize.y/2f) - sortSize.y
        )

    private val closePosition: Vector2
        get() = Vector2(
            viewport.screenWidth.toFloat() - 150f,
            (viewport.screenHeight - 50f) - sortSize.y
        )


    private val textX = sortPosition.x
    private val textY = sortPosition.y
    override fun show() {

        Gdx.input.inputProcessor = null

        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        viewport.camera.position.set(viewport.worldWidth / 2, viewport.worldHeight / 2, 0f)
        viewport.camera.update()
    }

    override fun render(delta: Float) {
        handleInput()

        viewport.apply()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        font = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
        val layout = GlyphLayout()
        font.color = Color.BLACK
        font.data.setScale(0.3f, 0.3f)

        val screenHeight = viewport.screenHeight
        val screenWidth = viewport.screenWidth

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(156 / 255f, 194 / 255f, 211 / 255f, 1f)
        shapeRenderer.rect(padding, padding, screenWidth - padding * 2, screenHeight - padding * 2)
        shapeRenderer.end()

        batch.end()

        batch.begin()

        val heftX = screenWidth / 2f - heftSize.x / 2f
        val heftY = screenHeight / 2f - heftSize.y / 2f
        batch.draw(heftTexture, heftX, heftY, heftSize.x, heftSize.y)

        if ((currentPage - 1) > 0) {
            batch.draw(backTexture, backPosition.x, backPosition.y, backSize.x, backSize.y)
        }
        if ((currentPage - 1) + 1 <= maxPages - 1f) {
            batch.draw(nextTexture, nextPosition.x, nextPosition.y, backSize.x, backSize.y)
        }
        batch.draw(sortTexture, sortPosition.x, sortPosition.y, sortSize.x, sortSize.y)
        batch.draw(closeTexture, closePosition.x, closePosition.y, closeSize.x, closeSize.y)


        val startX = screenWidth / 4f + 70f
        var adjustedY = currentY

        layout.setText(font, "English")
        var titleTextWidth = layout.width
        var titleX = startX - (titleTextWidth / 2)
        font.draw(batch, "English", titleX, adjustedY + 75f)
        titleX += 550f
        font.draw(batch, "English", titleX, adjustedY + 75f)
        layout.setText(font, "Deutsch")
        titleTextWidth = layout.width
        titleX = startX + spacing - (titleTextWidth / 2)
        font.draw(batch, "Deutsch", titleX, adjustedY + 75f)
        titleX += 550f
        font.draw(batch, "Deutsch", titleX, adjustedY + 75f)

        val phrasesPerPage = 20
        val phrasesPerColumn = 10



        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))
        font.data.setScale(0.2f, 0.2f)
        font.color = Color.BLACK

        for ((index, phrase) in phrases.withIndex()) {
            val pageStartIndex = if (currentPage == 1) {
                0
            } else {
                (currentPage * 10) + 2
            }

            val pageEndIndex = pageStartIndex + phrasesPerPage

            if (index in pageStartIndex..pageEndIndex + 1) {
                val phraseText = phrase.first
                val translationText = phrase.second

                val columnOffset = if ((index - pageStartIndex) <= phrasesPerColumn) {
                    0f
                } else {
                    550f
                }

                layout.setText(font, phraseText)
                val phraseTextWidth = layout.width

                layout.setText(font, translationText)
                val translationTextWidth = layout.width

                val phraseX = startX + columnOffset - (phraseTextWidth / 2)
                font.draw(batch, phraseText, phraseX, adjustedY)

                val translationX = startX + spacing + columnOffset - (translationTextWidth / 2)
                font.draw(batch, translationText, translationX, adjustedY)

                adjustedY -= lineHeight

                if ((index - pageStartIndex) == phrasesPerColumn) {
                    adjustedY = currentY
                }
            }
        }

        val sideText = "Sortiert nach: $sortText"


        //val fontRotationMatrix = batch.transformMatrix.cpy()
        //fontRotationMatrix.setToRotation(0f, 0f, 1f, 90f)
        //batch.transformMatrix = fontRotationMatrix

        font.data.setScale(0.15f, 0.15f)
        layout.setText(font, sideText)
        font.draw(batch, sideText, sortPosition.x, sortPosition.y + sortSize.y + 30f )

       // batch.transformMatrix.idt()

        batch.end()
    }




    private fun handleInput() {
        val mouseX = Gdx.input.x.toFloat() * viewport.screenWidth/ Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.screenHeight / Gdx.graphics.height


        if (Gdx.input.justTouched()) {


                if (mouseX in nextPosition.x..(nextPosition.x + backSize.x) && mouseY in nextPosition.y..(nextPosition.y + backSize.y)
                ) {

                    if((currentPage-1) + 1 <  maxPages-1f) {
                        currentPage++

                    }
                    println("Button Next, $currentPage")

                }

            if (mouseX in backPosition.x..(backPosition.x + backSize.x) && mouseY in backPosition.y..(backPosition.y + backSize.y)
            ) {

                if((currentPage-1) - 1 >=  0) {
                    currentPage--

                }
                println("Button Back, $currentPage")
            }

            if (mouseX in sortPosition.x..(sortPosition.x + sortSize.x) && mouseY in sortPosition.y..(sortPosition.y + sortSize.y)
            ) {
                currentSortState = when (currentSortState) {
                    SortState.ASCENDING_PHRASE -> SortState.DESCENDING_PHRASE
                    SortState.DESCENDING_PHRASE -> SortState.ASCENDING_TRANSLATION
                    SortState.ASCENDING_TRANSLATION -> SortState.DESCENDING_TRANSLATION
                    SortState.DESCENDING_TRANSLATION -> SortState.ASCENDING_PHRASE
                }

                phrases = when (currentSortState) {
                    SortState.ASCENDING_PHRASE -> phrases.sortedBy { it.first }
                    SortState.DESCENDING_PHRASE -> phrases.sortedByDescending { it.first }
                    SortState.ASCENDING_TRANSLATION -> phrases.sortedBy { it.second }
                    SortState.DESCENDING_TRANSLATION -> phrases.sortedByDescending { it.second }
                }

                sortText = when (currentSortState) {
                    SortState.ASCENDING_PHRASE -> "Phrase aufsteigend"
                    SortState.DESCENDING_PHRASE -> "Phrase absteigend"
                    SortState.ASCENDING_TRANSLATION -> "Übersetzung aufsteigend"
                    SortState.DESCENDING_TRANSLATION -> "Übersetzung absteigend"
                }
            }


            if (mouseX in closePosition.x..(closePosition.x + closeSize.x) && mouseY in closePosition.y..(closePosition.y + closeSize.y)
            ) {
                println("NEIN!!")

            }



            }

    }


        override fun resize(width: Int, height: Int) {
        // Update the viewport on resize

        viewport.update(width, height, true)
    }



    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        batch.dispose()
        font.dispose()
        heftTexture.disposeSafely()
        nextTexture.disposeSafely()
        backTexture.disposeSafely()
        sortTexture.disposeSafely()
        shapeRenderer.dispose()
    }
}
