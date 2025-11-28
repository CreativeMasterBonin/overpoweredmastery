package net.rk.overpoweredmastery.mix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.item.Item;
import net.rk.overpoweredmastery.item.custom.*;
import net.rk.overpoweredmastery.resource.ChickenWubGunTickableSound;
import net.rk.overpoweredmastery.resource.GreenWubGunTickableSound;
import net.rk.overpoweredmastery.resource.PurpleWubGunTickableSound;
import net.rk.overpoweredmastery.resource.RedWubGunTickableSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LocalPlayer.class)
public class OMMix {
    // this class is a mixin and adds some functionality to the end of some LocalPlayer methods (to prevent mixups with other mods)
    @Shadow public ClientInput input;
    public LocalPlayer localPlayer; // our personal reference to the localplayer itself
    public boolean alreadyPlayingChickenWubs = false;
    public boolean alreadyPlayingRedWubs = false;
    public boolean alreadyPlayingGreenWubs = false;
    public boolean alreadyPlayingPurpleWubs = false;
    public ChickenWubGunTickableSound chickenWubs;
    public GreenWubGunTickableSound greenWubs;
    public PurpleWubGunTickableSound purpleWubs;
    public RedWubGunTickableSound redWubs;

    public boolean caughtExceptionOnce = false; // log mixin errors only once as any should be counted as severe

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci){
        try{
            if(Minecraft.getInstance().player.hasClientLoaded()){
                localPlayer = Minecraft.getInstance().player;
                if(Minecraft.getInstance() != null){
                    // reset the player's sound instances if crouching or the player stops using any item
                    if(Minecraft.getInstance().player.isCrouching() || !(Minecraft.getInstance().player.isUsingItem())){
                        if(alreadyPlayingChickenWubs){
                            chickenWubs.stopSound();
                            chickenWubs = null;
                            alreadyPlayingChickenWubs = false;
                        }
                        if(alreadyPlayingRedWubs){
                            redWubs.stopSound();
                            redWubs = null;
                            alreadyPlayingRedWubs = false;
                        }
                        if(alreadyPlayingGreenWubs){
                            greenWubs.stopSound();
                            greenWubs = null;
                            alreadyPlayingGreenWubs = false;
                        }
                        if(alreadyPlayingPurpleWubs){
                            purpleWubs.stopSound();
                            purpleWubs = null;
                            alreadyPlayingPurpleWubs = false;
                        }
                    }
                }
            }
        }
        catch (Exception e){
            if(!caughtExceptionOnce){
                //Logger.getAnonymousLogger().info("OP Mod - OMMixin - tick - An exception was caught. Only logging first exception!");
                //Logger.getAnonymousLogger().warning(e.getMessage());
                caughtExceptionOnce = true;
            }
        }
    }

    @Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
    public void onSyncedDataUpdated(EntityDataAccessor<?> key, CallbackInfo ci){
        try {
            if(Minecraft.getInstance().player.hasClientLoaded()){
                localPlayer = Minecraft.getInstance().player;
                // reset all wubs when crouching, or continue to play the sound if not crouching from last point
                if (Minecraft.getInstance().player.getUseItem().getItem() instanceof AbstractWubs) {
                    Item item = Minecraft.getInstance().player.getUseItem().getItem();
                    if (Minecraft.getInstance().player.isUsingItem()) {
                        // chicken wubs
                        if (item instanceof ChickenWubs) {
                            if (!alreadyPlayingChickenWubs) {
                                chickenWubs = null;
                                chickenWubs = new ChickenWubGunTickableSound(localPlayer, localPlayer.getRandom());
                                Minecraft.getInstance().getSoundManager().playDelayed(chickenWubs, 20);
                                alreadyPlayingChickenWubs = true;
                            }
                        }
                        // red wubs
                        if (item instanceof RedWubs) {
                            if (!alreadyPlayingRedWubs) {
                                redWubs = null;
                                redWubs = new RedWubGunTickableSound(localPlayer, localPlayer.getRandom());
                                Minecraft.getInstance().getSoundManager().playDelayed(redWubs, 20);
                                alreadyPlayingRedWubs = true;
                            }
                        }
                        // green wubs
                        if (item instanceof GreenWubs) {
                            if (!alreadyPlayingGreenWubs) {
                                greenWubs = null;
                                greenWubs = new GreenWubGunTickableSound(localPlayer, localPlayer.getRandom());
                                Minecraft.getInstance().getSoundManager().playDelayed(greenWubs, 20);
                                alreadyPlayingGreenWubs = true;
                            }
                        }
                        // purple wubs
                        if (item instanceof PurpleWubs) {
                            if (!alreadyPlayingPurpleWubs) {
                                purpleWubs = null;
                                purpleWubs = new PurpleWubGunTickableSound(localPlayer, localPlayer.getRandom());
                                Minecraft.getInstance().getSoundManager().playDelayed(purpleWubs, 20);
                                alreadyPlayingPurpleWubs = true;
                            }
                        }
                    } else {
                        if (alreadyPlayingChickenWubs) {
                            chickenWubs.stopSound();
                            alreadyPlayingChickenWubs = false;
                        }
                        if (alreadyPlayingPurpleWubs) {
                            purpleWubs.stopSound();
                            alreadyPlayingPurpleWubs = false;
                        }
                        if (alreadyPlayingRedWubs) {
                            redWubs.stopSound();
                            alreadyPlayingRedWubs = false;
                        }
                        if (alreadyPlayingGreenWubs) {
                            greenWubs.stopSound();
                            alreadyPlayingGreenWubs = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            if(!caughtExceptionOnce){
                //Logger.getAnonymousLogger().info("OP Mod - OMMixin - syncDataUpdate - An exception was caught. Only logging first exception!");
                //Logger.getAnonymousLogger().warning(e.getMessage());
                caughtExceptionOnce = true;
            }
        }
    }
}
