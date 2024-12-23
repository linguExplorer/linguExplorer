package com.github.linguExplorer.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.github.linguExplorer.component.ImageComponent
import com.github.linguExplorer.component.PhysicComponent
import com.github.quillraven.fleks.*
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

@AllOf([PhysicComponent::class, ImageComponent::class])
class PhysicSystem (
    private val phWorld: World,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,

) :  IteratingSystem(
    interval = Fixed(1/60f)) {

    override fun onUpdate() {
        if (phWorld.autoClearForces) {
            log.error { "AutoClear Forces must be set to false" }
            phWorld.autoClearForces = false
        }
        super.onUpdate()
        phWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        phWorld.step(deltaTime,6,2)
    }

    override fun onTickEntity(entity: Entity) {
        val physicCmp = physicCmps[entity]
        val imageCmps = imageCmps[entity]

        physicCmp.prevPos.set(physicCmp.body.position)

        if(!physicCmp.impulse.isZero) {
            physicCmp.body.applyLinearImpulse(physicCmp.impulse, physicCmp.body.worldCenter, true)
            physicCmp.impulse.setZero()
        }


    }


    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val physicCmp = physicCmps[entity]
        val imageCmps = imageCmps[entity]

        val(prevX, prevY) = physicCmp.prevPos
        val (bodyX, bodyY) = physicCmp.body.position
        imageCmps.image.run {
            setPosition(
                MathUtils.lerp(prevX,bodyX, alpha)-width * 0.5f,
                MathUtils.lerp(prevY, bodyY, alpha)-height * 0.5f)

        }
    }


    companion object {
        private val log = logger<PhysicSystem>()
    }

}
