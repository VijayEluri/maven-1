package org.apache.maven.lifecycle;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.lifecycle.internal.ExecutionPlanItem;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;

//TODO: lifecycles being executed
//TODO: what runs in each phase
//TODO: plugins that need downloading
//TODO: project dependencies that need downloading
//TODO: unfortunately the plugins need to be downloaded in order to get the plugin.xml file. need to externalize this
//      from the plugin archive.
//TODO: this will be the class that people get in IDEs to modify

public class MavenExecutionPlan
    implements Iterable<ExecutionPlanItem>
{

    /*
       At the moment, this class is totally immutable, and this is in line with thoughts about the
       pre-calculated execution plan that stays the same during the execution.

       If deciding to add mutable state to this class, it should be at least considered to
       separate this into a separate mutable structure.

     */

    /**
     * For project dependency resolution, the scopes of resolution required if any.
     */
    private final Set<String> requiredDependencyResolutionScopes;

    /**
     * For project dependency collection, the scopes of collection required if any.
     */
    private final Set<String> requiredDependencyCollectionScopes;

    private final List<ExecutionPlanItem> planItem;

    private final Map<String, ExecutionPlanItem> lastMojoExecutionForAllPhases;


    final List<String> phasesInExecutionPlan;

    public MavenExecutionPlan( Set<String> requiredDependencyResolutionScopes,
                               Set<String> requiredDependencyCollectionScopes, List<ExecutionPlanItem> planItem,
                               DefaultLifecycles defaultLifecycles )
    {
        this.requiredDependencyResolutionScopes = requiredDependencyResolutionScopes;
        this.requiredDependencyCollectionScopes = requiredDependencyCollectionScopes;
        this.planItem = planItem;
        lastMojoExecutionForAllPhases = new LinkedHashMap<String, ExecutionPlanItem>();

        LinkedHashSet<String> totalPhaseSet = new LinkedHashSet<String>();
        if ( defaultLifecycles != null )
        {
            for ( String phase : getDistinctPhasesInOrderOfExecutionPlanAppearance( planItem ) )
            {
                final Lifecycle lifecycle = defaultLifecycles.get( phase );
                if ( lifecycle != null )
                {
                    totalPhaseSet.addAll( lifecycle.getPhases() );
                }
            }
        }
        this.phasesInExecutionPlan = new ArrayList<String>( totalPhaseSet );

        Map<String, ExecutionPlanItem> lastInExistingPhases = new HashMap<String, ExecutionPlanItem>();
        for ( ExecutionPlanItem executionPlanItem : getExecutionPlanItems() )
        {
            lastInExistingPhases.put( executionPlanItem.getLifecyclePhase(), executionPlanItem );
        }

        ExecutionPlanItem lastSeenExecutionPlanItem = null;
        ExecutionPlanItem forThisPhase;

        for ( String phase : totalPhaseSet )
        {
            forThisPhase = lastInExistingPhases.get( phase );
            if ( forThisPhase != null )
            {
                lastSeenExecutionPlanItem = forThisPhase;
            }
            lastMojoExecutionForAllPhases.put( phase, lastSeenExecutionPlanItem );

        }
    }


    public Iterator<ExecutionPlanItem> iterator()
    {
        return getExecutionPlanItems().iterator();
    }

    /**
     * Returns the last ExecutionPlanItem in the supplied phase. If no items are in the specified phase,
     * the closest executionPlanItem from an earlier phase item will be returned.
     *
     * @param requestedPhase the requested phase
     *                       The execution plan item
     * @return The ExecutionPlanItem or null if none can be found
     */
    public ExecutionPlanItem findLastInPhase( String requestedPhase )
    {
        return lastMojoExecutionForAllPhases.get( requestedPhase );
    }

    private List<ExecutionPlanItem> getExecutionPlanItems()
    {
        return planItem;
    }


    private static Iterable<String> getDistinctPhasesInOrderOfExecutionPlanAppearance(
        List<ExecutionPlanItem> planItems )
    {
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        for ( ExecutionPlanItem executionPlanItem : planItems )
        {
            final String phase = executionPlanItem.getLifecyclePhase();
            if ( !result.contains( phase ) )
            {
                result.add( phase );
            }
        }
        return result;
    }

    public void forceAllComplete()
    {
        for ( ExecutionPlanItem executionPlanItem : getExecutionPlanItems() )
        {
            executionPlanItem.forceComplete();
        }
    }

    public void waitUntilAllDone()
        throws InterruptedException
    {
        for ( ExecutionPlanItem executionPlanItem : getExecutionPlanItems() )
        {
            executionPlanItem.waitUntilDone();
        }
    }

    public boolean containsPhase( String phase )
    {
        return phasesInExecutionPlan.contains( phase );
    }

    public Set<String> getRequiredResolutionScopes()
    {
        return requiredDependencyResolutionScopes;
    }

    public Set<String> getRequiredCollectionScopes()
    {
        return requiredDependencyCollectionScopes;
    }

    public List<MojoExecution> getMojoExecutions()
    {
        List<MojoExecution> result = new ArrayList<MojoExecution>();
        for ( ExecutionPlanItem executionPlanItem : planItem )
        {
            result.add( executionPlanItem.getMojoExecution() );
        }
        return result;
    }


    public Set<Plugin> getNonThreadSafePlugins()
    {
        Set<Plugin> plugins = new HashSet<Plugin>();
        for ( ExecutionPlanItem executionPlanItem : planItem )
        {
            final MojoExecution mojoExecution = executionPlanItem.getMojoExecution();
            if ( !mojoExecution.getMojoDescriptor().isThreadSafe() )
            {
                plugins.add( mojoExecution.getPlugin() );
            }
        }
        return plugins;
    }

    // Used by m2e but will be removed, really.

    @SuppressWarnings( { "UnusedDeclaration" } )
    @Deprecated
    public List<MojoExecution> getExecutions()
    {
        return getMojoExecutions();
    }

    public int size()
    {
        return planItem.size();
    }

}
