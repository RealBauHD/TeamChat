rootProject.name = "TeamChat"

sequenceOf(
    "common",
    "bungee",
    "velocity",
    "bukkit",
    "paper"
).forEach {
    val project = ":teamchat-$it"
    include(project)
    project(project).projectDir = file(it)
}
