package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.access.LivingEntityAccess;
import com.github.merchantpug.bella.networking.BellaPackets;
import com.github.merchantpug.bella.registry.BellaComponents;
import com.github.merchantpug.bella.registry.BellaGameEvents;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityAccess {
	@Unique
	private float bella$bellVelocity;
	@Unique
	private float bella$bellPosition;
	@Unique
	private float bella$prevBellPosition;
	@Unique
	private float bella$prevPrevBellPosition;
	@Unique
	private float bella$prevPrevHeadYaw;

	@Shadow
	public float headYaw;

	@Shadow
	public float prevHeadYaw;

	public LivingEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "dropLoot", at = @At(value = "HEAD"))
	private void bella$dropBellOnDeath(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if ((LivingEntity)(Object)this instanceof AnimalEntity && BellaComponents.BELL_COMPONENT.isProvidedBy(this) && BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			this.dropStack(Items.BELL.getDefaultStack());
		}
	}

	@Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tickStatusEffects()V"))
	private void bella$tickBell(CallbackInfo ci) {
		if (!this.world.isClient) {
			if (((LivingEntity)(Object)this instanceof AnimalEntity) && BellaComponents.BELL_COMPONENT.isProvidedBy(this)) {
				if (BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
					bella$bellVelocity -= 0.05F * bella$bellPosition + 0.02F * bella$bellVelocity - ((((headYaw + bella$prevPrevHeadYaw - 2 * prevHeadYaw + 180.0) % 360) - 180.0) * 0.04);
					bella$bellPosition += bella$bellVelocity;

					PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
					buf.writeInt(this.getId());
					buf.writeFloat(bella$bellPosition);
					ServerPlayNetworking.send(((ServerWorld)this.world).getPlayers(), BellaPackets.SYNC_BELL_POSITION, buf);

					if ((this.bella$bellPosition - this.bella$prevBellPosition) * (this.bella$prevBellPosition - this.bella$prevPrevBellPosition) < 0.0F) {
						this.playSound(SoundEvents.BLOCK_BELL_USE, (float) Math.abs(bella$bellPosition / 8.0 * Math.PI / 180.0F), this.random.nextFloat() * 0.4F + 1.0F);
						this.emitGameEvent(BellaGameEvents.ENTITY_BELL_RING);
					}

					this.bella$prevPrevHeadYaw = this.prevHeadYaw;

					this.bella$prevPrevBellPosition = this.bella$prevBellPosition;
					this.bella$prevBellPosition = this.bella$bellPosition;
				}
			}
		}

		if (BellaComponents.BELL_COMPONENT.isProvidedBy(this) && !BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			if (!this.world.isClient && this.bella$prevPrevHeadYaw != 0.0F || this.bella$prevBellPosition != 0.0F || this.bella$prevPrevBellPosition != 0.0F) {
				this.bella$prevBellPosition = 0.0F;
				this.bella$prevPrevBellPosition = 0.0F;
				this.bella$prevPrevHeadYaw = 0.0F;
			}
			if (this.bella$bellPosition != 0.0F) {
				this.bella$bellPosition = 0.0F;
			}
		}
	}

	@Override
	public float bella$getBellRotation() {
		return this.bella$bellPosition;
	}

	@Override
	public void bella$setBellRotation(float value) {
		this.bella$bellPosition = value;
	}
}
