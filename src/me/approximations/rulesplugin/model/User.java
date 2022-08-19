package me.approximations.rulesplugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import me.approximations.rulesplugin.Main;
import me.approximations.rulesplugin.dao.repository.UserRepository;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class User {
    private String nick;
    private boolean accepted;

    public User update() {
        UserRepository userRepository = Main.getInstance().getUserRepository();
        userRepository.insertOrUpdate(this);
        return this;
    }
}
