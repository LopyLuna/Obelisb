package uwu.lopyluna.obelisk.helper.builders;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.obelisk.helper.entry.BlockEntry;
import uwu.lopyluna.obelisk.helper.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BlockItemBuilder<T extends Block, I extends BlockItem> {
    public final String name;
    public final Function<BlockItem.Properties, I> func;
    public Item.Properties properties = new Item.Properties();
    public String langKey;

    public final DeferredRegister<Item> itemRegister;
    public static final List<ItemEntry<? extends BlockItem>> ENTRIES = new ArrayList<>();

    private final BlockBuilder<T> blockBuilder;

    public BlockItemBuilder(BlockBuilder<T> blockBuilder, String name, DeferredRegister.Items itemRegister, BiFunction<T, BlockItem.Properties, I> blockItem, BlockEntry<T> blockEntry) {
        this.blockBuilder = blockBuilder;
        this.name = name;
        this.func = p -> blockItem.apply(blockEntry.get(), p);
        this.itemRegister = itemRegister;
        blockBuilder.setBlockItemEntry(this.buildEntry());
    }

    // SPECIAL PROPERTIES
    public BlockItemBuilder<T, I> properties(Function<Item.Properties, Item.Properties> propertiesFunction) {
        this.properties = propertiesFunction.apply(this.properties);
        return this;
    }

    public BlockItemBuilder<T, I> lang(String langKey) {
        this.langKey = langKey;
        return this;
    }

    //END OF SPECIAL PROPERTIES
    private ItemEntry<I> buildEntry() {
        DeferredHolder<Item, I> holder = this.itemRegister.register(this.name, () -> func.apply(properties));
        ItemEntry<I> entry = new ItemEntry<>(holder, langKey, name);
        ENTRIES.add(entry);
        return entry;
    }

    public BlockBuilder<T> build() {
        return blockBuilder;
    }

    public static List<ItemEntry<? extends BlockItem>> getEntries() {
        return ENTRIES;
    }
}