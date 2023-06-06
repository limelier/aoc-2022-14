data class Point (
    val x: Int, // right from origin
    val y: Int, // down from origin
) {
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
}