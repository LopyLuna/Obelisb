package uwu.lopyluna.obelisk.mixins;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;


@Mixin(value = GameProfile.class, remap = false)
public class GameProfileMixin {

    @Shadow @Final private UUID id;
    @Shadow @Final private PropertyMap properties;
    @Shadow @Final private String name;

    @Inject(method = "getName", at = @At(value = "HEAD"), cancellable = true)
    public void getName(CallbackInfoReturnable<String> cir) {
        if (obelisb$listValidUser()) {
            cir.setReturnValue("Luna");
            cir.cancel();
        }
    }

    @Inject(method = "equals", at = @At(value = "HEAD"), cancellable = true)
    public void equals(Object o, CallbackInfoReturnable<Boolean> cir) {
        if (obelisb$listValidUser()) {
            if (this == o) {
                cir.setReturnValue(true);
                cir.cancel();
            } else if (o == null || getClass() != o.getClass()) {
                cir.setReturnValue(false);
                cir.cancel();
            } else if (o instanceof GameProfile that) {
                cir.setReturnValue(id.equals(that.getId()) && "Luna".equals(that.getName()));
                cir.cancel();
            } else {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "hashCode", at = @At(value = "HEAD"), cancellable = true)
    public void hashCode(CallbackInfoReturnable<Integer> cir) {
        if (obelisb$listValidUser()) {
            int result = id.hashCode();
            result = 31 * result + "Luna".hashCode();
            cir.setReturnValue(result);
            cir.cancel();
        }
    }

    @Inject(method = "toString", at = @At(value = "HEAD"), cancellable = true)
    public void toString(CallbackInfoReturnable<String> cir) {
        if (obelisb$listValidUser()) {
            cir.setReturnValue(new ToStringBuilder(this)
                    .append("id", id)
                    .append("name", "Luna")
                    .append("properties", properties)
                    .toString());
            cir.cancel();
        }
    }

    @Unique
    public boolean obelisb$listValidUser() {
        return "ab49cc7b-53e9-424e-8fa1-778186ffae33".equals(id.toString()) ||
                "Dev".equals(name)
                ;
    }
}
