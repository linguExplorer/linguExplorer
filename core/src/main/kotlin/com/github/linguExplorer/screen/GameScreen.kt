package com.github.linguExplorer.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.linguExplorer.component.*
import com.github.linguExplorer.event.CollisionDespawnEvent
import com.github.linguExplorer.event.MapChangeEvent
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.input.PlayerKeyboardInputProcessor
import com.github.linguExplorer.system.*
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.graphics.use
import ktx.log.logger
import ktx.math.vec2

class GameScreen : KtxScreen {

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
        }

        components {
            add<ImageComponent.Companion.ImageComponentListener>()
            add<PhysicComponent.Companion.PhysicComponentListener>()
        }

        systems {
            add<EntitySpawnSystem>()
            add<CollisionSpawnSystem>()
            add<CollisionDespawnSystem>()
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
        currentMap = TmxMapLoader().load("assets/graphics/map/map1.tmx")
        stage.fire(MapChangeEvent(currentMap!!))

        PlayerKeyboardInputProcessor(world, world.mapper())

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
    companion object {
        private  val log = logger<GameScreen>()
    }
}
