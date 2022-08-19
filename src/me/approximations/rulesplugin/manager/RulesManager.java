package me.approximations.rulesplugin.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.approximations.dependencyApi.util.Utils;
import me.approximations.rulesplugin.Main;
import me.approximations.rulesplugin.dao.repository.UserRepository;
import me.approximations.rulesplugin.model.Rule;
import me.approximations.rulesplugin.model.User;
import me.approximations.rulesplugin.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RulesManager {
    private final Main plugin;
    private final FileConfiguration rulesConfig;

    public RulesManager(Main plugin) {
        this.plugin = plugin;
        rulesConfig = plugin.getRulesConfig().getConfig();
    }
    @Getter
    private final Map<String, User> notAcceptedList = new HashMap<>();
    @Getter
    private final List<Rule> rulesList = new ArrayList<>();

    public void accept(Player p) {
        User user = notAcceptedList.get(p.getName());
        user.setAccepted(true);
        user.update();
        notAcceptedList.remove(p.getName());
        p.sendMessage("§aYou accepted the rules, welcome!");
        p.playSound(p.getEyeLocation(), Sound.ORB_PICKUP, 1F, 1F);
        setPlayerCanWalk(p, true);
    }

    public void refuse(Player p) {
        setPlayerCanWalk(p, true);
        new BukkitRunnable(){
            @Override
            public void run() {
                p.kickPlayer("§cYou cannot play without agreeing to the rules.");
            }
        }.runTaskLater(plugin, 2L);
    }

    public void setPlayerCanWalk(Player p, boolean b) {
        if(b) {
            p.setWalkSpeed(0.2F);
            p.removePotionEffect(PotionEffectType.JUMP);
        }else {
            p.setWalkSpeed(0F);
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -50, true, false));
        }
    }

    public ItemStack getAcceptItem() {
        ConfigurationSection cfg = rulesConfig.getConfigurationSection("AcceptItem");
        String[] ms = cfg.getString("Item").split(":");
        return new ItemBuilder(cfg.getBoolean("CustomSkull") ? Utils.getHeadUrl(cfg.getString("Url")) : new ItemStack(Material.valueOf(ms[0]), 1, Byte.parseByte(ms[1])))
                .setName(cfg.getString("Name").replace("&", "§"))
                .setLore(cfg.getStringList("Lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()))
                .toItemStack();
    }

    public ItemStack getRefuseItem() {
        ConfigurationSection cfg = rulesConfig.getConfigurationSection("RefuseItem");
        String[] ms = cfg.getString("Item").split(":");
        return new ItemBuilder(cfg.getBoolean("CustomSkull") ? Utils.getHeadUrl(cfg.getString("Url")) : new ItemStack(Material.valueOf(ms[0]), 1, Byte.parseByte(ms[1])))
                .setName(cfg.getString("Name").replace("&", "§"))
                .setLore(cfg.getStringList("Lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()))
                .toItemStack();
    }

}
