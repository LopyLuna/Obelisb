package uwu.lopyluna.obelisk.helper.entry;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

@SuppressWarnings("unused")
public record BlockEntry<T extends Block>(DeferredHolder<Block, T> holder, String langKey, String name) {
    public T get() {
        return holder.get();
    }

    public DeferredHolder<Block, T> getHolder() {
        return holder;
    }

    public String getLangKey() {
        return langKey != null ? langKey : name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().replace('_', ' ');
    }

    public String getName() {
        return name;
    }

}

