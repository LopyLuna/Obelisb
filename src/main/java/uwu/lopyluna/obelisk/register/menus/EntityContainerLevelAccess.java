package uwu.lopyluna.obelisk.register.menus;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiFunction;

public interface EntityContainerLevelAccess extends ContainerLevelAccess {

    static ContainerLevelAccess create(final Entity pEntity) {
        return new ContainerLevelAccess() {
            @Override
            public <T> @NotNull Optional<T> evaluate(@NotNull BiFunction<Level, BlockPos, T> func) {
                return Optional.ofNullable(func.apply(pEntity.level(), pEntity.blockPosition()));
            }
        };
    }

}
