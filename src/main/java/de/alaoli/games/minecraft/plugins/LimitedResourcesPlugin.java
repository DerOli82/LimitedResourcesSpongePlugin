/* *************************************************************************************************************
 * Copyright (c) 2017 DerOli82 <https://github.com/DerOli82>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see:
 *
 * https://www.gnu.org/licenses/lgpl-3.0.html
 ************************************************************************************************************ */
package de.alaoli.games.minecraft.plugins;

import com.google.inject.Inject;
import de.alaoli.games.minecraft.plugins.manager.BlockManager;
import de.alaoli.games.minecraft.plugins.manager.FileHandling;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author DerOli82 <https://github.com/DerOli82>
 */
@Plugin( id = PluginInfo.ID, name = PluginInfo.NAME, description = PluginInfo.DESC, version = PluginInfo.VERSION )
public class LimitedResourcesPlugin
{
    /* **************************************************************************************************************
     * Attribute
     ************************************************************************************************************** */

    private static LimitedResourcesPlugin instance;

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir( sharedRoot = false )
    private Path configPath;

    /* **************************************************************************************************************
     * Method
     ************************************************************************************************************** */

    public static Optional<Logger> getLogger()
    {
        if( instance == null ) { return Optional.empty(); }

        return Optional.ofNullable( instance.logger );
    }

    /* **************************************************************************************************************
     * Method - Sponge Events
     ************************************************************************************************************** */

    @Listener
    public void onPreInitializationEvent( GamePreInitializationEvent event )
    {
        instance = this;

        try
        {
            FileHandling bm = BlockManager.getInstance();
            bm.init( this.configPath );
            bm.load();
        }
        catch( IOException e )
        {
            this.logger.error( "Can't load blocks.conf because: " + e.getMessage() );
        }
    }

    @Listener
    public void onGameStoppedServerEvent( GameStoppedServerEvent event )
    {
        try
        {
            FileHandling bm = BlockManager.getInstance();
            bm.save();
        }
        catch( IOException e )
        {
            this.logger.error( "Can't save blocks.conf because: " + e.getMessage() );
        }
    }
}
