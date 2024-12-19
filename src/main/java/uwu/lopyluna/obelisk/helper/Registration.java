package uwu.lopyluna.obelisk.helper;

import static net.minecraft.core.registries.Registries.*;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.obelisk.helper.builders.BlockBuilder;
import uwu.lopyluna.obelisk.helper.builders.BlockEntityBuilder;
import uwu.lopyluna.obelisk.helper.builders.BlockItemBuilder;
import uwu.lopyluna.obelisk.helper.builders.ItemBuilder;
import uwu.lopyluna.obelisk.helper.entry.ItemEntry;
import uwu.lopyluna.obelisk.helper.provider.DataGeneratorLangProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
public class Registration {
    public final String MOD_ID;
    public final String NAME;

    public final DeferredRegister.DataComponents DATA_COMPONENTS;
    public final DeferredRegister<Fluid> FLUIDS;
    public final DeferredRegister.Blocks BLOCKS;
    public final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES;
    public final DeferredRegister.Items ITEMS;
    public final DeferredRegister<DamageType> DAMAGES;
    public final DeferredRegister<MobEffect> MOB_EFFECTS;
    public final DeferredRegister<ParticleType<?>> PARTICLES;
    public final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    public final DeferredRegister<RecipeType<?>> RECIPES;
    public final DeferredRegister<MenuType<?>> MENUS;
    public final DeferredRegister<SoundEvent> SOUNDS;
    public final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS;
    public final DeferredRegister<Level> DIMENSIONS;

    public static Registration register(String name, boolean isModID) {
        return isModID ? Registration.register(name, name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().replace('_', ' ')) : Registration.register(name.toLowerCase().replace(" ", "_"), name);
    }

    public static Registration register(String modID, String name) {
        return new Registration(modID, name, DeferredRegister.createDataComponents(modID), DeferredRegister.create(FLUID, modID), DeferredRegister.createBlocks(modID), DeferredRegister.create(BLOCK_ENTITY_TYPE, modID), DeferredRegister.createItems(modID), DeferredRegister.create(DAMAGE_TYPE, modID), DeferredRegister.create(MOB_EFFECT, modID), DeferredRegister.create(PARTICLE_TYPE, modID), DeferredRegister.create(RECIPE_SERIALIZER, modID), DeferredRegister.create(RECIPE_TYPE, modID), DeferredRegister.create(MENU, modID), DeferredRegister.create(SOUND_EVENT, modID), DeferredRegister.create(CREATIVE_MODE_TAB, modID), DeferredRegister.create(DIMENSION, modID));
    }

    private Registration(String modID, String name, DeferredRegister.DataComponents dataComponents, DeferredRegister<Fluid> fluids, DeferredRegister.Blocks blocks, DeferredRegister<BlockEntityType<?>> blockEntities, DeferredRegister.Items items, DeferredRegister<DamageType> damages, DeferredRegister<MobEffect> mobEffects, DeferredRegister<ParticleType<?>> particles, DeferredRegister<RecipeSerializer<?>> recipeSerializers, DeferredRegister<RecipeType<?>> recipes, DeferredRegister<MenuType<?>> menus, DeferredRegister<SoundEvent> sounds, DeferredRegister<CreativeModeTab> creativeModeTabs, DeferredRegister<Level> dimensions) {
        MOD_ID = modID;
        NAME = name;
        DATA_COMPONENTS = dataComponents;
        FLUIDS = fluids;
        BLOCKS = blocks;
        BLOCK_ENTITIES = blockEntities;
        ITEMS = items;
        DAMAGES = damages;
        MOB_EFFECTS = mobEffects;
        PARTICLES = particles;
        RECIPE_SERIALIZERS = recipeSerializers;
        RECIPES = recipes;
        MENUS = menus;
        SOUNDS = sounds;
        CREATIVE_MODE_TABS = creativeModeTabs;
        DIMENSIONS = dimensions;
    }

    public DeferredRegister.DataComponents components() {return DATA_COMPONENTS;}
    public DeferredRegister<? extends Fluid> fluids() {return FLUIDS;}
    public DeferredRegister.Blocks blocks() {return BLOCKS;}
    public <T extends Block, I extends BlockItem> BlockBuilder<T> block(String name, Function<BlockBehaviour.Properties, T> factory) {
        return new BlockBuilder<>(name, factory, blocks(), items());
    }
    public DeferredRegister<BlockEntityType<?>> blockEntities() {return BLOCK_ENTITIES;}

