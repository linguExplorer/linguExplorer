package com.github.linguExplorer.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.TiledComponent
import com.github.linguExplorer.event.CollisionDespawnEvent
import com.github.linguExplorer.event.fire
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem


@AllOf([TiledComponent::class])
class CollisionDespawnSystem (
private val tiledCmps:ComponentMapper<TiledComponent>,
    private val stage: Stage
) :IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        with(tiledCmps[entity]) {
            if(nearbyEntities.isEmpty()) {
                stage.fire(CollisionDespawnEvent(cell))
                world.remove(entity)
            }
        }


    }
}
