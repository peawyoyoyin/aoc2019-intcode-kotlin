package problems.day15

import processing.core.PApplet

class Day15Visualizer(private val logic: Day15) : PApplet() {
    companion object {
        fun run(logic: Day15) {
            val art = Day15Visualizer(logic)
            art.setSize(1000, 1000)
            art.runSketch()
        }
    }

    override fun setup() {
        frameRate(60f)
    }

    private fun drawMap() {
        background(0)

        noFill()
        noStroke()

        for (y in 0 until 200) {
            for (x in 0 until 200) {
                when(logic.map[y][x]) {
                    UNEXPLORED -> noFill()
                    OPEN_TILE -> fill(120)
                    WALL -> fill(0f, 0f, 120f)
                    ROBOT -> fill(255f, 0f, 0f)
                    GOAL -> fill(0f, 255f, 0f)

                    else -> {
                        noFill()
                    }
                }

                if (Pair(x, y) == origin) {
                    fill(255f, 255f, 0f)
                }

                if (Pair(x,y) == logic.robotPosition) {
                    fill(255f, 0f, 0f)
                }

                rect(x*10f, y*10f, 10f, 10f)
            }
        }
    }

    override fun draw() {
        logic.advance()
        drawMap()
    }
}

fun main() {
    val logic = Day15()
    Day15Visualizer.run(logic)
}
