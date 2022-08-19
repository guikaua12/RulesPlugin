package me.approximations.rulesplugin.dao.repository;

import com.jaoow.sql.executor.SQLExecutor;
import lombok.RequiredArgsConstructor;
import me.approximations.rulesplugin.dao.adapter.UserAdapter;
import me.approximations.rulesplugin.model.User;
import me.approximations.rulesplugin.serializer.UserSerializer;
import org.bukkit.plugin.Plugin;

import java.util.Set;

@RequiredArgsConstructor
public class UserRepository{
    private final Plugin plugin;
    private final SQLExecutor sqlExecutor;
    private final String TABLE = "rules_plugin";
    private final UserSerializer userSerializer = UserSerializer.getInstance();


    
    public void createTable() {
        sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS "+TABLE+"(nick TEXT, user TEXT);");
    }

    
    public User insertOrUpdate(User user) {
        if(contains(user.getNick())) {
            update(user);
        }else {
            insert(user);
        }
        return user;
    }

    
    public User insert(User user) {
        sqlExecutor.updateQuery("INSERT INTO "+TABLE+" VALUES(?, ?);", c -> {
            c.set(1, user.getNick());
            c.set(2, userSerializer.encode(user));
        });
        return user;
    }

    
    public User update(User user) {
        sqlExecutor.updateQuery("UPDATE "+TABLE+" SET user = ? WHERE nick = ?;", c -> {
            c.set(1, userSerializer.encode(user));
            c.set(2, user.getNick());
        });
        return user;
    }

    
    public void remove(String nick) {
        sqlExecutor.updateQuery("DELETE FROM "+TABLE+" WHERE nick = ?;", c -> {
            c.set(1, nick);
        });
    }

    
    public boolean contains(String nick) {
        return get(nick) != null;
    }

    
    public User get(String nick) {
        return sqlExecutor.resultOneQuery("SELECT * FROM "+TABLE+" WHERE nick = ?;", c -> {
            c.set(1, nick);
        }, UserAdapter.class);
    }

    
    public Set<User> getAll() {
        return sqlExecutor.resultManyQuery("SELECT * FROM TABLE;", c -> {}, UserAdapter.class);
    }
}
