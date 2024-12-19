package uwu.lopyluna.obelisk.register.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.LoomMenu;
import org.jetbrains.annotations.NotNull;

public class LoomMenuCommand extends LoomMenu {
    public LoomMenuCommand(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        super(pContainerId, pPlayerInventory, EntityContainerLevelAccess.create(pPlayer));
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }
}
