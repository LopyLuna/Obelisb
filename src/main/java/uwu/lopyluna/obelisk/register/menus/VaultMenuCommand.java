package uwu.lopyluna.obelisk.register.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class VaultMenuCommand extends ChestMenu {
    public static final Map<Integer, BarrelBlockEntity> vaults = new HashMap<>();

    public VaultMenuCommand(int pContainerId, Inventory pPlayerInventory, BarrelBlockEntity pBlockEntity) {
        super(MenuType.GENERIC_9x3, pContainerId, pPlayerInventory, pBlockEntity, 3);
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }
}
