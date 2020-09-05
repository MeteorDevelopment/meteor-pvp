package minegame159.thebestplugin.utils;

import org.bukkit.ChatColor;

public class Msgs {
    // General
    public static String playerNotOnline() { return "Player is not online."; }
    public static String cantUseThisInPvp() { return "You can't use this command while in pvp."; }

    // Duels
    public static String dontHaveRequest() { return "You don't have duel request from that player."; }
    public static String cancelledRequest() { return "Cancelled duel request."; }
    public static String didntSendRequest() { return "You didn't send any duel request."; }
    public static String cantDuelYourself() { return "You can't duel yourself."; }
    public static String playerIsInDuel(String otherPlayer) { return "Player is in duel with " + otherPlayer + "."; }
    public static String duelRequestSent() { return "Duel request sent."; }
    public static String alreadySentRequest() { return "You have already sent a duel request."; }
    public static String cancelDuelHelp() { return "Do /cancelduel to cancel it."; }
    public static String duelRequest(String player) { return ChatColor.YELLOW + "" + ChatColor.BOLD + player + ChatColor.GRAY + " wants to duel you."; }
    public static String playerDeclinedDuel(String player) { return ChatColor.GRAY + player + ChatColor.WHITE + " declined your duel."; }
    public static String youDeclinedDuel(String player) { return "Declined duel from " + ChatColor.GRAY + player + ChatColor.WHITE + "."; }
    public static String yourDuelExpired() { return "Your duel request expired."; }
    public static String duelExpired(String player) { return "Duel request from " + ChatColor.GRAY + player + ChatColor.WHITE + " expired."; }

    // KITS
    public static String createdKit(String name) { return "Created kit with name " + ChatColor.GRAY + "'" + name + "'" + ChatColor.WHITE + "."; }
    public static String maxKits() { return "You can only have 3 kits."; }
    public static String kitAlreadyExists(String name) { return "Kit with name " + ChatColor.GRAY + "'" + name + "' " + ChatColor.WHITE + "already exists."; }
    public static String deletedKit(String name) { return "Kit with name " + ChatColor.GRAY + "'" + name + "' " + ChatColor.WHITE + "has been deleted."; }
    public static String doesntOwnKit() { return "You don't own that kit."; }
    public static String kitDoesntExist(String name) { return "Kit with name " + ChatColor.GRAY + "'" + name + "' " + ChatColor.WHITE + "doesn't exist."; }
    public static String kitCreatorToggled(boolean on) { return "Kit creator is now " + ChatColor.GRAY + (on ? "on" : "off") + ChatColor.WHITE + "."; }
}
