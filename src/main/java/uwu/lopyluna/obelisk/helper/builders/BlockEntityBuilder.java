package uwu.lopyluna.obelisk.helper.builders;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.obelisk.helper.entry.BlockEntityEntry;

public class BlockEntityBuilder<T extends BlockEntity> {
    private final String name;
    private final BlockEntityType.BlockEntitySupplier<T> factory;
    private final Block[] validBlocks;
    public final DeferredRegister<BlockEntityType<?>> blockEntityRegister;

    public BlockEntityBuilder(DeferredRegister<BlockEntityType<?>> blockEntityRegister, String name, BlockEntityType.BlockEntitySupplier<T> pFactory, Block... pValidBlocks) {
        this.blockEntityRegister = blockEntityRegister;
        this.name = name;
        this.factory = pFactory;
        this.validBlocks = pValidBlocks;
    }

    public BlockEntityEntry<T> register() {
        @SuppressWarnings("all")
        DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder = blockEntityRegister.register(name, () -> BlockEntityType.Builder.of(factory, validBlocks)
                .build(null));

        return new BlockEntityEntry<>(holder, name);
    }
}
