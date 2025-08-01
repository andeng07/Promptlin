group = "me.centauri07.promptlin.promptlin-jda"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":promptlin-discord"))
    compileOnly("net.dv8tion:JDA:5.6.1")
}