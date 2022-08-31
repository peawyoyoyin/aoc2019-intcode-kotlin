package utils.day15

enum class Direction(val raw: Int, val abbr: Char) {
    NORTH(1, 'N'),
    SOUTH(2, 'S'),
    WEST(3, 'W'),
    EAST(4, 'E');

    fun reverse(): Direction {
        return when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            WEST -> EAST
            EAST -> WEST
        }
    }
}

fun Pair<Int, Int>.move(direction: Direction): Pair<Int, Int> {
    val (x, y) = this

    return when (direction) {
        Direction.NORTH -> Pair(x, y+1)
        Direction.SOUTH -> Pair(x, y-1)
        Direction.EAST -> Pair(x+1, y)
        Direction.WEST -> Pair(x-1, y)
    }
}