package me.approximations.rulesplugin.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.approximations.rulesplugin.model.User;

public class UserSerializer implements Serializer<User>{
    @Getter
    private static final UserSerializer instance = new UserSerializer();
    @Getter
    private final Gson gson = new GsonBuilder().setPrettyPrinting()/*.excludeFieldsWithoutExposeAnnotation()*/.create();


    @Override
    public String encode(User data) {
        return gson.toJson(data);
    }

    @Override
    public User decode(String data) {
        return gson.fromJson(data, User.class);
    }
}
