package net.rk.overpoweredmastery.entity.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.blockentity.MultiAssemblerBlockEntity;

public class MultiAssemblerModel extends Model{
    public static final ModelLayerLocation MULTI_ASSEMBLER_MODEL_LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"textures/entity/multi_assembler.png"), "main");

    private final ModelPart main;
    private final ModelPart westjoint;
    private final ModelPart westarm;
    private final ModelPart westtool;
    private final ModelPart northjoint;
    private final ModelPart northarm;
    private final ModelPart northtool;
    private final ModelPart southjoint;
    private final ModelPart southarm;
    private final ModelPart southtool;
    private final ModelPart southtoolbuffer;
    private final ModelPart eastjoint;
    private final ModelPart eastarm;
    private final ModelPart easttool;

    public MultiAssemblerModel(ModelPart root) {
        super(root, RenderType::entityCutout);
        this.main = root.getChild("main");
        this.westjoint = this.main.getChild("westjoint");
        this.westarm = this.westjoint.getChild("westarm");
        this.westtool = this.westarm.getChild("westtool");
        this.northjoint = this.main.getChild("northjoint");
        this.northarm = this.northjoint.getChild("northarm");
        this.northtool = this.northarm.getChild("northtool");
        this.southjoint = this.main.getChild("southjoint");
        this.southarm = this.southjoint.getChild("southarm");
        this.southtool = this.southarm.getChild("southtool");
        this.southtoolbuffer = this.southtool.getChild("southtoolbuffer");
        this.eastjoint = this.main.getChild("eastjoint");
        this.eastarm = this.eastjoint.getChild("eastarm");
        this.easttool = this.eastarm.getChild("easttool");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.1F, -8.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(50, 63).addBox(10.0F, -5.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 45).addBox(8.0F, -7.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(60, 34).addBox(8.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(58, 63).addBox(-12.0F, -5.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition topassemblytable_r1 = main.addOrReplaceChild("topassemblytable_r1", CubeListBuilder.create().texOffs(0, 36).addBox(-3.5F, -0.5F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bottomeastpipe_r1 = main.addOrReplaceChild("bottomeastpipe_r1", CubeListBuilder.create().texOffs(60, 38).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(60, 49).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, -1.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition southwestcorner_r1 = main.addOrReplaceChild("southwestcorner_r1", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -5.5F, -1.0F, 4.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -5.5F, 7.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition southeastcorner_r1 = main.addOrReplaceChild("southeastcorner_r1", CubeListBuilder.create().texOffs(40, 37).addBox(-2.0F, -5.5F, -1.0F, 4.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -5.5F, 7.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition northeastcorner_r1 = main.addOrReplaceChild("northeastcorner_r1", CubeListBuilder.create().texOffs(40, 24).addBox(-2.0F, -5.5F, -1.0F, 4.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -5.5F, -7.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition northwestcorner_r1 = main.addOrReplaceChild("northwestcorner_r1", CubeListBuilder.create().texOffs(28, 36).addBox(-2.0F, -5.5F, -1.0F, 4.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -5.5F, -7.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition westjoint = main.addOrReplaceChild("westjoint", CubeListBuilder.create().texOffs(12, 44).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -8.5F, 0.0F));

        PartDefinition westarm = westjoint.addOrReplaceChild("westarm", CubeListBuilder.create().texOffs(52, 34).addBox(1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition westtool = westarm.addOrReplaceChild("westtool", CubeListBuilder.create().texOffs(18, 58).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -9.0F, 0.0F));

        PartDefinition northjoint = main.addOrReplaceChild("northjoint", CubeListBuilder.create().texOffs(28, 50).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, -8.0F));

        PartDefinition northarm = northjoint.addOrReplaceChild("northarm", CubeListBuilder.create().texOffs(0, 57).addBox(-1.0F, -9.0F, -3.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition northtool = northarm.addOrReplaceChild("northtool", CubeListBuilder.create().texOffs(44, 57).addBox(-2.0F, -5.0F, -0.5F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, -2.0F));

        PartDefinition southjoint = main.addOrReplaceChild("southjoint", CubeListBuilder.create().texOffs(44, 50).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 8.0F));

        PartDefinition southarm = southjoint.addOrReplaceChild("southarm", CubeListBuilder.create().texOffs(28, 57).addBox(-1.0F, -9.0F, 1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition southtool = southarm.addOrReplaceChild("southtool", CubeListBuilder.create().texOffs(60, 53).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 2.0F));

        PartDefinition southtoolbuffer = southtool.addOrReplaceChild("southtoolbuffer", CubeListBuilder.create().texOffs(52, 24).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition eastjoint = main.addOrReplaceChild("eastjoint", CubeListBuilder.create().texOffs(12, 51).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -8.5F, 0.0F));

        PartDefinition eastarm = eastjoint.addOrReplaceChild("eastarm", CubeListBuilder.create().texOffs(36, 57).addBox(-3.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition easttool = eastarm.addOrReplaceChild("easttool", CubeListBuilder.create().texOffs(60, 42).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 29).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(54, 57).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 58).addBox(-2.0F, -6.0F, 1.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 58).addBox(1.0F, -6.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 63).addBox(-2.0F, -6.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -9.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 72, 72);
    }

    public void setupAnim(MultiAssemblerBlockEntity be){
        float f = Mth.sin(be.ticksPassed / 82.0f);
        float f2 = Mth.sin(be.ticksPassed / 12.0f);
        float f3 = Mth.sin(be.ticksPassed / 8.0f);
        float f4 = Mth.sin(be.ticksPassed / 2.0f);
        float f5 = Mth.sin(be.ticksPassed / 5.0f);
        main.xRot = 3.1415f;
        main.y = -1.5f;
        southtool.yRot = 0.0f;
        southtool.xRot = Mth.clamp(f,0.0f,0.75f);
        southtoolbuffer.yRot = be.ticksPassed / 2.0f;
        southjoint.xRot = Mth.clamp(f2,0.0f,0.5f);
        southarm.xRot = Mth.clamp(f,0.0f,1.2f);
        southjoint.zRot = Mth.clamp(f,-0.11f,0.11f);
        northtool.yRot = f5 / 0.4f;
        northtool.zRot = Mth.clamp(f,-0.2f,0.3f);
        northjoint.xRot = Mth.clamp(-1.0f + f2,-1.2f,0.75f);
        northarm.zRot = Mth.clamp(f5,-0.5f,0.5f);
        easttool.yRot = Mth.clamp(f5,-1.0f,1.0f);
        eastarm.zRot = Mth.clamp(f,0.0f,0.2f);
        westarm.zRot = Mth.clamp(f2,-0.1f,0.7f) * -1;
        easttool.zRot = 1.45f - Mth.clamp(f3,-0.6f,1.1f);
        westtool.zRot = -1.65f - Mth.clamp(f4,-0.5f,1.1f);
        westjoint.zRot = Mth.clamp(f,0.0f,0.2f) * -1;
        eastjoint.zRot = Mth.clamp(f,0.0f,0.2f);
    }

    public void setupAnimOff(MultiAssemblerBlockEntity be){
        main.xRot = 3.1415f;
        main.y = -1.5f;
        southtool.yRot = be.ticksPassed / 4.0f;
        southtool.xRot = 0.0f;
        southtoolbuffer.yRot = 0.0f;
        southjoint.xRot = 0.0f;
        southarm.xRot = 0.0f;
        southjoint.zRot = 0.0f;
        northtool.yRot = 0.0f;
        northtool.zRot = 0.0f;
        northjoint.xRot = 0.0f;
        northarm.zRot = 0.0f;
        easttool.yRot = 0.0f;
        eastarm.zRot = 0.0f;
        westarm.zRot = 0.0f;
        easttool.zRot = 1.45f;
        westtool.zRot = -1.65f;
        westjoint.zRot = 0.0f;
        eastjoint.zRot = 0.0f;
    }
}
