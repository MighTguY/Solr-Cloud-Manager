[![Coverage Status](https://coveralls.io/repos/github/MighTguY/Solr-Cloud-Manager/badge.svg?branch=master)](https://coveralls.io/github/MighTguY/Solr-Cloud-Manager?branch=master)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://raw.githubusercontent.com/MighTguY/customized-symspell/master/LICENSE)

# Solr-Cloud-Manager

Traditionally for setting up solr with our collections, we need to manually push our configurations to zookeeper, and then we need to create collections from the collections API of SOLR.
And then we need to create aliases manually. This is a 4-5 step process and sometimes error prone to collections
of SOLR.


So here is the solr-cloud-initializer which will initialize your SOLR Cloud with pushing the fellow collections 
to the Zookeper and marking your instance up and ready for indexing.

This can also be used to update solr-configurations like adding new field and reload collections 
 
 

## Why Alias Switching ?

Suppose we have  collection named 'groceries' , now if we were doing indexing at groceries which was surving
 traffic at same time how are we going to achive it with zero down time.Because incase of fULL Indexing we need to delete
 the complete data and then do the indexing, which may lead to some down time.
 
 So the ideal way of doing it is , keep 2 collections 
 ```
 Before indexing 
           groceries_A    | ---> groceriesLive(Alias)   ----- > serving search traffic
           groceries_B    | ---> groceriesShadow(Alias) ----- > serving full indexing
 ```
 one as Live and one as Shadow, Services will always point to Live
 collection to serve trafic, all the indexing will be done to the shadow collection and once it is done.
 Alias switching will happen.
 (Live and Shadow are aliases)
 
  So in our case will keep groceries_A and groceries_B, so for now groceries_A is search serving collection 
  i.e groceriesLive alias is pointing to groceries_A and groceriesShadow to groceries_B.
  
  Now when the indexing will happen at groceriesShadow i.e groceries_B, as groceries_A is serving traffic,
  Once indexing successfully completed will switch the aliases so that 
  
   ```
   After indexing on shadow collection
             groceries_B    | ---> groceriesLive(Alias)   ----- > serving search traffic
             groceries_A    | ---> groceriesShadow(Alias) ----- > serving full indexing
 
   ```
 
  groceries_B will now become groceriesLive and groceries_A will be groceriesShadow collection.
  
 

### Prerequisites

Please make sure the collection configurations are updated in the solr-configurations module
for now only groceries and typeahead has been added to them.
To run SOLR cloud initializer it is mandatory to build solr-configurations.
so make sure your configurations are updated.

* Zk is started and chroot is created
* SolrCloud's nodes pointing to zk with chroot are started(jar with plugins should be already present there)


### Running SOLR Cloud Initialiser

It's easy to run SOLR cloud initialise as it is a spring boot command line runner application,
just run  

```
java -jar build/libs/solr-cloud-initialiser-1.0-SNAPSHOT.jar  
```
