package com.github.merchantpug.bella.mixin;

import com.github.merchantpug.bella.Bella;
import com.github.merchantpug.bella.access.LivingEntityAccess;
import com.github.merchantpug.bella.networking.BellaPackets;
import com.github.merchantpug.bella.registry.BellaComponents;
import com.github.merchantpug.bella.registry.BellaGameEvents;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
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
	private float bella$prevPrevYaw;

	@Shadow
	public abstract float getYaw(float tickDelta);

	public LivingEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "dropLoot", at = @At(value = "HEAD"))
	private void bella$dropBellOnDeath(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		if ((LivingEntity)(Object)this instanceof AnimalEntity && BellaComponents.BELL_COMPONENT.isProvidedBy(this) && BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			if (BellaComponents.BELL_COMPONENT.get(this).isStrung()) {
				this.dropStack(Items.STRING.getDefaultStack());
			}
			this.dropStack(Items.BELL.getDefaultStack());
		}
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void bella$tickBell(CallbackInfo ci) {
		if (!this.world.isClient) {
			if (BellaComponents.BELL_COMPONENT.isProvidedBy(this) && BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
				bella$bellVelocity -= 0.05F * bella$bellPosition + 0.01F * bella$bellVelocity - ((((this.getYaw() + this.bella$prevPrevYaw - 2 * prevYaw + 180.0 + 360) % 360) - 180.0) * 0.02);
				bella$bellPosition += bella$bellVelocity;

				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				buf.writeInt(this.getId());
				buf.writeFloat(bella$bellPosition);
				ServerPlayNetworking.send(((ServerWorld)this.world).getPlayers(), BellaPackets.SYNC_BELL_POSITION, buf);

				if ((this.bella$bellPosition - this.bella$prevBellPosition) * (this.bella$prevBellPosition - this.bella$prevPrevBellPosition) < 0.0F && !BellaComponents.BELL_COMPONENT.get(this).isStrung()) {
					this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_BELL_USE, this.getSoundCategory(), (float)(Math.abs(bella$bellPosition * Math.PI / 180.0F) / 4.0), this.random.nextFloat() * 0.4F + 1.0F);
					this.emitGameEvent(BellaGameEvents.ENTITY_BELL_RING);
				}

				this.bella$prevPrevBellPosition = this.bella$prevBellPosition;
				this.bella$prevBellPosition = this.bella$bellPosition;
			}
			this.bella$prevPrevYaw = this.prevYaw;
		}

		if (BellaComponents.BELL_COMPONENT.isProvidedBy(this) && !BellaComponents.BELL_COMPONENT.get(this).hasBell()) {
			if (!this.world.isClient && this.bella$prevPrevYaw != 0.0F || this.bella$prevBellPosition != 0.0F || this.bella$prevPrevBellPosition != 0.0F) {
				this.bella$prevBellPosition = 0.0F;
				this.bella$prevPrevBellPosition = 0.0F;
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
