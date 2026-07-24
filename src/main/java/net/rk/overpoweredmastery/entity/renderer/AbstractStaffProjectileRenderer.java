package net.rk.overpoweredmastery.entity.renderer;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.rk.overpoweredmastery.entity.custom.AbstractStaffProjectile;

public class AbstractStaffProjectileRenderer extends EntityRenderer<AbstractStaffProjectile, EntityRenderState> {
    public AbstractStaffProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldShowName(AbstractStaffProjectile entity, double distanceToCameraSq) {
        return false;
    }

    @Override
    public boolean shouldRender(AbstractStaffProjectile livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
