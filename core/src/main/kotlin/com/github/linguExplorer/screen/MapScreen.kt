package com.github.linguExplorer.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.linguExplorer.component.*
import com.github.linguExplorer.event.MapChangeEvent
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.input.PlayerKeyboardInputProcessor
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.system.*
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2
import com.badlogic.gdx.scenes.scene2d.ui.Image


class MapScreen(private val game: linguExplorer) : KtxScreen {

    private val stage :Stage = Stage(ExtendViewport(16f,9f))
    private val textureAtlas = TextureAtlas("com/github/linguExplorer/assets/graphics/entities.atlas")
    private val playerTexture: Texture = Texture("com/github/linguExplorer/assets/graphics/entities.png")
    private var currentMap: TiledMap? = null;
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    private val world: World= world {

        injectables {
            add(stage)
            add(textureAtlas)
            add(phWorld)
            add(game)
        }

        components {
            add<ImageComponent.Companion.ImageComponentListener>()
            add<PhysicComponent.Companion.PhysicComponentListener>()
        }

        systems {
            add<EntitySpawnSystem>()
            add<CollisionSpawnSystem>()
            add<CollisionDespawnSystem>()
            add<MapChangeSystem>()
            add<MoveSystem>()
            add<PhysicSystem>()
            add<AnimationSystem>()
            add<CameraSystem>()
            add<RenderSystem>()
            add<DebugSystem>()
        }
    }

    override fun show() {
        log.debug { "Game Screen gets shown" }
        world.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }

        // Karte laden
        currentMap = TmxMapLoader().load("com/github/linguExplorer/assets/graphics/map/main-map.tmx")
        stage.fire(MapChangeEvent(currentMap!!))

        // fixe Bilder hinzufügen
        //addUIImages()

        // Spieler-Eingabeverarbeitung
        PlayerKeyboardInputProcessor(world, stage, world.mapper())

    }

    //fixe Bilder Methode
    /*private fun addUIImages() {
        // Bilder laden
        val backpackTexture = Texture("assets/Symbolgrafiken/Rucksack/v2/Backpack2.png")
        val mapTexture = Texture("assets/Symbolgrafiken/Map/v2/Map2.png")
        val phrasingBookTexture = Texture("assets/Symbolgrafiken/Phrasenheft/Phrasenheft2-1.png.png")
        val progressBarTexture = Texture("assets/Symbolgrafiken/Prozentleiste/v2/Prozentleiste2-1.png.png")
        val moneyBagTexture = Texture("assets/Symbolgrafiken/Coinbag/v2/MoneyBag2-2.png.png")

        // Images für jedes Bild
        val backpackImage = com.badlogic.gdx.scenes.scene2d.ui.Image(backpackTexture)
        val mapImage = com.badlogic.gdx.scenes.scene2d.ui.Image(mapTexture)
        val phrasingBookImage = com.badlogic.gdx.scenes.scene2d.ui.Image(phrasingBookTexture)
        val progressBarImage = com.badlogic.gdx.scenes.scene2d.ui.Image(progressBarTexture)
        val moneyBagImage = com.badlogic.gdx.scenes.scene2d.ui.Image(moneyBagTexture)

        // Position
        backpackImage.setPosition(0f, 0f)  // Links unten
        mapImage.setPosition(100f, 0f)     // Ein bisschen nach rechts verschoben (z.B. für das Map-Bild)
        phrasingBookImage.setPosition(200f, 0f) // Noch ein weiteres nach rechts verschieben
        progressBarImage.setPosition(300f, 0f) // Beispiel
        moneyBagImage.setPosition(400f, 0f)   // Noch weiter nach rechts verschoben

        // Stage
        stage.addActor(backpackImage)
        stage.addActor(mapImage)
        stage.addActor(phrasingBookImage)
        stage.addActor(progressBarImage)
        stage.addActor(moneyBagImage)
    }*/

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {

       world.update(delta.coerceAtMost(0.25f))
    }

    override fun dispose() {
        stage.disposeSafely()
        playerTexture.disposeSafely()
        textureAtlas.disposeSafely()
        world.dispose()
        currentMap?.disposeSafely()
        phWorld.disposeSafely()
    }
    companion object : KtxScreen {
        private  val log = logger<MapScreen>()
    }
}
