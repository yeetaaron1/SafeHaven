package oi.yeetaaron1.org;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafeHaven implements ModInitializer {
    public static final String MOD_ID = "safehaven";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("SafeHaven server-side mod initializing...");
    }
}
