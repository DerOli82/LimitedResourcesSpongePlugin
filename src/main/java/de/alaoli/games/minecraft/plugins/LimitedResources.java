package de.alaoli.games.minecraft.plugins;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.Plugin;

/**
 * @author DerOli82 <https://github.com/DerOli82>
 */
@Plugin( id = "limitedresources", name = "Limited Resources", description = "Block limitation plugin for sponge ", version = "0.1.0" )
public class LimitedResources
{
    @Inject
    private Logger logger;
}
