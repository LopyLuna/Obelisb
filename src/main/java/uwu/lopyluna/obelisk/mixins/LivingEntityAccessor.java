package uwu.lopyluna.obelisk.mixins;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("attackStrengthTicker")
    int obelisk$getAttackStrengthTicker();
    @Accessor("attackStrengthTicker")
    void obelisk$setAttackStrengthTicker(int value);
}
