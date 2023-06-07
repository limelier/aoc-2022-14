
private fun Point.below() = Point(x, y + 1)
private fun Point.belowLeft() = Point(x - 1, y + 1)
private fun Point.belowRight() = Point(x + 1, y + 1)
class Simulator(private val cave: Cave) {
    var done = false
    var settledSand = 0

    /**
     * Simulate a falling sand particle, yielding all of its positions while falling.
     *
     * If the sand could not spawn because the `SAND_ORIGIN` is blocked, set `done` to true and return right away.
     * If the sand fell out of the cave (for part 1), set `done` to true and return.
     * If the sand settled, add 1 to `settledSand`.
     */
    fun stepPoints() = sequence {
        var sand = SAND_ORIGIN
        if (cave.isFilled(sand)) {
            done = true
            return@sequence
        }

        var below: Point
        var belowLeft: Point
        var belowRight: Point
        while(true) {
            yield(sand)

            below = sand.below()
            belowLeft = sand.belowLeft()
            belowRight = sand.belowRight()

            val nextPosition = if (!cave.isFilled(below)) {
                below
            } else if (!cave.isFilled(belowLeft)) {
                belowLeft
            } else if (!cave.isFilled(belowRight)) {
                belowRight
            } else {
                break
            }

            if (nextPosition !in cave.bounds) {
                done = true
                return@sequence
            }
            sand = nextPosition
        }

        cave.fill(sand)
        settledSand += 1
    }

    /** Drop one grain of sand. Same as `stepPoints`, but without returning the falling sand positions. */
    fun step() {
        stepPoints().count() // consume the entire sequence without doing anything
    }
}