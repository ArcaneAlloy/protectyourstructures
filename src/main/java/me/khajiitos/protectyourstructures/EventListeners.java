package me.khajiitos.protectyourstructures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventListeners {

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent e) {
        Config.load(e.getServer().registryAccess());
    }

    // The BoundingBox#encapsulate function has no reason to be deprecated?
    @SuppressWarnings("deprecation")
    public BoundingBox getStructureWholeBox(ServerLevel level, BlockPos blockPos, ResourceKey<Structure> structureKey) {
        StructureManager manager = level.structureManager();
        Structure structure = manager.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY).get(structureKey);

        BoundingBox boundingBox = new BoundingBox(0, 0, 0, 0, 0, 0);

        if (structure == null) {
            return boundingBox;
        }

        for (StructureStart structureStart : manager.startsForStructure(SectionPos.of(blockPos), structure)) {
            boundingBox.encapsulate(structureStart.getBoundingBox());
        }

        return boundingBox;
    }

    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent e) {
        if (e.player instanceof ServerPlayer player && player.tickCount % 20 == 0) {
            for (ResourceKey<Structure> structureKey : Config.structures) {
                BoundingBox structureBox = getStructureWholeBox(player.getLevel(), player.getOnPos(), structureKey);
                if (structureBox.intersects(new BoundingBox(player.getOnPos()))) {
                    player.addEffect(new MobEffectInstance(Effects.SACRED_PLACE_EFFECT, 600));
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent e) {
        if (e.getPlayer() != null && !e.getPlayer().getAbilities().instabuild && e.getPlayer().hasEffect(Effects.SACRED_PLACE_EFFECT) && e.getState().getBlock() != Blocks.SPAWNER) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent e) {
        if (e.getEntity() instanceof Player player && !player.getAbilities().instabuild && player.hasEffect(Effects.SACRED_PLACE_EFFECT)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlaceFluid(FillBucketEvent e) {
        if (e.getEntity() != null && !e.getEntity().getAbilities().instabuild && e.getEntity().hasEffect(Effects.SACRED_PLACE_EFFECT)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlaceBlockByInteract(PlayerInteractEvent.RightClickBlock e) {
        if (e.getEntity() != null && !e.getEntity().getAbilities().instabuild && e.getItemStack().getItem() instanceof BlockItem && e.getEntity().hasEffect(Effects.SACRED_PLACE_EFFECT)) {
            e.setCanceled(true);
        }
    }
}