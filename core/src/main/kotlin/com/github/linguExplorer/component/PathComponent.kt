package com.github.linguExplorer.component

import com.github.quillraven.fleks.Entity
data class PathComponent (
    var id:Int = -1,
    var path:String = "",
    var triggerEntities : MutableSet<Entity> = mutableSetOf<Entity>()
)



