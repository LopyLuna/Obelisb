package uwu.lopyluna.obelisk.mixins;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.lopyluna.obelisk.helper.access.PlayerAccess;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin {
    protected ServerPlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "restoreFrom", at = @At(value = "HEAD"))
    public void restoreFrom(ServerPlayer pThat, boolean pKeepEverything, CallbackInfo ci) {
        this.obelisk$vaultInventory = ((PlayerAccess) pThat).getObelisk$vaultInventory();
    }
}
