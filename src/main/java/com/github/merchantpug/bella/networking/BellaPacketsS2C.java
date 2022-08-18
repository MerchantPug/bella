package com.github.merchantpug.bella.networking;


import com.github.merchantpug.bella.Bella;
import com.github.merchantpug.bella.access.LivingEntityAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayConnectionEvents;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class BellaPacketsS2C {
	@Environment(EnvType.CLIENT)
	public static void register() {
		ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
			ClientPlayNetworking.registerReceiver(BellaPackets.SYNC_BELL_POSITION, BellaPacketsS2C::syncBellPosition);
		}));
	}

	private static void syncBellPosition(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender) {
		int entityId = buf.readInt();
		float bellRotation = buf.readFloat();
		minecraftClient.execute(() -> {
			Entity entity = clientPlayNetworkHandler.getWorld().getEntityById(entityId);
			if (!(entity instanceof LivingEntity livingEntity)) {
				Bella.LOGGER.warn("Tried modifying non LivingEntity's bell rotation.");
				return;
			}
			((LivingEntityAccess)livingEntity).bella$setBellRotation(bellRotation);
		});
	}
}
