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
 ************************************************************************************************************* */
package de.alaoli.games.minecraft.plugins.manager;

import de.alaoli.games.minecraft.plugins.data.Block;
import de.alaoli.games.minecraft.plugins.data.ForbiddenBlock;
import de.alaoli.games.minecraft.plugins.data.LimitedBlock;
import de.alaoli.games.minecraft.plugins.data.ManagedBlock;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author DerOli82 <https://github.com/DerOli82>
 */
public class BlockManager implements FileHandling
{
    /* **************************************************************************************************************
	 * Attribute
	 ************************************************************************************************************** */

    private static class LazyHolder
    {
        private static final BlockManager INSTANCE = new BlockManager();
    }
    private final Set<Block> blocks = new HashSet<>();
    private final Set<ForbiddenBlock> forbidden = new HashSet<>();
    private final Set<LimitedBlock> limited = new HashSet<>();

    private Path path;
    private boolean markDirty = false;

	/* **************************************************************************************************************
	 * Method
	 ************************************************************************************************************** */

    private BlockManager() {}

    public static BlockManager getInstance()
    {
        return LazyHolder.INSTANCE;
    }

    public void addBlock( Block block )
    {
        this.blocks.add( block );
        this.markDirty = true;
    }

    public void addBlock( ConfigurationNode node )
    {
        ManagedBlock block = new ManagedBlock( node.getKey().toString() );

        boolean isForbidden = node.getNode( "isForbidden" ).getBoolean( false );
        boolean isLimited = node.getNode( "isLimited" ).getBoolean( false );

        if( isForbidden )
        {
            block.setLimit( 0 );
            block.setReason( node.getNode( "reason" ).getString() );
        }
        else if( isLimited )
        {
            block.setLimit( node.getNode( "limit" ).getInt( 0 ) );
        }
        else
        {
            block.setLimit( node.getNode( "limit" ).getInt( -1 ) );
        }
        this.addBlock( block );
    }

    public void removeBlock( Block block )
    {
        this.blocks.remove( block );
        this.markDirty = true;
    }

    private void refresh()
    {
        this.forbidden.clear();
        this.limited.clear();

        this.blocks.stream()
            .filter( block -> block instanceof ForbiddenBlock && ((ForbiddenBlock)block).isForbidden() )
            .forEach( block -> this.forbidden.add( (ForbiddenBlock)block) );

        this.blocks.stream()
            .filter( block -> block instanceof LimitedBlock && ((LimitedBlock)block).isLimited() )
            .forEach( block -> this.limited.add( (LimitedBlock)block) );
    }

    public boolean isForbidden( String identifier )
    {
        return false;
    }

    public boolean isLimited( String identifier )
    {
        return false;
    }

    /* **************************************************************************************************************
     * Method - Implement FileHandling
     ************************************************************************************************************** */

    @Override
    public void init( Path path )
    {
        if( path == null ) { throw new IllegalArgumentException( "Config path is missing." ); }

        this.path = path.resolve( "blocks.conf" );
    }

    @Override
    public void load() throws IOException
    {
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader
            .builder()
            .setPath( this.path )
            .build();
        loader.load().getChildrenMap().values().forEach( this::addBlock );
        this.markDirty = false;
        this.refresh();
    }

    @Override
    public void save() throws IOException
    {
        if( !this.markDirty ) { return; }

        /*
         * @// TODO save handling
         */

        this.markDirty = false;
        this.refresh();
    }

    @Override
    public void markDirty()
    {
        this.markDirty = true;
    }

    @Override
    public boolean isDirty()
    {
        return this.markDirty;
    }
}