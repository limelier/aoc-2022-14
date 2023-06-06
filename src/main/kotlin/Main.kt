import draw.StdoutAsciiDrawer
import java.io.File

fun main() {
    val lines = lines()
    val bounds = boundingBox(lines)
    val cave = when(DAY_PART){
        DayPart.ONE -> Cave(bounds)
        DayPart.TWO -> {
            val groundY = (bounds.bottomRight - SAND_ORIGIN).y + 2
            val groundLine = Line(
                Point(SAND_ORIGIN.x - groundY, groundY),
                Point(SAND_ORIGIN.x + groundY, groundY)
            )
            Cave(bounds.expandedToContain(groundLine.start).expandedToContain(groundLine.end)).apply {
                draw(groundLine)
            }
        }
    }
    for (line in lines) {
        cave.draw(line)
    }

    val drawer = StdoutAsciiDrawer(cave)
    val simulator = Simulator(cave)
    while (!simulator.done) {
        for (fallingSandPosition in simulator.simulate()) {
            if (DRAW_ANIMATION) {
                drawer.draw(fallingSandPosition)
                Thread.sleep(FRAME_TIME_MILLIS)
            }
        }
    }
    drawer.draw()
    println(simulator.settledSand)
}

/** Iterate through all the lines of rock drawn by the input. */
fun lines() = sequence {
    File(INPUT_FILE_PATH).useLines {lines ->
        for (line in lines) {
            val points = line.split(" -> ")
                .map { pair ->
                    val coords = pair.split(",").map(String::toInt)
                    Point(coords[0], coords[1])
                }
            for (i in 0 until points.size - 1) {
                yield(Line(points[i], points[i + 1]))
            }
        }
    }
}

fun boundingBox(lines: Sequence<Line>): Rectangle {
    var box = Rectangle(SAND_ORIGIN, SAND_ORIGIN)
    for (line in lines) {
        for (point in line.ends()) {
            // possible optimisation: many points gone through twice; iterate through input lines instead of drawn lines
            box = box.expandedToContain(point)
        }
    }
    return box
}