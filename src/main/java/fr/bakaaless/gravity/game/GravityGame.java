package fr.bakaaless.gravity.game;

import fr.bakaaless.gravity.game.gamers.Gamer;
import fr.bakaaless.gravity.game.gamers.GravityPlayer;
import fr.bakaaless.gravity.game.storage.GravityConfig;
import fr.bakaaless.gravity.utils.DoubleResult;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class GravityGame {

    private static transient final List<GravityGame> games = new ArrayList<>();

    public static List<GravityGame> getGames() {
        return games;
    }

    public static Optional<DoubleResult<GravityGame, GravityPlayer>> fromPlayer(final Player player) {
        final Optional<GravityGame> game = games.stream().filter(games ->
                games.getPlayers().stream()
                        .anyMatch(gamer -> gamer.getUniqueId().equals(player.getUniqueId()))
        ).findFirst();
        if (!game.isPresent())
            return Optional.empty();
        final Optional<GravityPlayer> gamer = game.get().getPlayers().stream()
                .filter(gamers -> gamers.getUniqueId().equals(player.getUniqueId())).findFirst();
        if (!gamer.isPresent())
            return Optional.empty();
        final DoubleResult<GravityGame, GravityPlayer> result = new DoubleResult<>();
        result.setFirstValue(game.get());
        result.setSecondValue(gamer.get());
        return Optional.of(result);
    }

    private final UUID uniqueId;
    private String name;
    private final List<Map> mapSet;
    private final Set<GravityPlayer> players;
    private final GravityConfig config;

    private transient int step;
    private transient TimerTask task;
    private transient Timer timer;

    private transient List<Gamer> classement;
    private transient List<Gamer> finished;

    public GravityGame(final UUID uniqueId, final String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.mapSet = new ArrayList<>();
        this.players = new HashSet<>();
        this.config = new GravityConfig();
    }

    public void start() {
        if (this.timer != null && this.task != null)
            this.stop();
        this.mapSet.forEach(Map::loadSpawns);
        this.players.stream().filter(player -> player instanceof Gamer)
                .map(player -> (Gamer) player)
                .forEach(Gamer::init);
        this.step = 0;
        final GravityGame game = this;
        this.classement = new ArrayList<>();
        this.finished = new ArrayList<>();
        this.task = new TimerTask() {
            @Override
            public void run() {
                game.update();
            }
        };
        this.timer = new Timer();
        this.timer.schedule(this.task, 0, 50L);
    }

    public void update() {
        for (final GravityPlayer player : this.players) {
            int mapId = player.getMap();
            if (player.toPlayer().getLocation().getBlock().getType().equals(Material.NETHER_PORTAL))
                player.onSuccess();

        }
        if (this.step % 2 == 0)
            this.updateClassement();
        this.step++;
    }

    public void stop() {
        if (this.timer == null || this.task == null)
            return;
        this.step = -1;
        this.timer.cancel();
        this.task = null;
        this.timer = null;
    }

    public List<Gamer> updateClassement() {
        final List<Gamer> result = new ArrayList<>(this.finished);
        for (int index = this.mapSet.size() - 1; index >= 0 && result.size() <= 5; index--) {
            final int mapId = index;
            final List<Gamer> filtered = this.getGamers().stream()
                    .filter(player -> player.getMap() == mapId)
                    .sorted(Comparator.comparingInt(Gamer::getFails))
                    .collect(Collectors.toList());
            for (int i = 0; i < filtered.size() && result.size() <= 5; i++) {
                result.add(filtered.get(0));
            }
        }
        this.classement = result;
        return result;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map> getMapSet() {
        return this.mapSet;
    }

    public GravityConfig getConfig() {
        return this.config;
    }

    public Set<Gamer> getGamers() {
        return this.players.stream()
                .filter(player -> player instanceof Gamer)
                .map(player -> (Gamer) player)
                .collect(Collectors.toSet());
    }

    public Set<GravityPlayer> getPlayers() {
        return this.players;
    }
}
