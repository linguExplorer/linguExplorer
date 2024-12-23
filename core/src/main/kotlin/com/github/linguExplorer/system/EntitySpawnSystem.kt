package com.github.linguExplorer.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.linguExplorer.component.*
import com.github.linguExplorer.component.PhysicComponent.Companion.physicCmpFromImage
import com.github.linguExplorer.event.MapChangeEvent
import com.github.linguExplorer.linguExplorer.Companion.UNIT_SCALE
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y

@AllOf([SpawnComponent::class])
class EntitySpawnSystem (
    private val phWorld : World,
    private val atlas: TextureAtlas,
    private val SpawnCmps:ComponentMapper<SpawnComponent>

): EventListener, IteratingSystem() {

    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(SpawnCmps[entity]) {
            val cfg = spawnCfg(type)
            val relativSize = size(cfg.model)
            world.entity {

                val imageCmp = add<ImageComponent> {

                    image = Image().apply {
                        setPosition(location.x, location.y)
                        setSize(relativSize.x, relativSize.y)
                        setScaling(Scaling.fill)
                    }
                }
                add<AnimationComponent> {
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }

                physicCmpFromImage(phWorld, imageCmp.image, BodyDef.BodyType.DynamicBody) {
                    phCmp, width, height ->
                    box (width, height) {
                        isSensor = false
                        friction
                    }
                }
                if (cfg.speedScaling > 0f ) {
                    add<MoveComponent> {
                        speed = DEFAULT_SPEED * cfg.speedScaling
                    }
                }


                if(type == "Player") {
                    add<PlayerComponent>()
                }
            }
        }
        world.remove(entity)
    }

    private fun size(model: AnimationModel) = cachedSizes.getOrPut(model) {
        val regions = atlas.findRegions("${model.atlasKey}/${AnimationType.IDLE.atlasKey}")
        if(regions.isEmpty) {
            gdxError("No Regions for $model")
        }
        val firstFrame = regions.first()
        vec2(firstFrame.originalWidth* (UNIT_SCALE/4), firstFrame.originalHeight* (UNIT_SCALE/4))
    }

    private fun spawnCfg(type:String):SpawnCfg = cachedCfgs.getOrPut(type) {
        when  (type){
            "Player" -> SpawnCfg(AnimationModel.PLAYER)
            else -> gdxError("Type $type no Spawn config")
        }
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                val entityLayer = event.map.layer("entities")
                entityLayer.objects.forEach {
                    mapObj ->
                    val type = mapObj.type ?: gdxError("MapObject $mapObj no type")
                    world.entity {
                        add<SpawnComponent> {
                            this.type = type
                            this.location.set(mapObj.x*UNIT_SCALE, mapObj.y* UNIT_SCALE)
                        }
                    }
                }
                return true
            }
        }
        return false
    }
}
