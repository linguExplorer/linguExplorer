package com.github.linguExplorer.system

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.linguExplorer.component.*
import com.github.linguExplorer.component.PhysicComponent.Companion.physicCmpFromShape2D
import com.github.linguExplorer.event.ActivateKeyEvent
import com.github.linguExplorer.event.MapChangeEvent
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.screen.LoadingScreen
import com.github.linguExplorer.screen.MinigameEssenScreen
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.assets.disposeSafely
import ktx.tiled.*
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

@AllOf([PathComponent::class])
class PathSystem(
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val phWorld: World,
    private val pathCmps: ComponentMapper<PathComponent>,
    private val phCmps: ComponentMapper<PhysicComponent>,
    private val gameStage: Stage,
    private val game: linguExplorer
) : IteratingSystem(), EventListener {

    private val pathAreas = mutableListOf<PathArea>()
    private val intersections = mutableSetOf<Pair<Float, Float>>()

    data class PathArea(val x: Float, val y: Float, val width: Float, val height: Float, val type: String)
    data class Node(val x: Float, val y: Float, val neighbors: MutableMap<Node, Float> = mutableMapOf())

    private val graph = mutableMapOf<Pair<Float, Float>, Node>()

    override fun onTickEntity(entity: Entity) {
        val (id, path, triggerEntities) = pathCmps[entity]
        if (triggerEntities.isNotEmpty()) {
            println("Collision with Path $path")
            triggerEntities.clear()
        }
    }

    public fun findIntersections(): Set<Pair<Float, Float>> {
        val result = mutableSetOf<Pair<Float, Float>>()

        for (hPath in pathAreas.filter { it.type == "hor" }) {
            for (vPath in pathAreas.filter { it.type == "vert" }) {
                val intersects = hPath.x < (vPath.x + vPath.width) &&
                    (hPath.x + hPath.width) > vPath.x &&
                    vPath.y < (hPath.y + hPath.height) &&
                    (vPath.y + vPath.height) > hPath.y

                if (intersects) {
                    val overlapX1 = maxOf(hPath.x, vPath.x)
                    val overlapX2 = minOf(hPath.x + hPath.width, vPath.x + vPath.width)
                    val overlapY1 = maxOf(hPath.y, vPath.y)
                    val overlapY2 = minOf(hPath.y + hPath.height, vPath.y + vPath.height)

                    val intersectX = (overlapX1 + overlapX2) / 2
                    val intersectY = (overlapY1 + overlapY2) / 2

                    result.add(Pair(intersectX, intersectY))
                }
            }
        }

        return result
    }

    private fun buildGraph() {
        // Knoten erstellen
        intersections.forEach { (x, y) ->
            graph[x to y] = Node(x, y)
        }

        // Kanten hinzufügen (horizontale/vertikale Verbindungen)
        pathAreas.forEach { area ->
            val areaNodes = graph.values.filter { node ->
                when (area.type) {
                    "hor" -> node.y == area.y && node.x in area.x..(area.x + area.width)
                    "vert" -> node.x == area.x && node.y in area.y..(area.y + area.height)
                    else -> false
                }
            }

            areaNodes.forEach { node ->
                areaNodes
                    .filter { it != node }
                    .forEach { neighbor ->
                        val distance = sqrt(
                            (node.x - neighbor.x).toDouble().pow(2) +
                                (node.y - neighbor.y).toDouble().pow(2)
                        ).toFloat()
                        node.neighbors[neighbor] = distance
                    }
            }
        }
    }

    private fun heuristic(a: Node, b: Node): Float {
        return sqrt((a.x - b.x).toDouble().pow(2) + (a.y - b.y).toDouble().pow(2)).toFloat()
    }

    private fun reconstructPath(cameFrom: Map<Node, Node>, current: Node): List<Pair<Float, Float>> {
        val path = mutableListOf(current.x to current.y)
        var node = current
        while (cameFrom.containsKey(node)) {
            node = cameFrom[node]!!
            path.add(0, node.x to node.y)
        }
        return path
    }

    fun findPath(start: Pair<Float, Float>, goal: Pair<Float, Float>): List<Pair<Float, Float>>? {
        val startNode = graph[start] ?: return null
        val goalNode = graph[goal] ?: return null

        val cameFrom = mutableMapOf<Node, Node>()
        val gCost = mutableMapOf<Node, Float>().withDefault { Float.MAX_VALUE }
        val fCost = mutableMapOf<Node, Float>().withDefault { Float.MAX_VALUE }
        val openSet = PriorityQueue<Node>(compareBy { fCost.getValue(it) })

        gCost[startNode] = 0f
        fCost[startNode] = heuristic(startNode, goalNode)
        openSet.add(startNode)

        while (openSet.isNotEmpty()) {
            val current = openSet.poll()
            if (current == goalNode) return reconstructPath(cameFrom, current)

            current.neighbors.forEach { (neighbor, distance) ->
                val tentativeGCost = gCost.getValue(current) + distance
                if (tentativeGCost < gCost.getValue(neighbor)) {
                    cameFrom[neighbor] = current
                    gCost[neighbor] = tentativeGCost
                    fCost[neighbor] = tentativeGCost + heuristic(neighbor, goalNode)
                    openSet.remove(neighbor) // WICHTIG: Aktualisiere die PriorityQueue
                    openSet.add(neighbor)
                }
            }
        }

        return null // No path found
    }

    override fun handle(event: Event): Boolean {
        if (event is MapChangeEvent) {
            val tlayer = event.map.layer("path")
            tlayer.objects.forEach { mapObj ->
                val path = mapObj.property("path", "")
                if (path.isBlank()) return@forEach

                pathAreas.add(PathArea(mapObj.x, mapObj.y, mapObj.width, mapObj.height, path))

                world.entity {
                    add<PathComponent> {
                        this.id = mapObj.id
                        this.path = path
                    }
                    physicCmpFromShape2D(phWorld, 0, 0, mapObj.shape, true)
                }
            }

            intersections.addAll(findIntersections())
            buildGraph() // WICHTIG: Graph muss nach dem Laden der Pfadbereiche gebaut werden

            println("Gefundene Kreuzungen: $intersections")
            return true
        }
        return false
    }
}
