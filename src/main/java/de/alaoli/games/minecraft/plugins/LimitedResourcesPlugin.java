package de.alaoli.games.minecraft.plugins;

import com.google.inject.Inject;
import de.alaoli.games.minecraft.plugins.event.handler.BlockEventHandler;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

import java.io.IOException;

/**
 * @author DerOli82 <https://github.com/DerOli82>
 */
@Plugin(
    id = "limitedresources",
    name = "Limited Resources",
    description = "Block limitation and management plugin for Sponge.",
    version = "0.1.0" )
public class LimitedResources
{
    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig( sharedRoot = false )
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    @Listener
    public void onPreInitializationEvent( GamePreInitializationEvent event )
    {
        try
        {
            ConfigurationNode node = this.loader.load();

            this.loader.save( node );

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }




    }

    @Listener
    public void onInitializationEvent( GameInitializationEvent event )
    {

    }
}
