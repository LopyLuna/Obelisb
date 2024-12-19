package uwu.lopyluna.obelisk.register.container;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerVaultContainer extends SimpleContainer {

    public PlayerVaultContainer() {
        super(9*6);
    }

    @Override
    public void fromTag(@NotNull ListTag pTag, HolderLookup.@NotNull Provider pLevelRegistry) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.setItem(i, ItemStack.EMPTY);
        }

        for (int k = 0; k < pTag.size(); k++) {
            CompoundTag compoundtag = pTag.getCompound(k);
            int j = compoundtag.getByte("Slot") & 255;
            if (j >= 0 && j < this.getContainerSize()) {
                this.setItem(j, ItemStack.parse(pLevelRegistry, compoundtag).orElse(ItemStack.EMPTY));
            }
        }
    }

    @Override
    public @NotNull ListTag createTag(HolderLookup.@NotNull Provider pLevelRegistry) {
        ListTag listtag = new ListTag();

        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                listtag.add(itemstack.save(pLevelRegistry, compoundtag));
            }
        }

        return listtag;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }
}
