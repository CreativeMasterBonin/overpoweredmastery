package net.rk.overpoweredmastery;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.datagen.OMEnchantments;
import net.rk.overpoweredmastery.datagen.OMGlobalLootModifier;
import net.rk.overpoweredmastery.datagen.OMWorldgen;
import net.rk.overpoweredmastery.datamap.OMDatamaps;
import net.rk.overpoweredmastery.enchantment.EnchantmentEntityEffectTypes;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.entity.blockentity.OMBlockEntities;
import net.rk.overpoweredmastery.entity.custom.Fraud;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.menu.OMMenus;
import net.rk.overpoweredmastery.recipe.MultiAssemblerRecipe;
import net.rk.overpoweredmastery.recipe.MultiAssemblerRecipeDisplay;
import net.rk.overpoweredmastery.recipe.MultiAssemblerRecipeSerializer;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod(OverpoweredMastery.MODID)
public class OverpoweredMastery {
    public static final String MODID = "overpoweredmastery";
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,MODID);

    private static final DeferredRegister<RecipeBookCategory> RECIPE_CATEGORIES =
            DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY,MODID);
    private static final DeferredRegister<RecipeDisplay.Type<?>> RECIPE_DISPLAY_TYPES =
            DeferredRegister.create(Registries.RECIPE_DISPLAY,MODID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE,MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER,MODID);

    public static final Supplier<RecipeSerializer<MultiAssemblerRecipe>> MULTI_ASSEMBLER_SERIALIZER =
            RECIPE_SERIALIZERS.register("multi_assembler", MultiAssemblerRecipeSerializer::new);

    public static final Supplier<RecipeType<MultiAssemblerRecipe>> MULTI_ASSEMBLER_RECIPE =
            RECIPE_TYPES.register("multi_assembler",
                    RecipeType::simple);

    public static final Supplier<RecipeBookCategory> MULTI_ASSEMBLER_CATEGORY =
            RECIPE_CATEGORIES.register("multi_assembler",RecipeBookCategory::new);

    public static final Supplier<RecipeDisplay.Type<MultiAssemblerRecipeDisplay>> MULTI_ASSEMBLER_RECIPE_DISPLAY = RECIPE_DISPLAY_TYPES.register(
            "multi_assembler",
            () -> new RecipeDisplay.Type<>(MultiAssemblerRecipeDisplay.MAP_CODEC,MultiAssemblerRecipeDisplay.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> TICKS_ALLOWED_TILL_USELESS =
            DATA_COMPONENT_TYPES.register("ticks_allowed_till_useless",
                    () -> DataComponentType.<Integer>builder()
                            .persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());

    public static final Supplier<AttachmentType<Boolean>> USING_WUB_ITEM = ATTACHMENT_TYPES.register(
            "using_wub_item", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("using_wub_item")).build()
    );

    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> ALL_TAB = CREATIVE_MODE_TABS.register("overpoweredmastery_creative_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.overpoweredmastery"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(OMItems.ULTIMATE_INGOT::toStack)
            .build());

    public OverpoweredMastery(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onGatherData);
        modEventBus.addListener(this::registerDatamapTypes);
        EnchantmentEntityEffectTypes.ENCHANTMENT_ENTITY_EFFECTS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        DATA_COMPONENT_TYPES.register(modEventBus);
        RECIPE_CATEGORIES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        RECIPE_DISPLAY_TYPES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        OMMenus.MENU_TYPES.register(modEventBus);
        OMSoundEvents.SOUND_EVENTS.register(modEventBus);
        OMBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        OMBlocks.BLOCKS.register(modEventBus);
        OMItems.ITEMS.register(modEventBus);
        OMEntityTypes.ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::createDefaultAttributes);

        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    public void createDefaultAttributes(EntityAttributeCreationEvent event){
        event.put(OMEntityTypes.FRAUD.get(), Fraud.createAttributes());
    }

    protected void customItems(BuildCreativeModeTabContentsEvent event){
        ItemStack swordN = new ItemStack(Items.NETHERITE_SWORD);
        ItemStack bowOP = new ItemStack(Items.BOW);
        ItemStack signLol = new ItemStack(Items.OAK_SIGN);

        CreativeModeTab.ItemDisplayParameters params = event.getParameters();
        params.holders().lookup(Registries.ENCHANTMENT).ifPresent(en -> {
            DataComponentPatch opSword = DataComponentPatch.builder()
                    .set(DataComponents.ITEM_NAME,Component.literal("OP Test Sword"))
                    .set(DataComponents.TOOLTIP_STYLE, Identifier.parse("overpoweredmastery:om_epic"))
                    .build();
            if(en.get(Enchantments.KNOCKBACK).isPresent()
                    && en.get(Enchantments.FIRE_ASPECT).isPresent()
                    && en.get(Enchantments.EFFICIENCY).isPresent()){
                swordN.enchant(en.get(Enchantments.KNOCKBACK).get(),10);
                swordN.enchant(en.get(Enchantments.FIRE_ASPECT).get(),10);
                swordN.enchant(en.get(Enchantments.EFFICIENCY).get(),10);
                swordN.applyComponents(opSword);
                event.accept(swordN);
            }
        });
        params.holders().lookup(Registries.ENCHANTMENT).ifPresent(en -> {
            DataComponentPatch opBow = DataComponentPatch.builder()
                    .set(DataComponents.ITEM_NAME,Component.literal("OP Test Bow"))
                    .set(DataComponents.TOOLTIP_STYLE,Identifier.parse("overpoweredmastery:om_epic"))
                    .build();
            if(en.get(Enchantments.MULTISHOT).isPresent()
                    && en.get(Enchantments.FLAME).isPresent()
                    && en.get(Enchantments.QUICK_CHARGE).isPresent()){
                bowOP.enchant(en.get(Enchantments.MULTISHOT).get(),5);
                bowOP.enchant(en.get(Enchantments.FLAME).get(),10);
                bowOP.enchant(en.get(Enchantments.QUICK_CHARGE).get(),20);
                bowOP.applyComponents(opBow);
                event.accept(bowOP);
            }
        });
        params.holders().lookup(Registries.ENCHANTMENT).ifPresent(en -> {
            DataComponentPatch signLolC = DataComponentPatch.builder()
                    .set(DataComponents.ITEM_NAME,Component.literal("Battle Sign"))
                    .set(DataComponents.TOOLTIP_STYLE,Identifier.parse("overpoweredmastery:om_epic"))
                    .set(DataComponents.WEAPON,new Weapon(15,5))
                    .build();
            if(en.get(Enchantments.SHARPNESS).isPresent()
                    && en.get(Enchantments.FIRE_ASPECT).isPresent()){
                signLol.enchant(en.get(Enchantments.SHARPNESS).get(),10);
                signLol.enchant(en.get(Enchantments.FIRE_ASPECT).get(),20);
                signLol.applyComponents(signLolC);
                event.accept(signLol);
            }
        });

        ItemStack testSpear = new ItemStack(OMItems.TEST_SPEAR.asItem());
        params.holders().lookup(Registries.ENCHANTMENT).ifPresent(en -> {
            DataComponentPatch testSpearLol = DataComponentPatch.builder()
                    .set(DataComponents.ITEM_NAME,Component.literal("OP Testing Spear"))
                    .set(DataComponents.TOOLTIP_STYLE,Identifier.parse("overpoweredmastery:om_epic"))
                    .build();
            if(en.get(Enchantments.WIND_BURST).isPresent()
                    && en.get(Enchantments.RIPTIDE).isPresent() && en.get(Enchantments.UNBREAKING).isPresent()){
                testSpear.enchant(en.get(Enchantments.WIND_BURST).get(),255);
                testSpear.enchant(en.get(Enchantments.RIPTIDE).get(),255);
                testSpear.enchant(en.get(Enchantments.UNBREAKING).get(),10);
                testSpear.applyComponents(testSpearLol);
                event.accept(testSpear);
            }
        });
    }

    public static final BlockCapability<ResourceHandler<ItemResource>,@Nullable Direction> COMMON_DIRECTIONAL_BLOCK_ITEM_CAPABILITY =
            BlockCapability.createSided(
                    // Provide a name to uniquely identify the capability.
                    Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID, "common_directional_item_handler"),
                    // Provide the queried type. Here, we want to look up `ResourceHandler<ItemResource>` instances.
                    ResourceHandler.asClass());

    public void registerCapabilities(RegisterCapabilitiesEvent event){

    }

    protected void customOPItems(BuildCreativeModeTabContentsEvent event){
        CreativeModeTab.ItemDisplayParameters params = event.getParameters();

        ItemStack ultimateMace = new ItemStack(OMItems.ULTIMATE_MACE.asItem());
        params.holders().lookup(Registries.ENCHANTMENT).ifPresent(en -> {
            DataComponentPatch testSpearLol = DataComponentPatch.builder()
                    .set(DataComponents.ITEM_NAME,Component.literal("Max Testing Ultimate Mace"))
                    .set(DataComponents.TOOLTIP_STYLE,Identifier.parse("overpoweredmastery:om_epic"))
                    .build();
            if(en.get(Enchantments.THORNS).isPresent()
                    && en.get(Enchantments.LOOTING).isPresent() && en.get(Enchantments.UNBREAKING).isPresent()){
                ultimateMace.enchant(en.get(Enchantments.LOOTING).get(),3);
                ultimateMace.enchant(en.get(Enchantments.THORNS).get(),10);
                ultimateMace.enchant(en.get(Enchantments.UNBREAKING).get(),3);
                ultimateMace.applyComponents(testSpearLol);
                event.accept(ultimateMace);
            }
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.OP_BLOCKS){
            customItems(event);
            //customOPItems(event);
        }
        if(event.getTabKey() == CreativeModeTabs.COMBAT){

        }
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS){
            event.accept(OMItems.FRAUD_SPAWN_EGG.asItem());
        }
        if(event.getTabKey() == ALL_TAB.getKey()){
            // machines and devices
            //event.accept(OMItems.MULTI_ASSEMBLER);
            // ores
            event.accept(OMItems.INERT_BLUE_ESSENCE_ORE);
            event.accept(OMItems.INERT_GREEN_ESSENCE_ORE);
            event.accept(OMItems.INERT_YELLOW_ESSENCE_ORE);
            event.accept(OMItems.INERT_ORANGE_ESSENCE_ORE);
            event.accept(OMItems.INERT_RED_ESSENCE_ORE);
            // special ores
            event.accept(OMItems.INERT_LIGHT_ESSENCE_ORE);
            event.accept(OMItems.INERT_AURORAN_ESSENCE_ORE);
            event.accept(OMItems.INERT_DARK_ESSENCE_ORE);
            // deepslate ores
            event.accept(OMItems.DEEPSLATE_INERT_BLUE_ESSENCE_ORE);
            event.accept(OMItems.DEEPSLATE_INERT_GREEN_ESSENCE_ORE);
            event.accept(OMItems.DEEPSLATE_INERT_YELLOW_ESSENCE_ORE);
            event.accept(OMItems.DEEPSLATE_INERT_ORANGE_ESSENCE_ORE);
            event.accept(OMItems.DEEPSLATE_INERT_RED_ESSENCE_ORE);
            event.accept(OMItems.DEEPSLATE_INERT_LIGHT_ESSENCE_ORE);
            event.accept(OMItems.DEEPSLATE_INERT_AURORAN_ESSENCE_ORE);
            event.accept(OMItems.DEEPSLATE_INERT_DARK_ESSENCE_ORE);
            // ore items
            event.accept(OMItems.INERT_BLUE_ESSENCE);
            event.accept(OMItems.INERT_GREEN_ESSENCE);
            event.accept(OMItems.INERT_YELLOW_ESSENCE);
            event.accept(OMItems.INERT_ORANGE_ESSENCE);
            event.accept(OMItems.INERT_RED_ESSENCE);
            // special ore items
            event.accept(OMItems.INERT_LIGHT_ESSENCE);
            event.accept(OMItems.INERT_AURORAN_ESSENCE);
            event.accept(OMItems.INERT_DARK_ESSENCE);
            // materials
            event.accept(OMItems.STRANGE_STONE);
            event.accept(OMItems.CONCENTRATED_MULTI_ESSENCE);
            event.accept(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE);
            // tool bindings
            event.accept(OMItems.WOODEN_TOOL_BINDING);
            event.accept(OMItems.METAL_TOOL_BINDING);
            event.accept(OMItems.DIAMOND_TOOL_BINDING);
            event.accept(OMItems.NETHERITE_TOOL_BINDING);
            // electronic or other components
            event.accept(OMItems.REDSTONE_BASE_COMPONENT);
            event.accept(OMItems.AURORAN_PROCESSOR);
            event.accept(OMItems.ESSENCE_ELECTRONIC_CORE);
            // smithing templates
            event.accept(OMItems.ULTRA_CATALYST_TEMPLATE);
            // ingots and storage blocks
            event.accept(OMItems.ULTIMATE_INGOT);
            event.accept(OMItems.ULTIMATE_BLOCK);
            event.accept(OMItems.ULTRA_INGOT);
            event.accept(OMItems.ULTRA_BLOCK);
            // long spears
            event.accept(OMItems.WOODEN_SPEAR);
            event.accept(OMItems.STONE_SPEAR);
            event.accept(OMItems.GOLD_SPEAR);
            event.accept(OMItems.IRON_SPEAR);
            event.accept(OMItems.DIAMOND_SPEAR);
            event.accept(OMItems.NETHERITE_SPEAR);
            event.accept(OMItems.ULTIMATE_LONG_SPEAR);
            // staffs
            event.accept(OMItems.PRIMITIVE_STAFF);
            event.accept(OMItems.ENDERMARINE_STAFF);
            event.accept(OMItems.RESINVEIN_STAFF);
            // tier order weapons
            // starter
            event.accept(OMItems.BONE_SWORD);
            event.accept(OMItems.ENDARKENED_CROSSBOW);
            // penultimate
            event.accept(OMItems.PENULTIMATE_SWORD_CATALYST);
            event.accept(OMItems.PENULTIMATE_SWORD_DARK);
            event.accept(OMItems.PENULTIMATE_SWORD_LIGHT);
            event.accept(OMItems.PENULTIMATE_PICKAXE_CATALYST);
            //event.accept(OMItems.PENULTIMATE_PICKAXE_DARK);
            //event.accept(OMItems.PENULTIMATE_PICKAXE_LIGHT);
            event.accept(OMItems.PENULTIMATE_AXE_CATALYST);
            //event.accept(OMItems.PENULTIMATE_AXE_DARK);
            //event.accept(OMItems.PENULTIMATE_AXE_LIGHT);
            event.accept(OMItems.PENULTIMATE_SHOVEL_CATALYST);
            //event.accept(OMItems.PENULTIMATE_SHOVEL_DARK);
            //event.accept(OMItems.PENULTIMATE_SHOVEL_LIGHT);
            event.accept(OMItems.PENULTIMATE_HOE_CATALYST);
            //event.accept(OMItems.PENULTIMATE_HOE_DARK);
            //event.accept(OMItems.PENULTIMATE_HOE_LIGHT);
            // ultimate
            event.accept(OMItems.ULTIMATE_SWORD);
            event.accept(OMItems.ULTIMATE_SPEAR);
            event.accept(OMItems.ULTIMATE_PICKAXE);
            event.accept(OMItems.ULTIMATE_SHOVEL);
            event.accept(OMItems.ULTIMATE_HOE);
            event.accept(OMItems.ULTIMATE_BOW);
            event.accept(OMItems.ULTIMATE_FISHING_ROD);
            event.accept(OMItems.ULTIMATE_MACE);
            event.accept(OMItems.ULTIMATE_STAFF);
            // ultra (finale tier)
            event.accept(OMItems.ULTRA_SWORD);
            event.accept(OMItems.ULTRA_PICKAXE);
            // wubs
            event.accept(OMItems.RED_WUBS);
            event.accept(OMItems.NETHER_WUBS);
            event.accept(OMItems.CHICKEN_WUBS);
            event.accept(OMItems.GREEN_WUBS);
            event.accept(OMItems.PURPLE_WUBS);
            event.accept(OMItems.TRIAL_WUBS);
            event.accept(OMItems.OXIDIZED_TRIAl_WUBS);
            event.accept(OMItems.WEEPING_WUBS);
            // misc
            event.accept(OMItems.MOVING_PROBABLE_BLOCK_ITEM);
        }
    }

    private void onGatherData(GatherDataEvent.Client event){
        event.createDatapackRegistryObjects(OMWorldgen.WORLD_GEN_BUILDER.add(Registries.ENCHANTMENT,OMEnchantments::bootstrap));
        event.createProvider(OMGlobalLootModifier::new);
    }

    private void registerDatamapTypes(RegisterDataMapTypesEvent event){
        event.register(OMDatamaps.SMELTABLES);
    }
}
