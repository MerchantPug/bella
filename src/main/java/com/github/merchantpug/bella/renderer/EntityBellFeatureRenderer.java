package com.github.merchantpug.bella.renderer;

import com.github.merchantpug.bella.Bella;
import com.github.merchantpug.bella.access.LivingEntityAccess;
import com.github.merchantpug.bella.mixin.client.BeeEntityModelAccessor;
import com.github.merchantpug.bella.mixin.client.AnimalModelAccessor;
import com.github.merchantpug.bella.mixin.client.ModelPartAccessor;
import com.github.merchantpug.bella.registry.BellaComponents;
import com.github.merchantpug.bella.registry.BellaTags;
import com.github.merchantpug.bella.api.BellRenderModifiers;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import java.util.List;

public class EntityBellFeatureRenderer<T extends LivingEntity, M extends AnimalModel<T>> extends FeatureRenderer<T, M> {
	private static final Identifier SKIN = Bella.identifier("textures/entity/bell/entity_bell.png");
	private final EntityBellModel<AnimalEntity> model;
	private float xTransform = Float.NaN;
	private float yTransform = Float.NaN;
	private float zTransform = Float.NaN;
	private float scale = Float.NaN;
	private ModelPart referenceModelPart = null;
	private boolean hasAppliedModifiers;
	private boolean shouldMoveWithPart;

