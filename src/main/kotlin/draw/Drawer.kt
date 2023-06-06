package draw

import Cave
import Point

/** Allow the graphical representation of the falling sand in the cave. */
abstract class Drawer(protected val cave: Cave) {
    abstract fun draw(fallingSand: Point? = null)
}