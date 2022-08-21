package com.github.merchantpug.bella.networking;

import com.github.merchantpug.bella.Bella;
import com.github.merchantpug.bella.access.AnimalEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class BellaPacketsC2S {
	public static void register() {
		ServerPlayNetworking.registerGlobalReceiver(BellaPackets.SET_ANIMAL_MODEL, BellaPacketsC2S::onSetAnimalModel);
	}

	private static void onSetAnimalModel(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
		int animalId = packetByteBuf.readInt();
		minecraftServer.execute(() -> {
			Entity entity = playerEntity.getWorld().getEntityById(animalId);
			if (!(entity instanceof AnimalEntity animalEntity)) {
				Bella.LOGGER.warn("Received unknown AnimalEntity from bell interaction.");
				return;
			}
			((AnimalEntityAccess)animalEntity).bella$setHasAnimalModel(true);
		});
	}
}
