package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.OMEntityTypes;

import java.util.concurrent.CompletableFuture;

public class OMEntityTag extends EntityTypeTagsProvider {
    public OMEntityTag(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, OverpoweredMastery.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES)
                .add(OMEntityTypes.CHICKEN_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.RED_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.GREEN_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.PURPLE_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.TRIAL_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.NETHER_WUB_ENERGY_BALL.get())
        ;
        this.tag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)
                .add(OMEntityTypes.CHICKEN_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.RED_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.GREEN_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.PURPLE_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.TRIAL_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.NETHER_WUB_ENERGY_BALL.get())
        ;
        this.tag(OMTags.ULTRA_SEEK_IMMUNE)
                .addTag(Tags.EntityTypes.BOATS)
                .addTag(Tags.EntityTypes.BOSSES)
                .addTag(Tags.EntityTypes.MINECARTS)
                .addTag(EntityTypeTags.BOAT)
                .add(EntityType.GIANT)
                .addTag(EntityTypeTags.ARROWS)
                .add(OMEntityTypes.CHICKEN_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.RED_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.GREEN_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.PURPLE_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.TRIAL_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.NETHER_WUB_ENERGY_BALL.get())
                .add(EntityType.EXPERIENCE_BOTTLE)
                .add(EntityType.ITEM_FRAME)
                .add(EntityType.GLOW_ITEM_FRAME)
                .add(EntityType.LEASH_KNOT)
                .add(EntityType.ITEM)
                .add(EntityType.BLOCK_DISPLAY)
                .add(EntityType.TEXT_DISPLAY)
                .add(EntityType.ITEM_DISPLAY)
                .add(EntityType.MARKER)
                .add(EntityType.MANNEQUIN)
                .add(EntityType.VILLAGER)
                .add(EntityType.IRON_GOLEM)
                .add(EntityType.EXPERIENCE_ORB)
                .add(EntityType.AREA_EFFECT_CLOUD)
                .add(EntityType.ALLAY)
                .addTag(EntityTypeTags.CANDIDATE_FOR_IRON_GOLEM_GIFT)
                .addTag(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS)
                .add(EntityType.ENDER_PEARL)
                .add(EntityType.EYE_OF_ENDER)
                .add(EntityType.WITHER_SKULL)
                .add(EntityType.DRAGON_FIREBALL)
                .add(EntityType.FIREWORK_ROCKET)
                .add(EntityType.SMALL_FIREBALL)
                .add(EntityType.FIREBALL)
        ;
    }
}
