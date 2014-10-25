// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 1.8.1,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.model.immutable;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 
 *         
 *         The <code>&lt;project&gt;</code> element is the root of
 * the descriptor.
 *         The following table lists all of the possible child
 * elements.
 *         
 *       
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class Model
    extends ModelBase
{
    public final String modelVersion;
    public final Parent parent;
    public final String groupId;
    public final String artifactId;
    public final String version;
    public final String packaging;
    public final String name;
    public final String description;
    public final String url;
    public final String inceptionYear;
    public final Organization organization;
    public java.util.List<License> licenses;
    public java.util.List<Developer> developers;
    public java.util.List<Contributor> contributors;
    public java.util.List<MailingList> mailingLists;
    public final Prerequisites prerequisites;
    public final Scm scm;
    public final IssueManagement issueManagement;
    public final CiManagement ciManagement;
    public final Build build;
    public java.util.List<Profile> profiles;
    public String modelEncoding = "UTF-8";

    public Model( List<String> modules, DistributionManagement distributionManagement, Properties properties,
                  DependencyManagement dependencyManagement, List<Dependency> dependencies,
                  List<Repository> repositories, List<Repository> pluginRepositories, Object reports,
                  Reporting reporting, Map<Object, InputLocation> locations, String modelVersion, Parent parent,
                  String groupId, String artifactId, String version, String packaging, String name, String description,
                  String url, String inceptionYear, Organization organization, List<License> licenses,
                  List<Developer> developers, List<Contributor> contributors, List<MailingList> mailingLists,
                  Prerequisites prerequisites, Scm scm, IssueManagement issueManagement, CiManagement ciManagement,
                  Build build, List<Profile> profiles, String modelEncoding )
    {
        super( modules, distributionManagement, properties, dependencyManagement, dependencies, repositories,
               pluginRepositories, reports, reporting, locations );
        this.modelVersion = modelVersion;
        this.parent = parent;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.packaging = packaging;
        this.name = name;
        this.description = description;
        this.url = url;
        this.inceptionYear = inceptionYear;
        this.organization = organization;
        this.licenses = licenses;
        this.developers = developers;
        this.contributors = contributors;
        this.mailingLists = mailingLists;
        this.prerequisites = prerequisites;
        this.scm = scm;
        this.issueManagement = issueManagement;
        this.ciManagement = ciManagement;
        this.build = build;
        this.profiles = profiles;
        this.modelEncoding = modelEncoding;
    }
}