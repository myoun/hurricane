package app.myoun.hurricane.sample

import app.myoun.hurricane.bootstrap.DefaultHurricaneGenerator
import app.myoun.hurricane.bootstrap.makeServer
import app.myoun.hurricane.command.CommandGenerator
import app.myoun.hurricane.command.addSyntax
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.adventure.audience.Audiences
import net.minestom.server.command.ConsoleSender
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerSkinInitEvent

fun main() {
    makeServer {

        command {
            + CommandGenerator("broadcast") {
                val messageArgument = ArgumentType.String("message")

                addSyntax(messageArgument) { sender, context ->
                    val message: String = context[messageArgument]
                    Audiences.all().sendMessage(
                        Component.text("[${if (sender is ConsoleSender) "Console" else (if (sender is Player) sender.username else "Unknown") }] $message")
                    )
                }
            }
        }

        val spawningInstance = MinecraftServer.getInstanceManager().createInstanceContainer()
        spawningInstance.setGenerator(DefaultHurricaneGenerator())

        globalEventHandler.addListener(PlayerLoginEvent::class.java) {
            val player = it.player
            it.setSpawningInstance(spawningInstance)
            player.respawnPoint = Pos(0.0,41.0,0.0)
        }

        globalEventHandler.addListener(PlayerSkinInitEvent::class.java) {
            it.skin = PlayerSkin.fromUsername(it.player.username)
        }

    }.run()
}