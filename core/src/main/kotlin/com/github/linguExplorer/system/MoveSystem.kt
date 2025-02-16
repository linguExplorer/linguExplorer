package com.github.linguExplorer.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.*
import com.github.linguExplorer.event.ActivateKeyEvent
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.linguExplorer
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.math.component1
import ktx.math.component2
import java.util.*
import kotlinx.coroutines.*

@AllOf([MoveComponent::class, PhysicComponent::class])
class MoveSystem(
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    private val SpawnCmps:ComponentMapper<SpawnComponent>,
    private val gameStage: Stage,
    private val game: linguExplorer





    ) : IteratingSystem(){


    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()

    override fun onTickEntity(entity: Entity) {





        val moveCmp = moveCmps[entity]
        val physicCmp = physicCmps[entity]
        val mass = physicCmp.body.mass
        val (velX, velY) = physicCmp.body.linearVelocity




        if(moveCmp.cos == 0f && moveCmp.sin == 0f) {
            physicCmp.impulse.set(
                mass * (0f - velX),
                mass * (0f - velY)
            )
            return
        }
        physicCmp.impulse.set(
            mass * (moveCmp.speed* moveCmp.cos- velX),
            mass * (moveCmp.speed* moveCmp.sin - velY),

            )


            val cfg = cachedCfgs.getOrPut("Player") { SpawnCfg(AnimationModel.PLAYER) }

            val newAnimation = when {
                moveCmp.cos < 0f -> AnimationType.LEFT
                moveCmp.cos > 0f -> AnimationType.RIGHT
                moveCmp.sin > 0f -> AnimationType.BACK
                moveCmp.sin < 0f -> AnimationType.IDLE
                else -> AnimationType.IDLE
            }



        animationCmps.getOrNull(entity)?.let { aniCmp ->
            if (cfg.aniType != newAnimation) {

                cfg.aniType = newAnimation
                aniCmp.nextAnimation = "player/"+cfg.aniType.toString().lowercase()
            }
        }






    }
}
