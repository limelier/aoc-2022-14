/** Represent the 2D space of the cave using tiles. */
class Cave (
    val bounds: Rectangle
) {
    val backingArray = Array(bounds.rows) { Array<Tile?>(bounds.cols) { null } }

    fun draw(line: Line) {
        for (point in line.allPoints()) {
            fill(point, Tile.ROCK)
        }
    }

    fun fill(point: Point, tile: Tile = Tile.SAND) { this[point] = tile }
    fun isFilled(point: Point) = if (point in bounds) {
        this[point] != null
    } else {
        false
    }
    internal operator fun get(point: Point): Tile? {
        val offset = offset(point)
        return backingArray[offset.y][offset.x]
    }

    private operator fun set(point: Point, value: Tile) {
        val offset = offset(point)
        backingArray[offset.y][offset.x] = value
    }

    private fun offset(point: Point): Point = point - bounds.topLeft
}