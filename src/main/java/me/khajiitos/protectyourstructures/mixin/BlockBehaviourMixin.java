package me.khajiitos.protectyourstructures.mixin;

import me.khajiitos.protectyourstructures.Effects;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(at = @At("HEAD"), method = "getDestroyProgress", cancellable = true)
    public void getDestroyProgress(BlockState blockState, Player player, BlockGetter blockGetter, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (player instanceof LocalPlayer) {
            if (player.hasEffect(Effects.SACRED_PLACE_EFFECT) && blockGetter.getBlockState(pos).getBlock() != Blocks.SPAWNER) {
                cir.setReturnValue(0.0f);
            }
        }
    }
}