    public <T extends BlockEntity> BlockEntityBuilder<T> blockEntity(String name, BlockEntityType.BlockEntitySupplier<T> pFactory, Block... pValidBlocks) {
        return new BlockEntityBuilder<>(blockEntities(), name, pFactory, pValidBlocks);
    }
    public DeferredRegister.Items items() {return ITEMS;}
    public <T extends Item> ItemBuilder<T> item(String name, Function<Item.Properties, T> factory) {
        return new ItemBuilder<>(name, factory, items());
    }
    public DeferredRegister<CreativeModeTab> creativeTab() { return CREATIVE_MODE_TABS; }
    public DeferredRegister<DamageType> damages() { return DAMAGES; }
    public DeferredRegister<MobEffect> mobEffects() { return MOB_EFFECTS; }
    public DeferredRegister<ParticleType<?>> particles() { return PARTICLES; }
    public DeferredRegister<RecipeSerializer<?>> recipe_ser() { return RECIPE_SERIALIZERS; }
    public DeferredRegister<RecipeType<?>> recipes() { return RECIPES; }
    public DeferredRegister<MenuType<?>> menus() { return MENUS; }
    public DeferredRegister<SoundEvent> sounds() { return SOUNDS; }
    public DeferredRegister<Level> dimensions() { return DIMENSIONS; }

    public void lang(String key, String display) { lang("", key, display, ""); }
    public void lang(String display) { lang("", display, ""); }

    public void lang(String prefixType, String key, String display, String suffixType) {
        String prefix = prefixType.isEmpty() ? "" : prefixType + ".";
        String suffix = suffixType.isEmpty() ? "" : "." + suffixType;
        String langKey = prefix + MOD_ID + "." + key + suffix;
        langEntries.put(langKey.toLowerCase().replace(" ", "_"), display);
    }
    public void lang(String prefixType, String display, String suffixType) {
        String prefix = prefixType.isEmpty() ? "" : prefixType + ".";
        String suffix = suffixType.isEmpty() ? "" : "." + suffixType;
        String langKey = prefix + MOD_ID + "." + display + suffix;
        langEntries.put(langKey.toLowerCase().replace(" ", "_"), display);
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> BASE_TAB;

    public void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
        FLUIDS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        DAMAGES.register(bus);
        MOB_EFFECTS.register(bus);
        PARTICLES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        RECIPES.register(bus);
        MENUS.register(bus);
        SOUNDS.register(bus);
        DIMENSIONS.register(bus);

        BASE_TAB = creativeTab().register(MOD_ID + "_tab", () -> CreativeModeTab.builder()
                .title(Component.translatableWithFallback("itemGroup." + MOD_ID + ".base", NAME))
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(Items.DIAMOND::getDefaultInstance)
                .displayItems(itemForCreativeTab())
                .build());
    }

    public void generateData(IEventBus bus) {
        bus.addListener(this::gatherData);
    }

    private static final Map<String, String> langEntries = new HashMap<>();

    public void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        setupDataGeneration();
        generator.addProvider(true, new DataGeneratorLangProvider(output, langEntries));
    }

    public CreativeModeTab.DisplayItemsGenerator itemForCreativeTab() {
        return (parameters, output) -> getItemEntries().forEach(entry -> output.accept(entry.get()));
    }

    public static List<ItemEntry<? extends Item>> getItemEntries() {
        List<ItemEntry<? extends Item>> items = new ArrayList<>(List.of());
        if (!ItemBuilder.getEntries().isEmpty())
            items.addAll(ItemBuilder.getEntries());
        if (!BlockItemBuilder.getEntries().isEmpty())
            items.addAll(BlockItemBuilder.getEntries());
        return items;
    }

    private void setupDataGeneration() {
        getItemEntries().forEach(entry -> lang("item", entry.getName(), entry.getLangKey(), ""));
        BlockBuilder.getEntries().forEach(entry -> lang("block", entry.getName(), entry.getLangKey(), ""));
    }

    private static String convertToLangName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).replace('_', ' ');
    }
}
