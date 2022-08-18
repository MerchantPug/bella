package com.github.merchantpug.bella.mixin.client;

import com.github.merchantpug.bella.access.AnimalEntityAccess;
import com.github.merchantpug.bella.networking.BellaPackets;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixinClient extends Entity {
	public LivingEntityMixinClient(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "onSpawnPacket", at = @At("TAIL"))
	private void bella$setAnimalModel(EntitySpawnS2CPacket packet, CallbackInfo ci) {
		if ((LivingEntity)(Object)this instanceof AnimalEntity && world.isClient && !((AnimalEntityAccess)this).bella$hasAnimalModel()) {
			EntityRenderer<?> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(this);
			if (renderer instanceof LivingEntityRenderer<?,?> livingEntityRenderer && livingEntityRenderer.getModel() instanceof AnimalModel<?>) {
				((AnimalEntityAccess)this).bella$setHasAnimalModel(true);
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				buf.writeInt(packet.getId());
				ClientPlayNetworking.send(BellaPackets.SET_ANIMAL_MODEL, buf);
			}
		}
	}
}
