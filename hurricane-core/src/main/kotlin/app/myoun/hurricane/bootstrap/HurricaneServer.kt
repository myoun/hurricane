package app.myoun.hurricane.bootstrap

import app.myoun.hurricane.annotation.HurricaneMarker
import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator
import net.minestom.server.network.socket.Server

fun makeServer(setup: HurricaneServerSetupContext.() -> Unit): HurricaneServer.ServerRunner {
    return HurricaneServerSetupContext().apply(setup).hurricaneServer.runner
}

class HurricaneServer {

    private val mcServer = MinecraftServer.init()
    var port: Int = 25565
    var address: String = "0.0.0.0"

    var generator: Generator = DefaultHurricaneGenerator()

    val runner = ServerRunner(this)

    val commandList = CommandList()

    inner class ServerRunner private constructor(){

        internal constructor(server: HurricaneServer) : this()

        fun run() {


            mcServer.start(address, port)
        }
    }

}

class DefaultHurricaneGenerator : Generator {

    override fun generate(unit: GenerationUnit) {
        with(unit.modifier()) {
            fillHeight(0,1, Block.BEDROCK)
            fillHeight(1,36, Block.STONE)
            fillHeight(36,39, Block.DIRT)
            fillHeight(39,40, Block.GRASS_BLOCK)
        }
    }

}

class CommandList {

    @Suppress("Unused")
    val allCommands: Set<Command> by MinecraftServer.getCommandManager()::commands

    fun add(command: Command) {
        MinecraftServer.getCommandManager().register(command)
    }

    fun remove(command: Command) {
        MinecraftServer.getCommandManager().unregister(command)
    }

    fun findByName(name: String): Command? {
        return allCommands.find { name in it.names }
    }

    fun find(predicate: (Command) -> Boolean): Command? {
        return allCommands.find(predicate)
    }

    override fun toString(): String {
        return allCommands.toString()
    }

    override fun hashCode(): Int {
        return allCommands.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandList

        if (allCommands != other.allCommands) return false

        return true
    }

}

@HurricaneMarker
class HurricaneServerSetupContext {

    internal val hurricaneServer: HurricaneServer = HurricaneServer()

    var port: Int by hurricaneServer::port
    var address: String by hurricaneServer::address

    val globalEventHandler: GlobalEventHandler
        get() = MinecraftServer.getGlobalEventHandler()

    val instanceManager: InstanceManager
        get() = MinecraftServer.getInstanceManager()

    fun command(setup: HurricaneCommandSetupContext.() -> Unit) {
        HurricaneCommandSetupContext(hurricaneServer, this).apply(setup)
    }

}

@HurricaneMarker
class HurricaneCommandSetupContext private constructor(private val server: HurricaneServer) {

    internal constructor(server: HurricaneServer, context: HurricaneServerSetupContext) : this(server)

    operator fun Command.unaryPlus() {
        server.commandList.add(this)
    }
}