package thirdlab.generator

import kotlin.random.Random


class RandomFactory {
    companion object {
        private val tickCountOnStart: Long by lazy {
            System.currentTimeMillis() / 1_000
        }

        val create: () -> Random = { Random(tickCountOnStart) }
    }
}