package minegame159.thebestplugin.commands;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.world.block.BlockTypes;
import minegame159.thebestplugin.TheBestPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearCpvpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TaskManager.IMP.async(() -> {
            try (EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld("world")).fastmode(true).build()) {
                long start = System.currentTimeMillis();
                editSession.replaceBlocks(TheBestPlugin.CPVP_REGION, new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK), BlockTypes.AIR);
                long delta = System.currentTimeMillis() - start;
                sender.sendMessage(String.format("Area cleared in %.2fs.", delta / 1000.0));
            }
        });
        return true;
    }
}
