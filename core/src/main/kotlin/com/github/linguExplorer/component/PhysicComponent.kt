package com.github.linguExplorer.component

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.EntityCreateCfg
import com.badlogic.gdx.physics.box2d.World
import com.github.linguExplorer.linguExplorer.Companion.UNIT_SCALE
import com.github.linguExplorer.system.CollisionSpawnSystem.Companion.SPAWN_AREA_SIZE
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import ktx.app.gdxError
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.loop
import ktx.math.vec2
import java.awt.Polygon

class PhysicComponent {
    val prevPos = vec2()
    val impulse = vec2()
    lateinit var body: Body


    companion object {

        fun EntityCreateCfg.physicCmpFromShape2D(
            world: World,
            x: Int,
            y: Int,
            shape: Shape2D,
            isWindow: Boolean = false,
        ): PhysicComponent {
            when (shape) {
                is Rectangle -> {
                    val bodyX = x + shape.x * UNIT_SCALE
                    val bodyY = y + shape.y * UNIT_SCALE
                    val bodyW = shape.width * UNIT_SCALE
                    val bodyH = shape.height * UNIT_SCALE
                    return add {
                        body = world.body(BodyType.StaticBody) {
                            position.set(bodyX, bodyY)
                            fixedRotation = true
                            allowSleep = false
                            loop(
                                vec2(0f,0f),
                                vec2(bodyW, 0f),
                                vec2(bodyW, bodyH),
                                vec2(0f, bodyH)
                            )  {
                                this.isSensor = isWindow
                            }

                            if(!isWindow) {
                                circle(SPAWN_AREA_SIZE+4f) {
                                    isSensor = true
                                }
                            }

                        }
                    }

                }

                else -> gdxError("No shape")
            }
        }
        fun EntityCreateCfg.physicCmpFromImage(
            world: World,
            image: Image,
            bodyType: BodyType,
            fixtureAction: BodyDefinition.(PhysicComponent, Float, Float ) -> Unit
        ) : PhysicComponent {
            val x = image.x
            val y = image.y
            val w = image.width
            val h = image.height

            return add {
                body = world.body(bodyType) {
                    position.set(x+w*0.5f, y+h*0.5f)
                    fixedRotation = true
                    allowSleep = false
                    this.fixtureAction(this@add, w, h)
                }
            }

        }



        class PhysicComponentListener : ComponentListener<PhysicComponent> {
            override fun onComponentAdded(entity: Entity, component: PhysicComponent) {
                component.body.userData = entity
            }

            override fun onComponentRemoved(entity: Entity, component: PhysicComponent) {
                val body = component.body
                component.body.world.destroyBody(body)
                body.userData = null
            }
        }
    }
}
