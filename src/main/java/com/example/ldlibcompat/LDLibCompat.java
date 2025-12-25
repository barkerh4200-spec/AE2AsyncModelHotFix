package com.example.ldlibcompat;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod("ldlibcompat")
public class LDLibCompat {

    public static final Logger LOGGER = LogUtils.getLogger();

    public LDLibCompat() {
        LOGGER.info("AE2AsyncModelHotFix Patch loaded");
    }
}
