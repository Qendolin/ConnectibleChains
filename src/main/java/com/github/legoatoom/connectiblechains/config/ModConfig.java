package com.github.legoatoom.connectiblechains.config;

import com.github.legoatoom.connectiblechains.ConnectibleChains;
import com.github.legoatoom.connectiblechains.util.NetworkingPackages;
import io.netty.buffer.Unpooled;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

@Config(name = ConnectibleChains.MODID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    private static final transient boolean IS_DEBUG_ENV = FabricLoader.getInstance().isDevelopmentEnvironment();

    @ConfigEntry.Gui.Tooltip(count = 3)
    private float chainHangAmount = 9.0F;
    @ConfigEntry.BoundedDiscrete(max = 32)
    @ConfigEntry.Gui.Tooltip(count = 3)
    private int maxChainRange = 7;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 8)
    @ConfigEntry.Gui.Tooltip()
    private int quality = 4;

    public float getChainHangAmount() {
        return chainHangAmount;
    }

    @SuppressWarnings("unused")
    public void setChainHangAmount(float chainHangAmount) {
        this.chainHangAmount = chainHangAmount;
    }

    public int getMaxChainRange() {
        return maxChainRange;
    }

    @SuppressWarnings("unused")
    public void setMaxChainRange(int maxChainRange) {
        this.maxChainRange = maxChainRange;
    }

    public boolean doDebugDraw() {
        return IS_DEBUG_ENV && MinecraftClient.getInstance().options.debugEnabled;
    }

    @SuppressWarnings("unused")
    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getQuality() {
        return quality;
    }

    public void syncToClients(MinecraftServer server) {
        for (ServerPlayerEntity player : PlayerLookup.all(server)) {
            syncToClient(player);
        }
    }

    public void syncToClient(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, NetworkingPackages.S2C_CONFIG_SYNC_PACKET, this.writePacket());
    }

    public PacketByteBuf writePacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeFloat(chainHangAmount);
        return buf;
    }

    public void readPacket(PacketByteBuf buf) {
        this.chainHangAmount = buf.readFloat();
    }

    public ModConfig copyFrom(ModConfig config) {
        this.chainHangAmount = config.chainHangAmount;
        this.maxChainRange = config.maxChainRange;
        this.quality = config.quality;
        return this;
    }
}
