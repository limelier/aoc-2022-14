import kotlin.math.max
import kotlin.math.min

class Rectangle (
    val topLeft: Point,
    val bottomRight: Point,
) {
    val cols = bottomRight.x - topLeft.x + 1
    val rows = bottomRight.y - topLeft.y + 1
    val size = Point(cols, rows)

    operator fun contains(point: Point): Boolean {
        return point.x >= topLeft.x && point.y >= topLeft.y
                && point.x <= bottomRight.x && point.y <= bottomRight.y
    }

    fun expandedToContain(point: Point) = if (contains(point)) {
        this
    } else {
        Rectangle(
            Point(min(topLeft.x, point.x), min(topLeft.y, point.y)),
            Point(max(bottomRight.x, point.x), max(bottomRight.y, point.y))
        )
    }

    fun relative(point: Point): Point = point - topLeft
    fun global(point: Point): Point = topLeft + point
}