package uwu.lopyluna.obelisk.helper.builders;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import uwu.lopyluna.obelisk.helper.entry.BlockEntry;
import uwu.lopyluna.obelisk.helper.entry.ItemEntry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.BlockItem;

import java.util.function.BiFunction;
import java.util.function.Function;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BlockBuilder<T extends Block> {
    private static final List<BlockEntry<?>> BLOCK_ENTRIES = new ArrayList<>();
    private static final List<ItemEntry<? extends BlockItem>> ITEM_ENTRIES = new ArrayList<>();

    private final String name;
    private final Function<BlockBehaviour.Properties, T> func;
    private BlockBehaviour.Properties properties;
    private final DeferredRegister.Blocks blockRegister;
    private final DeferredRegister.Items itemRegister;
    private BlockEntry<T> blockEntry;
    private ItemEntry<? extends BlockItem> blockItemEntry;
    private String blockLangKey;
    private String blockItemLangKey;

    public BlockBuilder(String name, Function<BlockBehaviour.Properties, T> func, DeferredRegister.Blocks blockRegister, DeferredRegister.Items itemRegister) {
        this.name = name;
        this.func = func;
        this.blockRegister = blockRegister;
        this.itemRegister = itemRegister;
        this.properties = Block.Properties.of();
    }

    public BlockBuilder<T> properties(Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertyModifier) {
        this.properties = propertyModifier.apply(this.properties);
        return this;
    }

    public BlockBuilder<T> lang(String langKey) {
        this.blockLangKey = langKey;
        return this;
    }

    public BlockItemBuilder<T, ? extends BlockItem> blockItem() {
        return blockItem(BlockItem::new);
    }

    public BlockItemBuilder<T, ? extends BlockItem> blockItem(BiFunction<T, BlockItem.Properties, ? extends BlockItem> blockItem) {
        if (blockEntry == null) {
            blockEntry = new BlockEntry<>(this.blockRegister.register(this.name, () -> func.apply(properties)), blockLangKey, name);
            BLOCK_ENTRIES.add(blockEntry);
        }
        return new BlockItemBuilder<>(this, this.name, this.itemRegister, blockItem, blockEntry);
    }

    public BlockBuilder<T> langBlockItem(String langKey) {
        this.blockItemLangKey = langKey;
        return this;
    }

    protected void setBlockItemEntry(ItemEntry<? extends BlockItem> blockItemEntry) {
        this.blockItemEntry = blockItemEntry;
        ITEM_ENTRIES.add(blockItemEntry);
    }

    DeferredRegister.Items getItemRegister() {
        return this.itemRegister;
    }

    public BlockEntry<T> register() {
        if (blockEntry == null) {
            blockEntry = new BlockEntry<>(this.blockRegister.register(this.name, () -> func.apply(properties)), blockLangKey, name);
            BLOCK_ENTRIES.add(blockEntry);
        }

        if (blockItemEntry != null && blockItemLangKey != null) {
            blockItemEntry = new ItemEntry<>(blockItemEntry.getHolder(), blockItemLangKey, blockItemEntry.getName());
        }

        return blockEntry;
    }

    public static List<BlockEntry<?>> getEntries() {
        return BLOCK_ENTRIES;
    }

    public static List<ItemEntry<? extends BlockItem>> getItemEntries() {
        return ITEM_ENTRIES;
    }
}