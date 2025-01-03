package com.github.linguExplorer.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.math.vec2

const val DEFAULT_SPEED = 3f

data class SpawnCfg(
    val model: AnimationModel,
    val speedScaling: Float=1f,
    val physicScaling: Vector2 = vec2(1f,1f),
    val physicOffset:Vector2 = vec2(0f, 0f),
    val bodyType: BodyDef.BodyType = BodyDef.BodyType.DynamicBody,
    var aniType: AnimationType = AnimationType.IDLE

)

data class SpawnComponent (

    var type: String = "",
    var location: Vector2 = vec2(),

)
