package net.rk.overpoweredmastery;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

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
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,MODID);

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
            .icon(OMItems.ENDARKENED_CROSSBOW::toStack)
            .build());

    public OverpoweredMastery(IEventBus modEventBus, ModContainer modContainer) {
        ATTACHMENT_TYPES.register(modEventBus);
        DATA_COMPONENT_TYPES.register(modEventBus);
        OMSoundEvents.SOUND_EVENTS.register(modEventBus);
        OMBlocks.BLOCKS.register(modEventBus);
        OMItems.ITEMS.register(modEventBus);
        OMEntityTypes.ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    protected void customItems(BuildCreativeModeTabContentsEvent event){
        ItemStack swordN = new ItemStack(Items.NETHERITE_SWORD);
        ItemStack bowOP = new ItemStack(Items.BOW);
        ItemStack signLol = new ItemStack(Items.OAK_SIGN);

        CreativeModeTab.ItemDisplayParameters params = event.getParameters();
        params.holders().lookup(Registries.ENCHANTMENT).ifPresent(en -> {
            DataComponentPatch opSword = DataComponentPatch.builder()
                    .set(DataComponents.ITEM_NAME,Component.literal("OP Test Sword"))
                    .set(DataComponents.TOOLTIP_STYLE,ResourceLocation.parse("overpoweredmastery:om_epic"))
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
                    .set(DataComponents.TOOLTIP_STYLE,ResourceLocation.parse("overpoweredmastery:om_epic"))
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
                    .set(DataComponents.TOOLTIP_STYLE,ResourceLocation.parse("overpoweredmastery:om_epic"))
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
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == ALL_TAB.getKey()){
            customItems(event);
            event.accept(OMItems.BONE_SWORD);
            event.accept(OMItems.ENDARKENED_CROSSBOW);
            event.accept(OMItems.PENULTIMATE_SWORD_DARK);
            event.accept(OMItems.PENULTIMATE_SWORD_LIGHT);
            event.accept(OMItems.RED_WUBS);
            event.accept(OMItems.CHICKEN_WUBS);
            event.accept(OMItems.GREEN_WUBS);
            event.accept(OMItems.PURPLE_WUBS);
            event.accept(OMItems.MOVING_PROBABLE_BLOCK_ITEM);
        }
    }
}
