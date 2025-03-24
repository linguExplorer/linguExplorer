package com.github.linguExplorer.component

import com.github.quillraven.fleks.Entity

data class MGComponent (
    var id:Int = -1,
    var toGame:String = "",
    var topicId:Int = 0,
   var triggerEntities : MutableSet<Entity> = mutableSetOf<Entity>()

)
