package me.khajiitos.protectyourstructures;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Effects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ProtectYourStructures.MOD_ID);
    public static final MobEffect SACRED_PLACE_EFFECT = new SacredPlaceMobEffect(MobEffectCategory.NEUTRAL, 0x4a4217);

    public static void init(IEventBus eventBus) {
        EFFECTS.register("sacred_place", () -> SACRED_PLACE_EFFECT);
        EFFECTS.register(eventBus);
    }
}
