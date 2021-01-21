package fr.bakaaless.gravity.game;

import fr.bakaaless.gravity.utils.DoubleResult;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Map {

    private final UUID uniqueId;
    private String name;
    private int difficulty;
    private int floor;
    private final List<Location> spawns;
    private Location hole;
    private transient HashMap<Location, DoubleResult<Material, BlockData>> tempBlocks;

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

    public void unlock() {
        if (this.tempBlocks != null && this.tempBlocks.size() > 0)
            this.lock();
        this.tempBlocks = new HashMap<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    final Block previousBlock = this.hole.clone().add(x, y, z).getBlock();
                    this.tempBlocks.put(previousBlock.getLocation(),
                            DoubleResult.from(previousBlock.getType(), previousBlock.getBlockData())
                    );
                    previousBlock.setType(Material.AIR);
                }
            }
        }
    }

    public void lock() {
        for (final java.util.Map.Entry<Location, DoubleResult<Material, BlockData>> tempBlocks : this.tempBlocks.entrySet()) {
            final Block block = tempBlocks.getKey().getBlock();
            block.setType(tempBlocks.getValue().getFirstValue());
            block.setBlockData(tempBlocks.getValue().getSecondValue());
        }
        this.tempBlocks.clear();
    }

    public String toColor() {
        String result;
        switch(this.difficulty) {
            case 0:
                result = "§a";
                break;
            case 1:
                result = "§2";
                break;
            case 2:
                result = "§6";
                break;
            case 3:
                result = "§c";
                break;
            case 4:
                result = "§4";
                break;
            default:
                result = "§0";
        }
        return result;
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

    public Location getHole() {
        return this.hole;
    }

    public void setHole(final Location hole) {
        this.hole = hole;
    }
}
