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
import com.github.linguExplorer.userId
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import com.badlogic.gdx.Application
import com.github.linguExplorer.saveNumber
import java.util.*
import java.util.concurrent.Executors
import javax.swing.SwingUtilities

class PhrasenheftScreen (
    private val game: linguExplorer
): KtxScreen {
    private var font = BitmapFont()
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)

    private enum class SortState {
        ASCENDING_PHRASE, DESCENDING_PHRASE, ASCENDING_TRANSLATION, DESCENDING_TRANSLATION
    }
    private var currentSortState = SortState.ASCENDING_PHRASE
    private var sortText = "Phrase aufsteigend"

    private val phrasesOfProgress = PhraseProgressRepository().getAllPhrasesOfUserProgress(userId, saveNumber)
    private var phrases = phrasesOfProgress.map {
        it.phrase to it.translation
    }

    private val lineHeight = 56f
    private val spacing = 260f
    private var currentY: Float = 0f

    // Texturen
    private val heftTexture = Texture(Gdx.files.internal("Phrasenheft/heft_design.png"))
    private val sortTexture = Texture(Gdx.files.internal("Phrasenheft/Sort.png"))
    private val nextTexture = Texture(Gdx.files.internal("Phrasenheft/weiter.png"))
    private val backTexture = Texture(Gdx.files.internal("Phrasenheft/zurueck.png"))
    private val closeTexture = Texture(Gdx.files.internal("Phrasenheft/red_X.png"))

    private val heftSize = Vector2(1080f, 784f)
    private val nextSize = Vector2(backTexture.width.toFloat()*8, backTexture.height.toFloat()*8)
    private val backSize = Vector2(backTexture.width.toFloat()*8, backTexture.height.toFloat()*8)
    private val sortSize = Vector2(sortTexture.width.toFloat()*6, sortTexture.height.toFloat()*6)
    private val closeSize = Vector2(closeTexture.width.toFloat()*1.25f, closeTexture.height.toFloat()*1.25f)

    private var maxPages= (phrases.size/10f)//wie viele Phrasen max
    private var currentPage = 1

    // Flag für den PDF-Export-Zustand
    private var isExportingPDF = false
    // Flag, um zu überwachen, ob der Export abgeschlossen ist
    private var exportCompleted = false
    // Der Ausführungsdienst für Thread-Management
    private val executor = Executors.newSingleThreadExecutor()

    private val nextPosition: Vector2
        get() = Vector2(
            viewport.worldWidth/2 + 600f,
            (viewport.worldHeight - heftSize.y) - 200f
        )

    private val backPosition: Vector2
        get() = Vector2(
            viewport.worldWidth/2 - 690f,
            (viewport.worldHeight  - heftSize.y) - 200f
        )

    private val sortPosition: Vector2
        get() = Vector2(
            viewport.worldWidth/2 - 690f,
            (viewport.worldHeight/2f + heftSize.y/2f) - sortSize.y
        )

    private val closePosition: Vector2
        get() = Vector2(
            viewport.worldWidth - 150f,
            (viewport.worldHeight - 50f) - sortSize.y
        )

    private val textX = sortPosition.x
    private val textY = sortPosition.y

    override fun show() {
        Gdx.input.inputProcessor = null
    }

    override fun render(delta: Float) {
        // Prüfen, ob der Export abgeschlossen ist
        if (exportCompleted) {
            game.setScreen<MapScreen>()
            return
        }

        handleInput()
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        font = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
        val layout = GlyphLayout()
        font.color = Color.BLACK
        font.data.setScale(0.3f, 0.3f)

        val screenHeight = viewport.worldHeight
        val screenWidth = viewport.worldWidth

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(156 / 255f, 194 / 255f, 211 / 255f, 1f)
        shapeRenderer.rect(0f, 0f, screenWidth, screenHeight)
        shapeRenderer.end()

        batch.end()

        batch.begin()

        val heftX = (screenWidth - heftSize.x) / 2f
        val heftY = (screenHeight - heftSize.y) / 2f
        batch.draw(heftTexture, heftX, heftY, heftSize.x, heftSize.y)

        if ((currentPage - 1) > 0) {
            batch.draw(backTexture, backPosition.x, backPosition.y, backSize.x, backSize.y)
        }
        if ((currentPage - 1) + 1 <= maxPages - 1f) {
            batch.draw(nextTexture, nextPosition.x, nextPosition.y, backSize.x, backSize.y)
        }
        batch.draw(sortTexture, sortPosition.x, sortPosition.y, sortSize.x, sortSize.y)
        batch.draw(closeTexture, closePosition.x, closePosition.y, closeSize.x, closeSize.y)

        currentY = heftY + heftSize.y - 140f
        val startX = heftX + 130f
        var adjustedY = currentY

        layout.setText(font, "English")
        var titleTextWidth = layout.width
        var titleX = startX - (titleTextWidth / 2)
        font.draw(batch, "English", titleX, adjustedY + 75f)
        titleX += 558f
        font.draw(batch, "English", titleX, adjustedY + 75f)
        layout.setText(font, "Deutsch")
        titleTextWidth = layout.width
        titleX = startX + spacing - (titleTextWidth / 2)
        font.draw(batch, "Deutsch", titleX, adjustedY + 75f)
        titleX += 558f
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
                    558f
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

        font.data.setScale(0.15f, 0.15f)
        layout.setText(font, sideText)
        font.draw(batch, sideText, sortPosition.x, sortPosition.y + sortSize.y + 30f )

        batch.end()
    }

    private fun handleInput() {
        if (isExportingPDF) {
            // When exporting, do not process input
            return
        }

        // Use viewport's unproject method to get correct world coordinates
        val mouseX = viewport.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())).x
        val mouseY = viewport.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())).y

        if (Gdx.input.justTouched()) {
            // Next button
            if (mouseX in nextPosition.x..(nextPosition.x + backSize.x) &&
                mouseY in nextPosition.y..(nextPosition.y + backSize.y)) {
                if((currentPage-1) + 1 <  maxPages-1f) {
                    currentPage++
                }
                println("Button Next, $currentPage")
            }

            // Back button
            if (mouseX in backPosition.x..(backPosition.x + backSize.x) &&
                mouseY in backPosition.y..(backPosition.y + backSize.y)) {
                if((currentPage-1) - 1 >=  0) {
                    currentPage--
                }
                println("Button Back, $currentPage")
            }

            // Sort button
            if (mouseX in sortPosition.x..(sortPosition.x + sortSize.x) &&
                mouseY in sortPosition.y..(sortPosition.y + sortSize.y)) {
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

            // Close button
            if (mouseX in closePosition.x..(closePosition.x + closeSize.x) &&
                mouseY in closePosition.y..(closePosition.y + closeSize.y)) {
                if (!isExportingPDF) {
                    isExportingPDF = true
                    startExportPDF()
                }
            }
        }
    }

    private fun startExportPDF() {
        // Führe den Export in einem separaten Thread aus, um Blockieren des UI-Threads zu vermeiden
        executor.submit {
            try {
                if (Gdx.app.type == Application.ApplicationType.Desktop) {
                    // Verwende SwingUtilities.invokeAndWait um sicherzustellen, dass der Dialog im richtigen Thread ausgeführt wird
                    val filePathRef = arrayOfNulls<String>(1)
                    val wasCancelledRef = booleanArrayOf(false)

                    // Ausführen des JFileChooser im Swing-Thread
                    SwingUtilities.invokeAndWait {
                        try {
                            val chooser = javax.swing.JFileChooser()
                            chooser.dialogTitle = "PDF speichern unter"

                            // Standarddateiname vorschlagen
                            val defaultFile = File("Phrasenheft.pdf")
                            chooser.selectedFile = defaultFile

                            // Nur PDF-Dateien anzeigen/akzeptieren
                            val fileFilter = javax.swing.filechooser.FileNameExtensionFilter("PDF Dateien (*.pdf)", "pdf")
                            chooser.fileFilter = fileFilter

                            // Das Dialogfenster anzeigen
                            val returnVal = chooser.showSaveDialog(null)

                            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                                // Benutzer hat einen Speicherort ausgewählt
                                val selectedFile = chooser.selectedFile
                                var filePath = selectedFile.absolutePath

                                // Sicherstellen, dass die Datei die .pdf-Erweiterung hat
                                if (!filePath.lowercase(Locale.getDefault()).endsWith(".pdf")) {
                                    filePath += ".pdf"
                                }

                                filePathRef[0] = filePath
                            } else {
                                // Benutzer hat abgebrochen
                                wasCancelledRef[0] = true
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            wasCancelledRef[0] = true
                        }
                    }

                    // Verarbeiten des Ergebnisses
                    if (!wasCancelledRef[0] && filePathRef[0] != null) {
                        val filePath = filePathRef[0]!!
                        exportPhrasesToPDF(phrases, filePath)

                        // Erfolgsmeldung im Konsolenlog
                        println("PDF erfolgreich gespeichert unter: $filePath")

                        // Zurück zum LibGDX-Thread und zum MapScreen wechseln
                        Gdx.app.postRunnable {
                            exportCompleted = true
                        }
                    } else {
                        // Wenn abgebrochen, zurück zum normalen Zustand
                        Gdx.app.postRunnable {
                            isExportingPDF = false
                        }
                    }
                } else {
                    // Für Android und andere Plattformen
                    // Einfach direkt im externen Speicher speichern
                    val filePath = Gdx.files.external("Phrasenheft.pdf").file().absolutePath
                    exportPhrasesToPDF(phrases, filePath)

                    println("PDF gespeichert unter: $filePath")

                    // Zurück zum LibGDX-Thread und zum MapScreen wechseln
                    Gdx.app.postRunnable {
                        exportCompleted = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Bei Fehler zurück zum normalen Zustand
                Gdx.app.postRunnable {
                    isExportingPDF = false
                }
            }
        }
    }

    fun exportPhrasesToPDF(phrases: List<Pair<String, String>>, filePath: String) {
        val file = File(filePath)
        val pdfWriter = PdfWriter(file)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        // Titel
        document.add(Paragraph("linguExplorer Phrasenheft").setBold().setFontSize(18f))

        // Tabelle mit zwei Spalten (Phrase & Übersetzung)
        val table = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f))).useAllAvailableWidth()

        // Tabellenkopf
        table.addHeaderCell(Cell().add(Paragraph("Phrase")).setBold())
        table.addHeaderCell(Cell().add(Paragraph("Übersetzung")).setBold())

        // Phrasen & Übersetzungen einfügen
        for ((phrase, translation) in phrases) {
            table.addCell(Cell().add(Paragraph(phrase)))
            table.addCell(Cell().add(Paragraph(translation)))
        }

        document.add(table)
        document.close()

        println("PDF erfolgreich gespeichert: $filePath")
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
        // Beenden des Executors
        executor.shutdown()
    }
}
