package net.rk.overpoweredmastery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.item.custom.*;

import java.util.List;
import java.util.function.Consumer;

public class OMItems{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OverpoweredMastery.MODID);

    public static final DeferredItem<Item> ENDARKENED_CROSSBOW = ITEMS.registerItem("endarkened_crossbow",
            EndarkenedCrossbow::new,new Item.Properties().setId(makeResourceKey("endarkened_crossbow"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("om_epic")));

    public static final DeferredItem<Item> RED_WUBS = ITEMS.registerItem("red_wubs",
            RedWubs::new,new Item.Properties().setId(makeResourceKey("red_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("red_wub")));

    public static final DeferredItem<Item> GREEN_WUBS = ITEMS.registerItem("green_wubs",
            GreenWubs::new,new Item.Properties().setId(makeResourceKey("green_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("green_wub")));

    public static final DeferredItem<Item> PURPLE_WUBS = ITEMS.registerItem("purple_wubs",
            PurpleWubs::new,new Item.Properties().setId(makeResourceKey("purple_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("purple_wub")));

    /*public static final DeferredItem<Item> MULTIPURPOSE_VEHICLE = ITEMS.registerItem("multipurpose_vehicle",
            MultipurposeVehicleItem::new,new Item.Properties().setId(makeResourceKey("multipurpose_vehicle"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("om_epic")));*/

    public static final DeferredItem<Item> CHICKEN_WUBS = ITEMS.registerItem("chicken_wubs",
            ChickenWubs::new,new Item.Properties().setId(makeResourceKey("chicken_wubs"))
                    .component(DataComponents.TOOLTIP_STYLE,
                            makeTooltipReference("red_wub")));

    public static final DeferredItem<Item> MOVING_PROBABLE_BLOCK_ITEM = ITEMS.registerItem("moving_probable_block",
            MovingProbableBlockItem::new,
                    new Item.Properties().setId(makeResourceKey("moving_probable_block"))
                            .component(DataComponents.TOOLTIP_STYLE,
                                    makeTooltipReference("om_epic_blue")));


    public static ResourceKey<Item> makeResourceKey(String name){
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }

    public static ResourceLocation makeTooltipReference(String tooltipName){
        return ResourceLocation.parse(OverpoweredMastery.MODID + ":" + tooltipName);
    }
}
