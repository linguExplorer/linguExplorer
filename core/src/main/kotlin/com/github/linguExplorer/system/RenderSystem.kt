package com.github.linguExplorer.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.ImageComponent
import com.github.linguExplorer.event.ClickDownEvent
import com.github.linguExplorer.event.GameChangeEvent
import com.github.linguExplorer.event.MapChangeEvent
import com.github.linguExplorer.linguExplorer.Companion.UNIT_SCALE
import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.collection.compareEntity
import ktx.actors.alpha
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


    data class FadingCircle(
        val x: Float,
        val y: Float,
        var alpha: Float = 0.5f
    )

    override fun onTick() {
        super.onTick()
        with(stage) {
            viewport.apply()

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


           is GameChangeEvent -> {

               println("Game change Event")



           }
       }
        return false
    }

    override fun onDispose() {
        mapRenderer.disposeSafely()
    }
}
