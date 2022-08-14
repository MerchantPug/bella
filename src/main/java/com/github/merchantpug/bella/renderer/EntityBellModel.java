package com.github.merchantpug.bella.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class EntityBellModel<T extends Entity> extends EntityModel<T> {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(new Identifier("bella", "entity_bell"), "main");
	public final ModelPart bellBody;

	public EntityBellModel(ModelPart root) {
		this.bellBody = root.getChild("entity_bell_body");
	}

	public static TexturedModelData createModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData bellBody = modelPartData.addChild("entity_bell_body", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		bellBody.addChild("entity_bell_base", ModelPartBuilder.create().uv(0, 9).cuboid(-3.0F, 4.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		return TexturedModelData.of(modelData, 32, 16);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		bellBody.render(matrices, vertices, light, overlay);
	}
}
