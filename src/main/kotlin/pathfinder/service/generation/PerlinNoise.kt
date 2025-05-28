package pathfinder.service.generation

import kotlin.random.Random

// Simple Perlin Noise implementation
class PerlinNoise(seed: Long) {
    private val random = Random(seed)
    private val permutation = List(256) { it }.shuffled(random).let { it + it }

    fun noise(x: Double, y: Double): Double {
        val xi = x.toInt() and 255
        val yi = y.toInt() and 255
        val g1 = permutation[permutation[xi] + yi]
        val g2 = permutation[permutation[xi + 1] + yi]
        val g3 = permutation[permutation[xi] + yi + 1]
        val g4 = permutation[permutation[xi + 1] + yi + 1]

        val xf = x - x.toInt()
        val yf = y - y.toInt()

        val d1 = grad(g1, xf, yf)
        val d2 = grad(g2, xf - 1, yf)
        val d3 = grad(g3, xf, yf - 1)
        val d4 = grad(g4, xf - 1, yf - 1)

        val u = fade(xf)
        val v = fade(yf)

        return lerp(
            lerp(d1, d2, u),
            lerp(d3, d4, u),
            v
        )
    }

    private fun fade(t: Double): Double = t * t * t * (t * (t * 6 - 15) + 10)

    private fun lerp(a: Double, b: Double, t: Double): Double = a + t * (b - a)

    private fun grad(hash: Int, x: Double, y: Double): Double {
        val h = hash and 15
        val u = if (h < 8) x else y
        val v = if (h < 4) y else x
        return ((if (h and 1 == 0) u else -u) +
                (if (h and 2 == 0) v else -v))
    }
}
