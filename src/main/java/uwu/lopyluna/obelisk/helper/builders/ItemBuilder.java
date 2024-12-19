package uwu.lopyluna.obelisk.helper.builders;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.obelisk.helper.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class ItemBuilder<T extends Item> {
    public final String name;
    public final Function<Item.Properties, ? extends T> func;
    public Item.Properties properties = new Item.Properties();
    public String langKey;

    public final DeferredRegister<Item> itemRegister;
    public static final List<ItemEntry<?>> ENTRIES = new ArrayList<>();

    public ItemBuilder(String name, Function<Item.Properties, ? extends T> func, DeferredRegister.Items itemRegister) {
        this.name = name;
        this.func = func;
        this.itemRegister = itemRegister;
    }

    //SPECIAL PROPERTIES
    public ItemBuilder<T> properties(Function<Item.Properties, Item.Properties> propertiesFunction) {
        this.properties = propertiesFunction.apply(this.properties);
        return this;
    }

    public ItemBuilder<T> lang(String langKey) {
        this.langKey = langKey;
        return this;
    }
    //END OF SPECIAL PROPERTIES

    public ItemEntry<T> register() {
        DeferredHolder<Item, T> holder = this.itemRegister.register(this.name, () -> func.apply(properties));

        if (langKey == null || langKey.isBlank()) {
            langKey = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().replace('_', ' ');
        }

        ItemEntry<T> entry = new ItemEntry<>(holder, langKey, name);

        ENTRIES.add(entry);
        return entry;
    }

    public static List<ItemEntry<? extends Item>> getEntries() {
        return ENTRIES;
    }
}
