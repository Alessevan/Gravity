package fr.bakaaless.gravity.game;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Map {

    private final UUID uniqueId;
    private String name;
    private int difficulty;
    private int floor;
    private final List<Location> spawns;
    private Location pointA;
    private Location pointB;

    public Map(final UUID uniqueId, final String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.difficulty = 0;
        this.floor = 0;
        this.spawns = new ArrayList<>();
    }

    public void loadSpawns() {
        this.spawns.forEach(location -> location.getChunk().load());
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(final int difficulty) {
        this.difficulty = difficulty;
    }

    public int getFloor() {
        return this.floor;
    }

    public void setFloor(final int floor) {
        this.floor = floor;
    }

    public List<Location> getSpawns() {
        return spawns;
    }

    public Location getPointA() {
        return pointA;
    }

    public void setPointA(Location pointA) {
        this.pointA = pointA;
    }

    public Location getPointB() {
        return pointB;
    }

    public void setPointB(Location pointB) {
        this.pointB = pointB;
    }
}
