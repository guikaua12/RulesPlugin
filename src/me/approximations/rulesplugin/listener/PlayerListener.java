package me.approximations.rulesplugin.listener;

import com.google.common.collect.ImmutableMap;
import me.approximations.rulesplugin.Main;
import me.approximations.rulesplugin.dao.repository.UserRepository;
import me.approximations.rulesplugin.manager.RulesManager;
import me.approximations.rulesplugin.model.User;
import me.approximations.rulesplugin.view.RulesView;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final Main plugin;
    private final UserRepository userRepository;
    private final RulesManager rulesManager;
    public PlayerListener(Main plugin) {
        this.plugin = plugin;
        userRepository = plugin.getUserRepository();
        rulesManager = plugin.getRulesManager();
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        User user = userRepository.get(p.getName());
        if(user == null) {
            user = userRepository.insert(new User(p.getName(), false));
        }
        if(!user.isAccepted()) {
            rulesManager.getNotAcceptedList().put(p.getName(), user);
            rulesManager.setPlayerCanWalk(p, false);

            User finalUser = user;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.getViewFrame().open(RulesView.class, p, ImmutableMap.of("user", finalUser));
            }, 5L);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        rulesManager.getNotAcceptedList().remove(p.getName());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(rulesManager.getNotAcceptedList().containsKey(p.getName())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.getFrom().distance(e.getTo()) == 0) return;

        Player p = e.getPlayer();
        if(rulesManager.getNotAcceptedList().containsKey(p.getName())) e.setCancelled(true);
    }



}
