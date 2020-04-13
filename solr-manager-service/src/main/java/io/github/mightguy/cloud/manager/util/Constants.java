
package io.github.mightguy.cloud.manager.util;

public final class Constants {

  private Constants() {
  }

  public static final String ZK_MODE_CLIENT = "zk";
  public static final String HTTP_MODE_CLIENT = "http";
  public static final String ALL_COLLECTIONS = "all";
  public static final String STATUS_COLLECTIONS_RELOAD_COMPLETED = "collections reload is complete";
  public static final String STATUS_COLLECTIONS_INTITALIZED =
      "SolrCloud All Collections got initialized";
  public static final String STATUS_COLLECTION_INTITALIZED = "SolrCloud Collection got initialized";
  public static final String STATUS_CONFIG_UPLOADED = "SolrCloud Collections config got uploaded";
  public static final String FIRST_COLLECTION_SUFFIX = "_A";
  public static final String SECOND_COLLECTION_SUFFIX = "_B";
  public static final String RESTORE_COLLECTION_SUFFIX = "_restore";
  public static final String BACKUP_COLLECTION_SUFFIX = "_backup";
  public static final String PATH_DELIM = "/";
  public static final String COLLECTION_RELOADED = "Collection Reloaded ";
  public static final String ALL_ALIASES_DELETED = "All Aliases Deleted";
  public static final String ALL_COLLECTION_DELETED = "All Collections Deleted ";
  public static final String COLLECTION_DELETED = "Collection Deleted ";
  public static final String INVALID_COLLECTION = "INVALID COLLECTION TO QUERY";
  public static final String INVALID_CLUSTER = "INVALID CLUSTER TO CONNECT";
  public static final String BACKUP_CREATED = "Backup Created";
  public static final String BACKUP_RESTORED = "Backup fully restored";
  public static final String SOLR_CLOUD_SUCCESSFULLY_INITIALIZED =
      "Solr cloud successfully initialized";
  public static final String SOLR_CLOUD_INITIALIZATION_FAILED = "Solr cloud  initialization failed";
  public static final String SLASH = "/";


}
