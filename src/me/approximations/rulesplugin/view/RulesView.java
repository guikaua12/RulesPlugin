package me.approximations.rulesplugin.view;

import me.approximations.rulesplugin.manager.RulesManager;
import me.approximations.rulesplugin.model.Rule;
import me.approximations.rulesplugin.model.User;
import me.approximations.rulesplugin.util.ItemBuilder;
import me.saiintbrisson.minecraft.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RulesView extends PaginatedView<Rule> {
    public RulesView(RulesManager rulesManager) {
        super(6, "Rules");
        setCancelOnClick(true);
        setLayout("XXXXXXXXX",
                  "XOOOOOOOX",
                  "<OOOOOOO>",
                  "XOOOOOOOX",
                  "XXXXXXXXX",
                  "XXXXXXXXX"
                );
        setSource(rulesManager.getRulesList());

        slot(38, rulesManager.getRefuseItem()).onClick(click -> {
            Player p = click.getPlayer();
            rulesManager.refuse(p);
        });
        slot(42, rulesManager.getAcceptItem()).onClick(click -> {
            Player p = click.getPlayer();
            rulesManager.accept(p);
            click.set("canClose", true);
            click.close();
        });
    }

    @Override
    protected void onItemRender(PaginatedViewSlotContext<Rule> render, ViewItem item, Rule value) {
        item.withItem(value.getDisplayItem());
    }

    @Override
    protected void onRender(ViewContext context) {
        context.set("canClose", false);
    }

    @Override
    protected void onClose(ViewContext context) {
        boolean canClose = context.get("canClose");
        context.setCancelled(!canClose);
    }

    @Override
    public ViewItem getNextPageItem(PaginatedViewContext<Rule> context) {
        return context.item(context.paginated().hasNextPage() ? new ItemBuilder("51e94e1130201af1a3062f62d42ecbb55d59e4cabb3435a0e0062bc008cb0985").setName("§aPróxima página").toItemStack()
                :
                new ItemStack(Material.AIR)
        );
    }

    @Override
    public ViewItem getPreviousPageItem(PaginatedViewContext<Rule> context) {
        return context.item(context.paginated().hasPreviousPage() ? new ItemBuilder("98b1b4819fda0f0babbcd36c8404f34862b529409ea55775712c0b67945977ea").setName("§aPágina anterior").toItemStack()
                :
                new ItemStack(Material.AIR)
        );
    }
}
