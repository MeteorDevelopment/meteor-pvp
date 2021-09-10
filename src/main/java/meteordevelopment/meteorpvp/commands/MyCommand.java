package meteordevelopment.meteorpvp.commands;

import meteordevelopment.meteorpvp.MeteorPvp;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public abstract class MyCommand extends Command {

    public MyCommand(String name, String description) {
        super(name, description, null, Collections.emptyList());
    }

    public MyCommand(String name, String description, String usage) {
        super(name, description, usage, Collections.emptyList());
    }

    public MyCommand(String name, String description, Permission permission) {
        super(name, description, null, Collections.emptyList());

        if (permission != null) setPermission(permission.getName());
    }

    public MyCommand(String name, String description, String usage, Permission permission) {
        super(name, description, usage, Collections.emptyList());

        if (permission != null) setPermission(permission.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        boolean success;

        if (!MeteorPvp.INSTANCE.isEnabled()) {
            throw new CommandException("Cannot execute command '" + commandLabel + "' in plugin " + MeteorPvp.INSTANCE.getDescription().getFullName() + " - plugin is disabled.");
        }

        if (!testPermission(sender)) {
            return true;
        }

        try {
            success = onCommand(sender, commandLabel, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + MeteorPvp.INSTANCE.getDescription().getFullName(), ex);
        }

        if (!success && usageMessage.length() > 0) {
            for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }

        return success;
    }

    protected abstract boolean onCommand(CommandSender sender, String label, String[] args);

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args, Location location) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        List<String> completions;
        try {
            completions = onTabComplete(sender, alias, args);
        } catch (Throwable ex) {
            StringBuilder message = new StringBuilder();
            message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
            for (String arg : args) {
                message.append(arg).append(' ');
            }
            message.deleteCharAt(message.length() - 1).append("' in plugin ").append(MeteorPvp.INSTANCE.getDescription().getFullName());
            throw new CommandException(message.toString(), ex);
        }

        return completions;
    }

    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (!sender.getServer().suggestPlayerNamesWhenNullTabCompletions()) return com.google.common.collect.ImmutableList.of();
        return tabComplete(sender, alias, args);
    }
}
