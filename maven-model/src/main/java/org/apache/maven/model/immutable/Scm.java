// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 1.8.1,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.model.immutable;

import java.util.Map;

/**
 * 
 *         
 *         The <code>&lt;scm&gt;</code> element contains
 * informations required to the SCM
 *         (Source Control Management) of the project.
 *         
 *       
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class Scm
{
    private String connection;
    private String developerConnection;
    private String tag;
    private String url;
    private java.util.Map<Object, InputLocation> locations;

    public Scm( String connection, String developerConnection, String tag, String url,
                Map<Object, InputLocation> locations )
    {
        this.connection = connection;
        this.developerConnection = developerConnection;
        this.tag = tag;
        this.url = url;
        this.locations = locations;
    }
}