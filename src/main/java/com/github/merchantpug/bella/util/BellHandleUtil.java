package com.github.merchantpug.bella.util;

import com.github.merchantpug.bella.access.AnimalEntityAccess;
import com.github.merchantpug.bella.networking.BellaPackets;
import com.github.merchantpug.bella.registry.BellaComponents;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class BellHandleUtil {
	public static ActionResult addBellToEntity(AnimalEntity animalEntity, PlayerEntity player, ItemStack stack) {
		if (player.world.isClient && !((AnimalEntityAccess)animalEntity).bella$hasAnimalModel()) {
			EntityRenderer<?> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(animalEntity);
			if (renderer instanceof LivingEntityRenderer<?,?> livingEntityRenderer && livingEntityRenderer.getModel() instanceof AnimalModel<?>) {
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				buf.writeInt(animalEntity.getId());
				ClientPlayNetworking.send(BellaPackets.SET_ANIMAL_MODEL, buf);
				((AnimalEntityAccess)animalEntity).bella$setHasAnimalModel(true);
			}
		}
		if (((AnimalEntityAccess)animalEntity).bella$hasAnimalModel()) {
			if (!player.getAbilities().creativeMode) {
				stack.decrement(1);
			}
			BellaComponents.BELL_COMPONENT.get(animalEntity).setBell(true);
			return ActionResult.success(player.world.isClient);
		}
		return ActionResult.PASS;
	}

	public static ActionResult removeBellFromEntity(AnimalEntity animalEntity, PlayerEntity player, Hand hand) {
		if (!player.world.isClient) {
			player.setStackInHand(hand, new ItemStack(Items.BELL));
		}
		BellaComponents.BELL_COMPONENT.get(animalEntity).setBell(false);
		return ActionResult.success(player.world.isClient);
	}
}
