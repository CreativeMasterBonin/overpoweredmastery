package net.rk.overpoweredmastery.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;

public class OMMenus{
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, OverpoweredMastery.MODID);

    public static final DeferredHolder<MenuType<?>,MenuType<MultiAssemblerMenu>> MULTI_ASSEMBLER_MENU = register(
            "multi_assembler",MultiAssemblerMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return MENU_TYPES.register(name, () -> IMenuTypeExtension.create(factory));
    }
}
