package me.approximations.rulesplugin.dao;

import com.jaoow.sql.connector.type.impl.MySQLDatabaseType;
import com.jaoow.sql.connector.type.impl.SQLiteDatabaseType;
import com.jaoow.sql.executor.SQLExecutor;
import me.approximations.rulesplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.SQLException;

public class SQLProvider {
    private final Main plugin;
    private final String IP;
    private final String DB;
    private final String USER;
    private final String PASSWORD;
    private final String TYPE;
    public SQLProvider(Main plugin) {
        this.plugin = plugin;
        FileConfiguration CONFIG = plugin.getConfig();
        this.IP = CONFIG.getString("database.ip");
        this.DB = CONFIG.getString("database.db");
        this.USER = CONFIG.getString("database.user");
        this.PASSWORD = CONFIG.getString("database.password");
        this.TYPE = CONFIG.getString("database.type");
    }

    public SQLExecutor setupDatabase(){
        if(this.TYPE.equalsIgnoreCase("mysql")) {
            MySQLDatabaseType mysql = MySQLDatabaseType.builder()
                    .address(this.IP)
                    .database(this.DB)
                    .username(this.USER)
                    .password(this.PASSWORD)
                    .build();
            try {
                return new SQLExecutor(mysql.connect());
            } catch (SQLException e) {
                this.plugin.getLogger().severe("Não foi possível conectar ao MySQL, desabilitando plugin.");
                Bukkit.getPluginManager().disablePlugin(plugin);
                e.printStackTrace();
            }
        }else if(this.TYPE.equalsIgnoreCase("sqlite")){
            SQLiteDatabaseType sqlite = SQLiteDatabaseType.builder()
                    .file(new File(new File(plugin.getDataFolder(), ""), "database.db"))
                    .build();

            try {
                return new SQLExecutor(sqlite.connect());
            } catch (SQLException e) {
                this.plugin.getLogger().severe("Não foi possível conectar ao MySQL, desabilitando plugin.");
                Bukkit.getPluginManager().disablePlugin(plugin);
                e.printStackTrace();
            }
        }
        return null;
    }

//    public void setupListener() {
//
//    }
}
