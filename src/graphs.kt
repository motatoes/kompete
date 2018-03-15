
import java.util.*

class Queue <T>(list:MutableList<T>) {

    var items:MutableList<T> = list

    fun isEmpty():Boolean = this.items.isEmpty()

    fun count():Int = this.items.count()

    override fun toString() = this.items.toString()

    fun enqueue(element: T) {
        this.items.add(element)
    }

    fun dequeue():T {
        if (this.isEmpty()) {
            throw RuntimeException("Queue is empty")
        } else {
            return this.items.removeAt(0)
        }
    }

    fun peek():T {
        return this.items[0]
    }

}

data class ShortestPathTree(val distances:IntArray, val previous:Array<Int>)

data class Edge(var toNode:Int, var weight:Int=1)

data class Graph(var adjList:List<List<Edge>>) {

    // just traverses recursively start node and marks all connected nodes as visited
    fun explore(startNode:Int, visited:Array<Boolean> = Array<Boolean>(adjList.size, {false})): Array<Boolean> {

        visited[startNode] = true

        var nei = adjList[startNode]

        for (n in nei) {
            if (!visited[n.toNode]) {
                explore(n.toNode, visited)
            }
        }
        return visited
    }

    // returns the number of connected components for the graph
    fun num_of_connected_components():Int {
        var res = 0
        var visited = Array<Boolean>(adjList.size, {false})
        for (i in 0 until adjList.size) {
            if (!visited[i]) {
                visited = explore(i, visited)
                res = res + 1
            }
        }

        return res
    }

    fun reachable(fromNode:Int, toNode:Int):Boolean {

        var visited:Array<Boolean> = Array<Boolean>(adjList.size, {false})

        fun _reachable(fromNode:Int, toNode:Int):Boolean {
            var nei = adjList[fromNode]
            for (n in nei) {

                if (n.toNode == toNode) {
                    return true
                }

                if (!visited[n.toNode]) {
                    if (_reachable(n.toNode, toNode)) {
                        return true
                    }
                }
            }

            return false
        }

        return _reachable(fromNode, toNode)
    }

    // depth first search
    fun dfs(toFind:Int) {

    }

    fun bfs_path(start:Int, end:Int, spt:ShortestPathTree):List<Int> {

        var shortest_path_tree = spt

        if (spt == null) {
            var shortest_path_tree = spt(start)
        }

        var c = end
        var path = mutableListOf<Int>(c)
        while (true) {
            c =  shortest_path_tree.previous[c as Int]
            path.add(c as Int)
            if (c == start) {
                break
            }
        }

        return path.reversed()
    }

    fun bfs(start:Int, end:Int, spt:ShortestPathTree? = null):Int {

        var shortest_path_tree = spt

        if (spt == null) {
            var shortest_path_tree = spt(start)
        }

        return shortest_path_tree!!.distances[end]
    }

    // computes the shortest path tree from a start node
    // until all the other connected nodes in the graph
    fun spt(start:Int):ShortestPathTree {

        var distances = IntArray(adjList.size)
        var prev = Array<Int?>(adjList.size, {x->x})

        val inf = adjList.size + 1000

        for ( i in 0 until distances.size) {
            distances[i] = inf
            prev[i] = null
        }

        distances[start] = 0
        var queue = Queue<Edge>(mutableListOf())

        // weight bellow does not matter as its just a starting point
        // and neighbouring weights are considered
        queue.enqueue(Edge(start,1))
        while (!queue.isEmpty()) {
            var cur = queue.dequeue()
            var nei = adjList[cur.toNode]

            for (n in nei) {
                if (distances[n.toNode] == inf) {
                    queue.enqueue(n)
                    distances[n.toNode] = distances[cur.toNode] + n.weight
                    prev[n.toNode] = cur.toNode
                }
            }
        }

        return ShortestPathTree(distances, prev as Array<Int>)

    }

    // for directed graphs: check whether the graph contains cycles
    // true if no cycles are present, false otehrwise
    fun acyclic():Boolean {
//        var visited = Array<Boolean>(adjList.size, {false})

        fun nodeHasCycle(startNode:Int, endNode:Int):Boolean {

            var nei = adjList[startNode]
            for (n in nei) {
                if (n.toNode == endNode || nodeHasCycle(n.toNode, endNode)) {
                    return true
                }
            }

//            visited[startNode] = true
            return false
        }

        for (i in 0 until adjList.size) {
            if (nodeHasCycle(i, i)) {
                return false
            }
        }

        return true
    }

    // TODO: dijkstra algorithm implementation
    fun dijikstra() {

    }

    // TODO: bellman-ford algorithm implementation
    fun bellmanford() {

    }

    // TODO: minimum spanning tree implementation
    fun mst() {

    }

    override fun toString():String {
        var s = String.format("Graph contains ${adjList.size} nodes:\n")
        for (i in 0 until adjList.size) {
            s += "Node ${i}: "
            for (n in adjList[i])
                s += "$n "
            s += "\n"
        }
        return s
    }

}


// Sample input
// (first line number of nodes and number o edges
// .. and then e lines for each edeg)
// [what about weights ???]
5 4
1 2
3 2
4 3
1 4

fun main(ars: Array<String>) {

    var s = Scanner(System.`in`)

    var (v, e) = s.nextLine().split(" ").map{x->x.toInt()}

    var nodes = mutableListOf<MutableList<Edge>>()
    for (i in 0 until v) {
        nodes.add(mutableListOf<Edge>())
    }

    for (i in 0 until e) {
        var t = s.nextLine()
        var (a,b) = t.split(" ").map{x->x.toInt()}
        nodes[a-1].add(Edge(b-1, 2))
        nodes[b-1].add(Edge(a-1, 2))
    }

    var g = Graph(nodes)

    println("the graph size is ${nodes.size}")
    println(g)
    var spt =  g.spt(0)
    println("The bfs results: ${g.bfs(0,2, spt)}")
    println("The bfs path: ${g.bfs_path(0,2, spt)}")

    println("is node 1 reachable from node 2?: ${g.reachable(0,1)}")
    println("is node 5 reachable from node 2?: ${g.reachable(4,1)}")

    println("The number of connected components in this graph are: ${g.num_of_connected_components()}")

    println("Does this graph contain cycles: ${!g.acyclic()}")
}