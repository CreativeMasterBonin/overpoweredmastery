package net.rk.overpoweredmastery.resource;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.Mod;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.item.OMItems;

public class GreenWubGunTickableSound extends AbstractTickableSoundInstance {
    public Player player;

    public GreenWubGunTickableSound(Player player, RandomSource randomSource) {
        super(OMSoundEvents.GREEN_WUBS.get(), SoundSource.PLAYERS, randomSource);
        this.player = player;
        this.looping = true;
        this.delay = 0;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Override
    public boolean canPlaySound() {
        return !this.player.isScoping() && !this.player.isSecondaryUseActive();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public void stopSound(){
        this.stop();
    }

    int count = 0;

    @Override
    public void tick() {
        count++;
        if(this.player.getItemInHand(player.getUsedItemHand()).is(OMItems.GREEN_WUBS)){
            if(!this.player.isUsingItem()){
                if(this.volume > 0.0f){
                    this.volume -= 0.1f;
                }
                this.x = player.getX();
                this.y = player.getY();
                this.z = player.getZ();
            }
            else if(this.player.isUsingItem()){
                if(this.player.level().tickRateManager().runsNormally()){
                    this.pitch = 1.0f;
                    this.volume = Mth.lerp(count,0.0f,1.0f);
                    this.x = player.getX();
                    this.y = player.getY();
                    this.z = player.getZ();
                }
                else{
                    this.pitch = 0;
                    this.volume = 0;
                }
            }
        }
        else{
            this.stop();
        }
        if(count > 32767){
            count = 0;
        }
    }
}
