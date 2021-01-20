package fr.bakaaless.gravity.game;

import fr.bakaaless.gravity.game.gamers.*;
import fr.bakaaless.gravity.game.storage.GravityConfig;
import fr.bakaaless.gravity.utils.DoubleResult;
import fr.bakaaless.gravity.utils.Others;
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
    private final GravityConfig config;

    private transient Status status;
    private transient Set<GravityPlayer> players;
    private transient List<Map> loadedMaps;
    private transient int step;

    private transient List<Gamer> classement;
    private transient List<Gamer> finished;

    public GravityGame(final UUID uniqueId, final String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.mapSet = new ArrayList<>();
        this.config = new GravityConfig();
    }

    public void init() {
        this.status = Status.WAITING;
        this.players = new HashSet<>();
        this.classement = new ArrayList<>();
        this.finished = new ArrayList<>();
    }

    public void start() {
        this.status = Status.STARTING;
        this.loadedMaps = Others.copy(this.mapSet);
        while (this.loadedMaps.size() > this.getConfig().getMaps()) {
            this.loadedMaps.remove(new Random().nextInt(this.loadedMaps.size()));
        }
        this.loadedMaps.forEach(Map::loadSpawns);
        this.players.stream().filter(player -> player instanceof Gamer)
                .map(player -> (Gamer) player)
                .forEach(Gamer::init);
        this.step = 0;
    }

    public void update() {
        if (this.status == Status.WAITING) {
            final int needPlayers = this.getConfig().getPlayerMin() - this.players.size();
            if (needPlayers > 0)
                this.players.forEach(player -> player.toPlayer().sendActionBar("§eWaiting §l" + needPlayers + " players §eto launch the game..."));
            if (needPlayers >= 0) {
                final int timeUntilStart = this.getConfig().getWaitingTimer() - this.step / 20;
                this.players.forEach(player -> player.toPlayer().sendActionBar("§aStarting in " + (timeUntilStart > 5 ? "&l" : "&c&l") + timeUntilStart));
                if (timeUntilStart == 0) {
                    this.start();
                    return;
                }
                this.step++;
            }
            return;
        }
        for (final GravityPlayer player : this.players) {
            if (player.toPlayer().getLocation().getBlock().getType().equals(Material.NETHER_PORTAL))
                player.onSuccess();
            final int mapId = player.getMap();
            final Map currentMap = this.loadedMaps.get(mapId);
            if (player instanceof Gamer) {
                player.toPlayer().setLevel(player.toPlayer().getLocation().getBlockY() - currentMap.getFloor());
                final StringBuilder builder = new StringBuilder();
                if (player instanceof HardCoreGamer) {
                    builder.append("§cLifes : §4")
                            .append(((Gamer) player).getFails())
                            .append("/")
                            .append(this.getConfig().getMaxFails());
                } else {
                    builder.append("§cFails : §4")
                            .append(((Gamer) player).getFails());
                }
                builder.append("  §8§l| ");
                for (int i = 0; i < this.loadedMaps.size(); i++) {
                    final Map map = this.loadedMaps.get(i);
                    if (map.equals(currentMap)) {
                        builder.append(" §2» ")
                                .append(map.toColor())
                                .append(Others.getIntCircle(i))
                                .append(" §2«");
                    } else {
                        builder.append(" ")
                                .append(map.toColor())
                                .append(Others.getIntCircle(i));
                    }
                }
                player.toPlayer().sendActionBar(builder.toString());
            }
        }
        if (this.step % 2 == 0)
            this.updateClassement();
        if (this.finished.size() > 0) {
            if (this.step / 20 == this.getConfig().getTimeAfterWin()) {
                //stop
            }
        } else if (this.step / 20 == this.getConfig().getTimeMax()) {
            //stop
        } else if (this.getGamers().size() == 0) {
            //stop
        }
        this.step++;
    }

    public void stop() {
        this.step = 0;
        this.status = Status.WAITING;
    }

    public DoubleResult<Boolean, String> join(final Player player) {
        final DoubleResult<Boolean, String> answer = new DoubleResult<>();
        if ((this.status == Status.WAITING || this.status == Status.STARTING) && this.getPlayers().size() > this.getConfig().getPlayerMax()) {
            answer.setFirstValue(false);
            answer.setSecondValue("Full");
        } else if (this.status == Status.WAITING && this.getPlayers().size() <= this.getConfig().getPlayerMax()) {
            final GravityPlayer playerGame = new SimpleGamer(player.getUniqueId(), this);
            playerGame.join();
            for (final GravityPlayer players : this.getPlayers()) {
                players.toPlayer().sendMessage("sendJoinMessage");
            }
            this.getPlayers().add(playerGame);
            answer.setFirstValue(true);
            answer.setSecondValue("Joined");
        } else {
            final GravityPlayer playerGame = new SpectatorPlayer(player.getUniqueId(), this);
            playerGame.join();
            this.getPlayers().add(playerGame);
            answer.setFirstValue(true);
            answer.setSecondValue("Spectator");
        }
        return answer;
    }

    public List<Gamer> updateClassement() {
        final List<Gamer> result = Others.copy(this.finished);
        for (int index = this.loadedMaps.size() - 1; index >= 0 && result.size() <= 5; index--) {
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

    public void addWinner(final Gamer gamer) {
        this.finished.add(gamer);
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

    public List<Map> getLoadedMaps() {
        return this.loadedMaps;
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

    private enum Status {
        WAITING,
        STARTING,
        EXECUTING,
        ENDING;
    }
}
