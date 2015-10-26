# Apache Geode example using Groovy and SpringBoot

One of the nice things about Groovy is the usage of @Grab annotations to download dependencies
without the need for a dependency manager such as Maven or Gradle explicitly. For example:

```
@GrabResolver(name='asf-snapshots', root="https://repository.apache.org/content/repositories/snapshots")
@Grab(group="org.apache.geode", module = "gemfire-core", version = "1.0.0-incubating-SNAPSHOT")
```

SpringBoot provides

This two Groovy scripts illustrate examples of Geode clients from Groovy and

## Requirements

1. Apache Geode - https://github.com/apache/incubator-geode
1. SDKMAN! (Previously known as GVM) - http://sdkman.io/
1. Spring Boot CLI
  1. `sdk install springboot`

## Running the PDX example

An entity named Person is available in both scripts and that's going to be used for our data model. Using Groovy @Canonical annotation
```
@Canonical
class Person {
    int id
    String name
    String lastname
    String email
    String city
}
```
### Starting Apache Geode

Replace the GEODE_HOME variable in the startGeode.sh.

Run `startGeode.sh` on a terminal. It's goingto start a locator and a server with a single [PARTITIONED](http://geode.docs.pivotal.io/docs/developing/partitioned_regions/chapter_overview.html) region named `Person`.

### Producer

Producer is a simple Groovy script that is leveraging SpringBoot and @Grab, populating the region with 100 entries every 5 seconds.

```
spring run Producer.groovy -- --locatorHost=localhost --locatorPort=10334
```

The output of the script will show Geode client and PdxType information:

```
[info 2015/10/26 09:57:09.484 PDT <pool-8-thread-1> tid=0x39] Defining: PdxType[
    dsid=0typenum=1, name=Person, fields=[
        id:int:0:idx0(relativeOffset)=0:idx1(vlfOffsetIndex)=0
        name:String:1:idx0(relativeOffset)=4:idx1(vlfOffsetIndex)=-1
        lastname:String:2:1:idx0(relativeOffset)=0:idx1(vlfOffsetIndex)=1
        email:String:3:2:idx0(relativeOffset)=0:idx1(vlfOffsetIndex)=2
        city:String:4:3:idx0(relativeOffset)=0:idx1(vlfOffsetIndex)=3]]
```

Open a new terminal and change the `Person` entity definition.  You can add or remove a field. In this run `city` got removed.  Re-run Producer.groovy script and leave the previous version still running.

Note the difference in the the PdxType output:

```
[info 2015/10/26 10:06:52.879 PDT <pool-8-thread-1> tid=0x37] Defining: PdxType[
    dsid=0typenum=2, name=Person, fields=[
        id:int:0:idx0(relativeOffset)=0:idx1(vlfOffsetIndex)=0
        name:String:1:idx0(relativeOffset)=4:idx1(vlfOffsetIndex)=-1
        lastname:String:2:1:idx0(relativeOffset)=0:idx1(vlfOffsetIndex)=1
        email:String:3:2:idx0(relativeOffset)=0:idx1(vlfOffsetIndex)=2]]
```

### Consumer

The consumer script follow the same pattern but reading data from Person region.

```
spring run Consumer.groovy -- --locatorHost=localhost --locatorPort=10334
```

Change the definition of the Person entity on the Consumer.groovy and note how PDX handled the missing/added fields.

Consumer with `city` field:

```
Person(97, Ernő {97}, null, null, Budapest)
Person(98, Ernő {98}, null, null, Budapest)
```

Consumer without `city` field:

```
Person(97, Ernő {97}, null, null)
Person(98, Ernő {98}, null, null)
```

# Conclusion

Apache Geode PDX offers a powerful mechanism for data model versioning in distributed systems, allowing different versions of clients to run concurrently.

If client/server were running on separate machines, this would reflect on network usage as well, sending less bytes in a real world scenario with larger objects.
