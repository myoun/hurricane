package app.myoun.hurricane.command

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.CommandSyntax
import net.minestom.server.command.builder.arguments.Argument
import org.jetbrains.annotations.ApiStatus

fun Command.addSyntax(vararg argument: Argument<*>, executor: CommandExecutor): MutableCollection<CommandSyntax> {
    return addSyntax(executor, *argument)
}

@Suppress("UnstableApiUsage")
@ApiStatus.Experimental
fun Command.addSyntax(format: String, executor: CommandExecutor): MutableCollection<CommandSyntax> {
    return addSyntax(executor, format)
}

fun CommandGenerator(name: String, vararg alias: String, context: Command.() -> Unit): Command {
    return Command(name, *alias).apply(context)
}