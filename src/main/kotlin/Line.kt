class Line (
    val start: Point,
    val end: Point,
) {
    val isVertical = start.x == end.x
    val isHorizontal = start.y == end.y
    fun ends() = iterator {
        yield(start)
        yield(end)
    }

    fun allPoints() = iterator {
        if (isHorizontal) {
            val xs = listOf(start.x, end.x).sorted()
            for (x in xs[0]..xs[1]) {
                yield(Point(x, start.y))
            }
        } else if (isVertical) {
            val ys = listOf(start.y, end.y).sorted()
            for (y in ys[0]..ys[1]) {
                yield(Point(start.x, y))
            }
        } else {
            throw IllegalStateException("line is neither vertical nor horizontal")
        }
    }
}