	public EntityBellFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, EntityModelLoader entityModelLoader) {
		super(featureRendererContext);
		this.model = new EntityBellModel<>(entityModelLoader.getModelPart(EntityBellModel.LAYER_LOCATION));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (!BellaComponents.BELL_COMPONENT.get(entity).hasBell() || entity.getType().isIn(BellaTags.BLACKLIST)) return;
		matrices.push();
		boolean hasApplied = false;
		List<BellRenderModifiers> modifiers = Bella.getBellRenderModifiers(entity);
		if (Float.isNaN(this.xTransform) || Float.isNaN(this.yTransform) || Float.isNaN(this.scale) || Float.isNaN(this.zTransform)) {
			float xScale = 0.0F;
			float maxX = 0.0F;
			float yScale = 0.0F;
			float minY = 0.0F;
			float zScale = 0.0F;
			float maxZ = 0.0F;
			ModelPart referenceModelPart = null;

			if (this.getContextModel() instanceof BeeEntityModel<?> beeModel) {
				ModelPart part = ((BeeEntityModelAccessor)beeModel).bella$getBone().getChild("body");
				ModelPart.Cuboid cuboid = ((ModelPartAccessor) (Object) part).bella$getCuboids().get(0);
				xScale = cuboid.maxX - cuboid.minX;
				maxX = cuboid.maxX;
				yScale = cuboid.maxY - cuboid.minY;
				minY = cuboid.maxY;
				zScale = cuboid.maxZ - cuboid.minZ;
				maxZ = cuboid.maxZ;
				referenceModelPart = part;

				hasApplied = true;
			} if (modifiers.stream().anyMatch(modifier -> modifier.override)) {
				for (BellRenderModifiers modifier : modifiers) {
					xTransform += modifier.x;
					yTransform += modifier.y;
					zTransform += modifier.z;
					if (!Float.isNaN(modifier.headScaleX)) {
						xScale = modifier.headScaleX;
					}
					if (!Float.isNaN(modifier.headScaleY)) {
						yScale = modifier.headScaleY;
					}
					if (!Float.isNaN(modifier.headScaleZ)) {
						zScale = modifier.headScaleZ;
					}
				}

				hasApplied = true;
			} else {
				for (ModelPart part : ((AnimalModelAccessor) this.getContextModel()).bella$getHeadParts()) {
					for (int i = 0; i < ((ModelPartAccessor) (Object) part).bella$getCuboids().size(); ++i) {
						ModelPart.Cuboid cuboid = ((ModelPartAccessor) (Object) part).bella$getCuboids().get(i);
						float yCuboidScale = cuboid.maxY - cuboid.minY;
						if (yScale < yCuboidScale) {
							xScale = cuboid.maxX - cuboid.minX;
							maxX = cuboid.maxX;
							yScale = yCuboidScale;
							minY = cuboid.maxY;
							zScale = cuboid.maxZ - cuboid.minZ;
							maxZ = cuboid.maxZ;
							referenceModelPart = part;

							hasApplied = true;
						}
					}
					for (ModelPart partChild : ((ModelPartAccessor) (Object) part).bella$getChildren().values()) {
						for (int i = 0; i < ((ModelPartAccessor) (Object) partChild).bella$getCuboids().size(); ++i) {
							ModelPart.Cuboid cuboid = ((ModelPartAccessor) (Object) partChild).bella$getCuboids().get(i);
							float yCuboidScale = cuboid.maxY - cuboid.minY;
							if (yScale < yCuboidScale) {
								yScale = yCuboidScale;
								minY = cuboid.maxY;
								xScale = cuboid.maxX - cuboid.minX;
								maxX = cuboid.maxX;
								zScale = cuboid.maxZ - cuboid.minZ;
								maxZ = cuboid.maxZ;
								referenceModelPart = part;

								hasApplied = true;
							}
						}
					}
				}
			}

			if (hasApplied && Float.isNaN(this.xTransform) && Float.isNaN(this.yTransform) && Float.isNaN(this.zTransform) && Float.isNaN(this.scale)) {
				this.xTransform = maxX - (xScale / 2);
				this.yTransform = minY;
				this.zTransform = maxZ - (zScale / 4);
				this.scale = yScale;
				this.referenceModelPart = referenceModelPart;
			} else {
				Bella.LOGGER.warn("No bell model transforms were found for entity type: " + entity.getType().getName() + ".");
			}
		}

		if (referenceModelPart != null && !hasAppliedModifiers) {
			float xTransform = this.xTransform;
			float yTransform = this.yTransform;
			float zTransform = this.zTransform;
			float scale = this.scale / 8.0F;

			if (!modifiers.isEmpty()) {
				for (BellRenderModifiers modifier : modifiers) {
					xTransform += modifier.x;
					yTransform += modifier.y;
					zTransform += modifier.z;
					if (!Float.isNaN(modifier.headScaleX)) {
						scale = modifier.headScaleX;
					}
					if (!Float.isNaN(modifier.headScaleY)) {
						scale = modifier.headScaleY;
					}
					if (!Float.isNaN(modifier.headScaleZ)) {
						scale = modifier.headScaleZ;
					}
				}
			}

			this.xTransform = xTransform;
			this.yTransform = yTransform;
			this.zTransform = zTransform;
			this.scale = scale;
			this.shouldMoveWithPart = modifiers.stream().anyMatch(modifier -> modifier.shouldMoveWithPart);
			this.hasAppliedModifiers = true;
		}

		float pivotX = referenceModelPart != null ? referenceModelPart.pivotX : 0.0F;
		float pivotY = referenceModelPart != null ? referenceModelPart.pivotY : 0.0F;
		float pivotZ = referenceModelPart != null ? referenceModelPart.pivotZ : 0.0F;
		matrices.translate((pivotX + xTransform) / 16.0F, (pivotY + yTransform) / 16.0F, (pivotZ + zTransform) / 16.0F);
		this.model.bellBody.scaleX = scale;
		this.model.bellBody.scaleY = scale;
		this.model.bellBody.scaleZ = scale;
		if (this.getContextModel().child) {
			matrices.translate(0.0F, ((AnimalModelAccessor)this.getContextModel()).bella$getChildHeadYOffset() / 16.0F, ((AnimalModelAccessor)this.getContextModel()).bella$getChildHeadZOffset() / 16.0F);
			this.model.bellBody.scaleX *= 0.6F;
			this.model.bellBody.scaleY *= 0.6F;
			this.model.bellBody.scaleZ *= 0.6F;
		}
		if (this.shouldMoveWithPart && this.referenceModelPart != null) {
			referenceModelPart.rotate(matrices);
		}

		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((((LivingEntityAccess)entity).bella$getBellRotation())));

		this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(SKIN)), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

		matrices.pop();
	}
}
