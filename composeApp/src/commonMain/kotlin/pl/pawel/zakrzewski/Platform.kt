package pl.pawel.zakrzewski

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform