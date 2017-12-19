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

package de.alaoli.games.minecraft.plugins.data;

import java.util.Optional;

/**
 * @author DerOli82 <https://github.com/DerOli82>
 */
public class ManagedBlock implements ForbiddenBlock, LimitedBlock
{
    /* **************************************************************************************************************
     * Attribute
     ************************************************************************************************************** */

    private final String identifier;

    private int limit;
    private String reason;

    /* **************************************************************************************************************
     * Method
     ************************************************************************************************************** */

    public ManagedBlock( String identifier )
    {
        this.identifier = identifier;
        this.limit = -1;
    }

    public void setLimit( int limit )
    {
        this.limit = limit;
    }

    public void setReason( String reason )
    {
        this.reason = reason;
    }

    @Override
    public int hashCode()
    {
        return this.identifier.hashCode();
    }

    @Override
    public boolean equals( Object obj )
    {
        return obj instanceof ManagedBlock && ((ManagedBlock)obj).identifier.equals( this.identifier );
    }

    /* **************************************************************************************************************
     * Method - Implement Block
     ************************************************************************************************************** */

    @Override
    public String getIdentifier()
    {
        return this.identifier;
    }

    /* **************************************************************************************************************
     * Method - Implement ForbiddenBlock
     ************************************************************************************************************** */

    @Override
    public boolean isForbidden()
    {
        return this.limit == 0;
    }

    @Override
    public Optional<String> getReason()
    {
        return Optional.ofNullable( this.reason );
    }

    /* **************************************************************************************************************
     * Method - Implement LimitedBlock
     ************************************************************************************************************** */

    @Override
    public boolean isLimited()
    {
        return this.limit > 0;
    }

    @Override
    public int getLimit()
    {
        return this.limit;
    }
}