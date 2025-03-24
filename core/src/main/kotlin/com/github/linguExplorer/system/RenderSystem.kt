package com.github.linguExplorer.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.component.ImageComponent
import com.github.linguExplorer.event.*
import com.github.linguExplorer.linguExplorer.Companion.UNIT_SCALE
import com.github.linguExplorer.screen.GameUnlockScreenRenderer
import com.github.linguExplorer.screen.LockScreenRenderer
import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.collection.compareEntity
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.tiled.forEachLayer


@AllOf([ImageComponent::class])
class RenderSystem(
    private val stage: Stage,
    private val imageCmps:ComponentMapper<ImageComponent>
) : EventListener, IteratingSystem(
    comparator = compareEntity{e1,e2 -> imageCmps[e1].compareTo(imageCmps[e2])}
) {

    private val bgdLayers = mutableListOf<TiledMapTileLayer>()
    private val fgdLayers = mutableListOf<TiledMapTileLayer>()
    private val mapRenderer = OrthogonalTiledMapRenderer(null, UNIT_SCALE, stage.batch)
    private val orthoCam = stage.camera as OrthographicCamera
    private val shapeRenderer = ShapeRenderer()
    private val fadingCircles = mutableListOf<FadingCircle>()
    private var showLock = false
    private var showUnlocked = false
    private var lockTimer = 0f

    data class FadingCircle(
        val x: Float,
        val y: Float,
        var alpha: Float = 0.5f
    )
    private val uiStage: Stage = Stage(ExtendViewport(16f, 9f)) // Eine Stage speziell für die UI

    private val gviewport: Viewport = ExtendViewport(1920f, 1080f)


    override fun onTick() {
        super.onTick()
        with(stage) {
            viewport.apply()
            gviewport.update(Gdx.graphics.width, Gdx.graphics.height, true)

            AnimatedTiledMapTile.updateAnimationBaseTime()
            mapRenderer.setView(orthoCam)

            if (bgdLayers.isNotEmpty()) {
                stage.batch.use (orthoCam.combined){
                    bgdLayers.forEach { mapRenderer.renderTileLayer(it)}
                }
            }
            act(deltaTime)
            draw()
            if(fgdLayers.isNotEmpty()){
                stage.batch.use (orthoCam.combined) {
                    fgdLayers.forEach {mapRenderer.renderTileLayer(it)}
                }
            }
            //uiStage.act(Math.min(60f, 1 / 30f)) // Update für die UI-Stage
            //uiStage.draw()

            // Zeichne Kreise hier
            val iterator = fadingCircles.iterator()
            while (iterator.hasNext()) {
                val circle = iterator.next()
                circle.alpha -= 0.01f // Alpha-Wert reduzieren

                if (circle.alpha <= 0f) {
                    iterator.remove() // Kreis entfernen, wenn er verblasst ist
                } else {
                    // Kreis mit aktuellem Alpha-Wert zeichnen

                    Gdx.gl.glEnable(GL20.GL_BLEND)
                    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                    shapeRenderer.projectionMatrix = orthoCam.combined
                    shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
                        shapeRenderer.color = Color(1f, 1f, 1f, circle.alpha) // Weiß mit Alpha
                        shapeRenderer.circle(circle.x, circle.y, 0.25f, 20)
                    }
                }
            }


            /////Funkion hier zum Lockscreen erstellen Welche Minigame es ist wird über Parameter angegeben



            ////


            ///Funktion für Celebrate oder Unlocked



            ////

            if(showLock) {

                lockTimer += deltaTime
                if(lockTimer >= 3f) {
                    showLock = false
                    lockTimer = 0f
                }
                LockScreenRenderer().render(SpriteBatch(), gviewport, deltaTime)
            }

            if(showUnlocked) {

                lockTimer += deltaTime
                if(lockTimer >= 3f) {
                    showUnlocked = false
                    lockTimer = 0f
                }
                GameUnlockScreenRenderer().renderTopicUpdate(SpriteBatch(), gviewport, deltaTime, 1f, true)
            }

        }
    }
    override fun onTickEntity(entity: Entity) {
        imageCmps[entity].image.toFront()
    }

    override fun handle(event: Event): Boolean {
       when (event) {
           is MapChangeEvent -> {
               bgdLayers.clear()
               fgdLayers.clear()
                event.map.forEachLayer<TiledMapTileLayer> { layer ->
                    if(layer.name.startsWith("fgd")) {
                        fgdLayers.add(layer)
                    }else {
                        bgdLayers.add(layer)
                    }
                }
                return true

           }


           is ClickDownEvent -> {
               fadingCircles.add(FadingCircle(event.mouseX, event.mouseY))
               return true


           }

           //Event für Lockscreen catchen, welches Minigam es ist wird über Event übergeben
           // Beispiel: (event.mouseX, event.mouseY)


           is LockScreenEvent -> {

               showLock = true
               lockTimer = 0f
               println(event.miniGame)


           }


           //////////////



           //Event für Celebrate oder unlocked

           is UnlockedEvent -> {

               showUnlocked = true
               lockTimer = 0f

           }

           /////////////
           is GameChangeEvent -> {

               println("Game change Event")



           }
       }
        return false
    }

    fun resize(width: Int, height: Int) {
        gviewport.update(width, height, true)
    }
    override fun onDispose() {
        mapRenderer.disposeSafely()

    }
}
