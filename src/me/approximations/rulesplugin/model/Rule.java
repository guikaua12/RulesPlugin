package me.approximations.rulesplugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Rule {
    private int displayOrder;
    private ItemStack displayItem;
}
