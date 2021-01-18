package fr.bakaaless.gravity.game.gamers;

import fr.bakaaless.gravity.game.GravityGame;
import fr.bakaaless.gravity.plugin.GravityPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class GravityPlayer {

    private final UUID uniqueId;
    private final GravityGame game;
    private int map;

    public GravityPlayer(final UUID uniqueId, final GravityGame game) {
        this.uniqueId = uniqueId;
        this.game = game;
        this.map = 0;
    }

    public void join() {
        this.toPlayer().setAllowFlight(false);
    }

    public void onSuccess() {
        final List<Location> futureSpawns = this.getGame().getMapSet().get(++this.map).getSpawns();
        this.toPlayer().teleport(
                        futureSpawns.get(new Random().nextInt(futureSpawns.size()))
        );
    }

    public void remove() {
        this.game.getPlayers().remove(this);
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public GravityGame getGame() {
        return this.game;
    }

    public int getMap() {
        return this.map;
    }

    public void setMap(final int map) {
        this.map = map;
    }

    public Player toPlayer() {
        return GravityPlayer.toPlayer(this);
    }

    public static Player toPlayer(GravityPlayer player) {
        return GravityPlugin.get().getServer().getPlayer(player.getUniqueId());
    }
}
