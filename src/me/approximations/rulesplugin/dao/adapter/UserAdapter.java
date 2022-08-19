package me.approximations.rulesplugin.dao.adapter;

import com.jaoow.sql.executor.adapter.SQLResultAdapter;
import com.jaoow.sql.executor.result.SimpleResultSet;
import me.approximations.rulesplugin.model.User;
import me.approximations.rulesplugin.serializer.UserSerializer;

public class UserAdapter implements SQLResultAdapter<User> {
    @Override
    public User adaptResult(SimpleResultSet rs) {
        return UserSerializer.getInstance().decode(rs.get("user"));
    }
}
