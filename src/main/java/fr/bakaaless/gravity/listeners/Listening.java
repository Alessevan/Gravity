package fr.bakaaless.gravity.listeners;

import fr.bakaaless.gravity.game.GravityGame;
import fr.bakaaless.gravity.game.gamers.Gamer;
import fr.bakaaless.gravity.game.gamers.GravityPlayer;
import fr.bakaaless.gravity.storage.GeneralConfig;
import fr.bakaaless.gravity.utils.DoubleResult;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class Listening implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (GeneralConfig.get().isBungee()) {
            GravityGame.getGames().get(0).join(e.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        GravityGame.getGames().forEach(game -> {
            game.getPlayers().stream().filter(player -> player.getUniqueId().equals(e.getPlayer().getUniqueId()))
                    .forEach(GravityPlayer::remove);
        });
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent e) {
        if (e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        final Optional<DoubleResult<GravityGame, GravityPlayer>> values = GravityGame.fromPlayer((Player) e.getEntity());
        values.ifPresent(result -> {
            e.setCancelled(true);
            if (result.getSecondValue() instanceof Gamer)
                ((Gamer) result.getSecondValue()).onFail();
        });
    }
}
