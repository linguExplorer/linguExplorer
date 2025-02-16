package com.github.linguExplorer.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.World
import com.github.linguExplorer.component.*
import com.github.quillraven.fleks.*
import kotlinx.coroutines.flow.combineTransform
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

@AllOf([PhysicComponent::class, ImageComponent::class])
class PhysicSystem (
    private val phWorld: World,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,
    private val tiledCmps: ComponentMapper<TiledComponent>,
    private val collisionCmps: ComponentMapper<CollisionComponent>,
    private val mgCmps: ComponentMapper<MGComponent>,
    private val playerCmps : ComponentMapper<PlayerComponent>,

    ) : ContactListener,  IteratingSystem(
    interval = Fixed(1/60f)) {


    init {
        phWorld.setContactListener(this)
    }
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



    private val Fixture.entity:Entity
        get() = this.body.userData as Entity

    override fun beginContact(contact: Contact) {
        val entityA :Entity= contact.fixtureA.entity
        val entityB :Entity= contact.fixtureB.entity
        val isEntityATiledCollisionSensor = entityA in tiledCmps && contact.fixtureA.isSensor
        val isEntityBTiledCollisionFixture =  entityB in collisionCmps  && !contact.fixtureB.isSensor
        val isEntityBTiledCollisionSensor = entityB in tiledCmps && contact.fixtureB.isSensor
        val isEntityATiledCollisionFixture =  entityA in collisionCmps  && !contact.fixtureA.isSensor


        when {
            isEntityATiledCollisionSensor && isEntityBTiledCollisionFixture -> {
                tiledCmps[entityA].nearbyEntities += entityB
            }
            isEntityBTiledCollisionSensor && isEntityATiledCollisionFixture -> {
                tiledCmps[entityB].nearbyEntities += entityA
            }

            //Go to Game

            entityA in mgCmps && entityB in playerCmps && contact.fixtureB.isSensor -> {
                mgCmps[entityA].triggerEntities += entityB
            }

            entityB in mgCmps && entityA in playerCmps && contact.fixtureB.isSensor -> {
                mgCmps[entityB].triggerEntities += entityA
            }
        }

    }

    override fun endContact(contact: Contact) {

        val entityA :Entity= contact.fixtureA.entity
        val entityB :Entity= contact.fixtureB.entity
        val isEntityATiledCollisionSensor = entityA in tiledCmps && contact.fixtureA.isSensor
        val isEntityBTiledCollisionSensor = entityB in tiledCmps && contact.fixtureB.isSensor



        when {
            isEntityATiledCollisionSensor && !contact.fixtureB.isSensor -> {
                tiledCmps[entityA].nearbyEntities -= entityB
            }
            isEntityBTiledCollisionSensor && !contact.fixtureA.isSensor -> {
                tiledCmps[entityB].nearbyEntities -= entityA
            }

        }
    }

    private fun Fixture.isStaticBody() = this.body.type == BodyDef.BodyType.StaticBody
    private fun Fixture.isDynamicBody() = this.body.type == BodyDef.BodyType.DynamicBody

    override fun preSolve(contact: Contact, p1: Manifold) {
        contact.isEnabled =
            (contact.fixtureA.isStaticBody()  && contact.fixtureB.isDynamicBody())
        || (contact.fixtureB.isStaticBody() && contact.fixtureA.isDynamicBody())
    }

    override fun postSolve(p0: Contact?, p1: ContactImpulse?) {
    }
    companion object {
        private val log = logger<PhysicSystem>()
    }


}
