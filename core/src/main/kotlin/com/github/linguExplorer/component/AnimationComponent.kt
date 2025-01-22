package com.github.linguExplorer.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable


enum class AnimationModel{
    PLAYER, UNDEFINED;

    val atlasKey: String = this.toString().lowercase()

}
enum class AnimationType {
    IDLE, BACK, LEFT, RIGHT;

    val atlasKey: String = this.toString().lowercase()
}

data class AnimationComponent(
    var model: AnimationModel = AnimationModel.UNDEFINED,
    var stateTime:Float=0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
){
    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation: String = ""

    fun nextAnimation(model: AnimationModel , type: AnimationType) {
        this.model = model
        nextAnimation = "${model.atlasKey}/${type.atlasKey}"
    }

    companion object {
        val NO_ANIMATION = ""
    }
}
