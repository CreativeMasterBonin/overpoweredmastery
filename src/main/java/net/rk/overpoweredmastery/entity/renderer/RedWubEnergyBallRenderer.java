package net.rk.overpoweredmastery.entity.renderer;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.rk.overpoweredmastery.entity.custom.RedWubEnergyBall;

public class RedWubEnergyBallRenderer extends EntityRenderer<RedWubEnergyBall, EntityRenderState>{
    public RedWubEnergyBallRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(RedWubEnergyBall livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
