package problems.day19

import processing.core.PApplet

class Day19Visualizer(private val logic: Day19): PApplet() {
    companion object {
        fun run(logic: Day19) {
            val art = Day19Visualizer(logic)
            art.setSize(2000, 2000)
            art.runSketch()
        }
    }

    override fun setup() {
        frameRate(60f)
    }

    private fun drawMap() {
        background(0)
        noStroke()
        fill(255)
        for ((y, beamBoundary) in logic.beamBoundaries.withIndex()) {
            val (minX, maxX) = beamBoundary
            println("drawing $y $minX-$maxX")
            rect(minX.toFloat(), y.toFloat(), (maxX-minX).toFloat(), 1f)
        }
    }

    override fun draw() {
        logic.advance()
        drawMap()
    }
}

fun main() {
    val logic = Day19()
    Day19Visualizer.run(logic)
}
