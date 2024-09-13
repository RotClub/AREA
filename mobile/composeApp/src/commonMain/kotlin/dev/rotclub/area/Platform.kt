package dev.rotclub.area

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform