package me.khajiitos.protectyourstructures;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ProtectYourStructures.MOD_ID)
public class ProtectYourStructures {
    public static final String MOD_ID = "protectyourstructures";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ProtectYourStructures() {
        Effects.init(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(new EventListeners());
    }
}
