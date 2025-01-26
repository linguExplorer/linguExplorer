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

class MapScreen(private val game: linguExplorer) : KtxScreen {

    private val stage :Stage = Stage(ExtendViewport(16f,9f))
    private val textureAtlas = TextureAtlas("assets/graphics/entities.atlas")
    private val playerTexture: Texture = Texture("assets/graphics/entities.png")
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

        currentMap = TmxMapLoader().load("assets/graphics/map/main-map.tmx")
        stage.fire(MapChangeEvent(currentMap!!))



        PlayerKeyboardInputProcessor(world, stage, world.mapper())

    }

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
