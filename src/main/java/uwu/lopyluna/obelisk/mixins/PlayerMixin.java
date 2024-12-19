package uwu.lopyluna.obelisk.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.obelisk.helper.Utils;
import uwu.lopyluna.obelisk.helper.access.PlayerAccess;
import uwu.lopyluna.obelisk.register.container.PlayerVaultContainer;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerAccess {

    @Shadow @Final private GameProfile gameProfile;
    @Shadow @Final Inventory inventory;
    @Shadow protected PlayerEnderChestContainer enderChestInventory;
    @Unique protected PlayerVaultContainer obelisk$vaultInventory = new PlayerVaultContainer();

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
    public void readAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if (pCompound.contains("VaultItems", 9)) {
            obelisk$vaultInventory.fromTag(pCompound.getList("VaultItems", 10), this.registryAccess());
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
    public void addAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        pCompound.put("VaultItems", obelisk$vaultInventory.createTag(this.registryAccess()));
    }

    @Override
    public PlayerVaultContainer getObelisk$vaultInventory() {
        return obelisk$vaultInventory;
    }

    @Inject(method = "getSlot", at = @At(value = "HEAD"), cancellable = true)
    public void getSlot(int pSlot, CallbackInfoReturnable<SlotAccess> cir) {
        if (pSlot != 499) {
            final int i = pSlot - 500;
            int j = pSlot - 200;
            int obelisk = pSlot - 100;
            if (!(i >= 0 && i < 4) &&
                !(pSlot >= 0 && pSlot < inventory.items.size()) &&
                !(j >= 0 && j < enderChestInventory.getContainerSize())) {
                cir.setReturnValue(obelisk >= 0 && obelisk < obelisk$vaultInventory.getContainerSize() ? SlotAccess.forContainer(obelisk$vaultInventory, obelisk) : super.getSlot(pSlot));
                cir.cancel();
            }
        }
    }


    @Inject(method = "getName", at = @At(value = "HEAD"), cancellable = true)
    public void getName(CallbackInfoReturnable<Component> cir) {
        if (obelisb$listValidUser()) {
            cir.setReturnValue(Component.literal("Luna").withColor(Utils.color("#85f9ff")));
            cir.cancel();
        }
    }

    @Inject(method = "getScoreboardName", at = @At(value = "HEAD"), cancellable = true)
    public void getScoreboardName(CallbackInfoReturnable<String> cir) {
        if (obelisb$listValidUser()) {
            cir.setReturnValue("Luna");
            cir.cancel();
        }
    }

    @Inject(method = "decorateDisplayNameComponent", at = @At(value = "HEAD"), cancellable = true)
    private void decorateDisplayNameComponent(MutableComponent pDisplayName, CallbackInfoReturnable<MutableComponent> cir) {
        if (obelisb$listValidUser()) {
            cir.setReturnValue(pDisplayName.withColor(Utils.color("#85f9ff")).withStyle(
                    p_352834_ -> p_352834_.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell Luna "))
                            .withHoverEvent(createHoverEvent())
                            .withInsertion("Luna")
            ));
            cir.cancel();
        }
    }

    @Unique
    public boolean obelisb$listValidUser() {
        return "ab49cc7b-53e9-424e-8fa1-778186ffae33".equals(gameProfile.getId().toString()) ||
                "Dev".equals(gameProfile.getName())
                ;
    }
}
