package net.rk.overpoweredmastery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.rk.overpoweredmastery.OverpoweredMasteryClient;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.util.ClientActionHandler;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.function.Consumer;

public class UltraSword extends Item {
    public UltraSword(Properties properties) {
        super(properties.stacksTo(1).durability(99999).repairable(OMItems.ULTRA_INGOT.asItem())
                .rarity(OMRarity.ULTRA.getValue()));
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(OMTags.ULTRA_SWORD_SUPPORTED);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.ultra.desc")
                .withColor(OPUtil.ULTRA_COLOR));
        if(ClientActionHandler.keyMappingPressed(OverpoweredMasteryClient.DESCRIPTION_KEY_MAPPING)){
            tooltipAdder.accept(Component.translatable("item.overpoweredmastery.ultra.desc.detail")
                    .withStyle(ChatFormatting.GRAY));
        }
        else{
            tooltipAdder.accept(Component.translatable("item.overpoweredmastery.press_desc_key",Component.translatable(OverpoweredMasteryClient.DESCRIPTION_KEY_MAPPING.getKey().getName()))
                    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}
