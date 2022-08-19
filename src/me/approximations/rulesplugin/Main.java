package me.approximations.rulesplugin;

import lombok.Getter;
import me.approximations.dependencyApi.util.ConfigReader;
import me.approximations.dependencyApi.util.Utils;
import me.approximations.rulesplugin.dao.SQLProvider;
import me.approximations.rulesplugin.dao.repository.UserRepository;
import me.approximations.rulesplugin.listener.PlayerListener;
import me.approximations.rulesplugin.manager.RulesManager;
import me.approximations.rulesplugin.model.Rule;
import me.approximations.rulesplugin.util.ItemBuilder;
import me.approximations.rulesplugin.view.RulesView;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.ViewFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class Main extends JavaPlugin {
    @Getter
    private static Main instance;
    @Getter
    private ConfigReader rulesConfig;
    @Getter
    private BukkitFrame bukkitFrame;
    @Getter
    private ViewFrame viewFrame;
    @Getter
    private RulesManager rulesManager;
    @Getter
    private SQLProvider sqlProvider;
    @Getter
    private UserRepository userRepository;

    @Override
    public void onLoad() {
        rulesConfig = new ConfigReader(this, "", "rules.yml");
        rulesConfig.saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        instance = this;
        setupDatabase();
        setupManager();
        setupView();
        setupCommand();
        setupListener();
        setupRules();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void setupListener() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void setupCommand() {
        bukkitFrame = new BukkitFrame(this);
        MessageHolder ms = bukkitFrame.getMessageHolder();
        ms.setMessage(MessageType.INCORRECT_USAGE, "§cUtilize: {usage}");
        ms.setMessage(MessageType.NO_PERMISSION, "§cVocê não tem permissão para executar esse comando.");
        ms.setMessage(MessageType.INCORRECT_TARGET, "§cApenas {target} pode usar esse comando.");
        ms.setMessage(MessageType.ERROR, "§cOcorreu um erro durante a execução desse comando.");
    }

    private void setupDatabase() {
        sqlProvider = new SQLProvider(this);
        userRepository = new UserRepository(this, sqlProvider.setupDatabase());
        userRepository.createTable();
    }

    private void setupView() {
        viewFrame = new ViewFrame(this);
        viewFrame.register(new RulesView(rulesManager));
    }

    private void setupManager() {
        rulesManager = new RulesManager(this);
    }

    private void setupRules() {
        rulesConfig.getConfig().getKeys(false).stream()
                .filter(k -> !(k.equalsIgnoreCase("AcceptItem")) && !(k.equalsIgnoreCase("RefuseItem")))
                .forEach(r -> {
                    ConfigurationSection cfg = rulesConfig.getConfig().getConfigurationSection(r);
                    String[] ms = cfg.getString("DisplayItem.Item").split(":");
                    rulesManager.getRulesList().add(new Rule(cfg.getInt("DisplayOrder"), new ItemBuilder(cfg.getBoolean("CustomSkull") ? Utils.getHeadUrl(cfg.getString("Url")) : new ItemStack(Material.valueOf(ms[0]), 1, Byte.parseByte(ms[1])))
                            .setName(cfg.getString("DisplayItem.Name").replace("&", "§"))
                            .setLore(cfg.getStringList("DisplayItem.Lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()))
                            .toItemStack()));
                    rulesManager.getRulesList().sort((r1, r2) -> Integer.compare(r1.getDisplayOrder(), r2.getDisplayOrder()));
                });
    }
}