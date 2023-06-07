package draw

import Cave
import FRAME_TIME_MILLIS
import Point
import SAND_ORIGIN
import org.openrndr.PresentationMode
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.Linearity
import org.openrndr.draw.loadFont
import org.openrndr.extra.noise.Random
import kotlin.concurrent.thread
import org.openrndr.draw.Drawer as ORDrawer

private const val TILE_PIXEL_SIZE = 6
private val COLOR_BG = ColorRGBa.fromHex("#262222")
private val COLOR_ROCK = ColorRGBa.fromHex("#735f5f")
private val COLOR_SAND = ColorRGBa.PINK
private val COLOR_ORIGIN = COLOR_BG.shade(0.5)

private fun Point.sandColor(): ColorRGBa {
    return COLOR_SAND
        .mix(
            ColorRGBa(
            Random.perlin(x.toDouble(), y.toDouble()) * 0.1 + 1.0,
            Random.simplex(x.toDouble(), y.toDouble()) * 0.5 + 1.0,
            Random.value(x.toDouble(), y.toDouble()) * 0.5 + 1.0,
            1.0,
            Linearity.SRGB
        ), 0.1)
        .shade(Random.value(x.toDouble(), y.toDouble()) * 0.1 + 1.0)
}
class GraphicalDrawer(cave: Cave) : Drawer(cave) {
    private val windowSize = cave.bounds.size * TILE_PIXEL_SIZE

    private var fallingSand: Point? = null

    private var redraw: (() -> Unit)? = null

    private var finished = false
    private var totalSand = 0

    init {
        thread {
            application {
                configure {
                    width = windowSize.x
                    height = windowSize.y
                }

                program {
                    window.presentationMode = PresentationMode.MANUAL
                    redraw = window::requestDraw

                    val font = loadFont("data/fonts/default.otf", 64.0)
                    drawer.fontMap = font

                    extend {
                        drawer.stroke = null
                        drawer.drawCave()
                        fallingSand?.let { drawer.drawSingle(it) }

                        if (finished) {
                            drawer.text(
                                "${if (cave.bounds.cols > 100) "Total sand: " else ""}$totalSand",
                                TILE_PIXEL_SIZE.toDouble(),
                                TILE_PIXEL_SIZE.toDouble() * 3
                            )
                        }
                    }
                }
            }
        }
    }

    override fun draw(fallingSand: Point?) {
        this.fallingSand = fallingSand
        redraw?.let { it() }
        Thread.sleep(FRAME_TIME_MILLIS)
    }

    override fun finish(totalSand: Int) {
        this.totalSand = totalSand
        finished = true
        redraw?.let { it() }
    }

    private fun ORDrawer.drawCave() {
        clear(COLOR_BG)

        rectangles {
            stroke = null
            for (y in 0 until cave.bounds.rows) {
                for (x in 0 until cave.bounds.cols) {
                    val globalPoint = cave.bounds.global(Point(x, y))
                    val tile = cave[globalPoint]


                    fill = when (tile) {
                        Tile.ROCK -> COLOR_ROCK
                        Tile.SAND -> globalPoint.sandColor()
                        null -> if (globalPoint == SAND_ORIGIN) {
                            COLOR_ORIGIN
                        } else {
                            continue
                        }
                    }

                    if (fill != null) {
                        rectangle(
                            (x * TILE_PIXEL_SIZE).toDouble(),
                            (y * TILE_PIXEL_SIZE).toDouble(),
                            TILE_PIXEL_SIZE.toDouble(),
                            TILE_PIXEL_SIZE.toDouble()
                        )
                    }
                }
            }
        }
    }
    private fun ORDrawer.drawSingle(sand: Point) {
        sand
            .let { cave.bounds.relative(it) }
            .run {
                fill = COLOR_SAND
                rectangle(
                    (x * TILE_PIXEL_SIZE).toDouble(),
                    (y * TILE_PIXEL_SIZE).toDouble(),
                    TILE_PIXEL_SIZE.toDouble(),
                    TILE_PIXEL_SIZE.toDouble()
                )
            }
    }
}