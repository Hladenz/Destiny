package xyz.crystallizedprison.destinyenchants.Objects;

import org.bukkit.entity.Player;

public class PlayerInfo {

    final Player player;
    double Tokens;

    public Player getPlayer() {
        return player;
    }

    public double getTokens() {
        return Tokens;
    }

    public void setTokens(Double tokens) {
        Tokens = tokens;
    }

    public PlayerInfo(Player player, double tokens) {
        this.player = player;
        Tokens = tokens;
    }
}
