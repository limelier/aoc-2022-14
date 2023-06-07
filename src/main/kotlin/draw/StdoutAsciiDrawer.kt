package draw

import Cave
import FRAME_TIME_MILLIS
import Point
import SAND_ORIGIN

/**
 * Animate the falling sand as ASCII art in the standard output stream.
 *
 * Will print the frames one after the other, without clearing the terminal.
 */
class StdoutAsciiDrawer(cave: Cave) : Drawer(cave) {
    override fun draw(fallingSand: Point?) {
        with(cave.bounds) {
            for (y in topLeft.y..bottomRight.y) {
                for (x in topLeft.x..bottomRight.x) {
                    val point = Point(x, y)

                    print(when (point) {
                        fallingSand -> "x"
                        SAND_ORIGIN -> "+"
                        else -> when(cave[Point(x, y)]) {
                            Tile.ROCK -> "#"
                            Tile.SAND -> "o"
                            null -> "."
                        }
                    })
                }
                println()
            }
        }
        println()
        Thread.sleep(FRAME_TIME_MILLIS)
    }

    override fun finish(totalSand: Int) {
        draw()
        println("Total sand grains: $totalSand")
    }
}