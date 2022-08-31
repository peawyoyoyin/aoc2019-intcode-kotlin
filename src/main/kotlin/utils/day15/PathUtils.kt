package utils.day15

import java.lang.Exception

typealias Path = List<Direction>

fun Path.toAbbr(): String {
    return this.map { it.abbr }.joinToString("")
}

fun pathFromString(pathString: String): Path {
    return pathString.map {
        when(it) {
            'N' -> Direction.NORTH
            'S' -> Direction.SOUTH
            'W' -> Direction.WEST
            'E' -> Direction.EAST
            else -> throw Exception("Unexpected $it in path string")
        }
    }
}

fun partition(toTry: Path, lastOK: Path): Triple<Path, Path, Path> {
    var pt1 = 0
    var pt2 = 0

    var commonPrefix = mutableListOf<Direction>()

    var toApply = mutableListOf<Direction>()
    var toReverse = mutableListOf<Direction>()

    while (true) {
        if (pt1 >= toTry.size || pt2 >= lastOK.size) {
            break
        }
        if (toTry[pt1] == lastOK[pt2]) {
            commonPrefix.add(toTry[pt1])
            pt1 += 1
            pt2 += 1
        }
        else {
            break
        }
    }

    if (pt1 < toTry.size) {
        toApply += toTry.drop(pt1)
    }

    if (pt2 < lastOK.size) {
        toReverse += lastOK.drop(pt2)
    }

    return Triple(commonPrefix.toList(), toReverse.toList(), toApply.toList())
}

// NNWWW,NNWWS NNWW, W, S