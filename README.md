# Quick Setup

As a first step the application needs to be downloaded to your local machine. The project is using a [GIT](https://en.wikipedia.org/wiki/Git) as a version control system, however due to it's complexity i would suggest to download the application directly from `GitHub` and extract it on your machine.
Beside the regular [Java SE Development Kit 8 Downloads](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) [Apache Maven](https://maven.apache.org/) is also required to build this project (For more information about the installation please see the `Build management` section).

# Build management

In order to be able to build the project [Maven](https://maven.apache.org/) is required. With `Maven` there's no need to the download or link the external dependencies (libraries) anymore as Maven is using it's own repositories. All project related configurations for `Maven` can be found in the project's pom.xml.

## Command line

To build a project with `Maven`, it requires to be installed first on your machine:

[How to install Maven on Mac OSX](https://www.mkyong.com/maven/install-maven-on-mac-osx/)

As it described in the description, please verify the presence of `Java` and `Maven` on with the following commands (from a terminal):

```
java -version
```

The result should looks something similar like this, if the `Java` is available on your machine (the `JDK` version must be 8, otherwise the project cannot be built):

```
java version "<JDK VERSION>"
Java(TM) SE Runtime Environment (build <JDK BUILD NUMBER>)
Java HotSpot(TM) 64-Bit Server VM (build 25.101-b13, mixed mode)
```

And for `Maven` please run the following command:

```
mvn --version
```

If everything is fine the following message will be displayed:

```
Maven home: /usr/share/maven
Java version: <JDK VERSION>, vendor: Apple Inc.
Java home: <JDK HOME>
Default locale: en_US, platform encoding: MacRoman
OS name: "mac os x", version: "10.7.4", arch: "x86_64", family: "mac"
```

Unfortunately the project has an additional dependency what cannot be found in any `Maven` central repository, therefore this dependency must be installed in advance to your local repositories with the following command FROM THE PROJECT'S ROOT FOLDER:

```
mvn install:install-file -Dfile=lib/idw-gpl.jar -DgroupId=com.infonode -DartifactId=idw-gpl -Dversion=1.6.1 -Dpackaging=jar
```

The result should look something like this:

```
[INFO] --- maven-install-plugin:2.5.2:install-file (default-cli) @ spring-data-jpa-swing-sandbox ---
[INFO] pom.xml not found in idw-gpl.jar
[INFO] Installing <PROJECT DIR>\lib\idw-gpl.jar to <LOCAL REPOSITORY LOCATION>\.m2\repository\com\infonode\idw-gpl\1.6.1\idw-gpl-1.6.1.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.582 s
[INFO] Finished at: 2016-10-23T17:30:45+02:00
[INFO] Final Memory: 12M/309M
[INFO] ------------------------------------------------------------------------
```

As you may see the jar file is located in the project's `lib` directory, but after it has been installed once it will be available for `Maven` via it's local repository so this operation needs to be done only once.

After the necessary dependency is installed the application can be built with the following command (the command has to be executed from the project's root folder):

```
mvn clean install
```

If the project build succeeded the the following message should be displayed:

```
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.631 s
[INFO] Finished at: 2016-10-23T17:36:39+02:00
[INFO] Final Memory: 39M/319M
[INFO] ------------------------------------------------------------------------
```

After a success build the application can be started with the following command:

```
java -jar target/library-0.1.0.jar
```

## IDE

`Maven` is fully integrated with almost all type if IDE, so without any problem the project can be imported to `Eclipse` as well. To import the project to the current workspace:

1. Open [File] -> [Import...] from the menu
2. Select [Mven] / [Existing Maven Projects] from the list and press [Next>]
3. On [Import Maven projects] dialog click on the [Browse...] button and select the project's root directory
4. Make sure that the [/pom.xml hu.aisb:library:0.1.0.jar] in the [Projects:] list is selected and click on the [Finish] button

Run the project from the IDE as a regular `Java` project. The `main` method can be found in `application.Application` class.

# Spring
[Spring](https://spring.io/) is acting as a system integration framework in `aisb-library` project. On the top of the core Spring features the application is using [Spring Boot](https://projects.spring.io/spring-boot/) to make the base configuration a lot more easier (in a [convention over configuration](https://en.wikipedia.org/wiki/Convention_over_configuration) way). With `Spring Boot` it's not required to have complex configurations (e.g.: `.xml` descriptors) in the application's classpath any more. Instead of descriptors `Spring Boot` provides an easy way to apply a feature (usually) without any major configuration, to make a feature available usually there are only two things needs to be done:
- Add the necessary dependency to the classpath (add it to the application's `pom.xml` file and let `Maven` to obtain it during build time)
- Add the proper `@Annotation` to enable the feature (it can be done in a configuration class).
By default `Spring Boot` auto-configuration attempts to automatically configure your Spring application based on the jar dependencies that have been added.

## Features
### Dependency Injection (Spring Core)
In nutshell `Spring`'s Dependency Injection makes it possible to define beans, to lookup the defined beans and to inject the beans into other managed beans in the application context without directly instantiated them. It our case the beans (which are technically plan Java classes usually annotated with one of the available [Stereotype](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/package-summary.html)s or defined as [@Bean](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/annotation/Bean.html)s in one of the [@Configuration](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Configuration.html) classes) are detected by `Spring`, based on the bean's packages with the help of the [@ComponentScan](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/ComponentScan.html) annotation. The detected beans can be injected (referred without instantiation) in other beans with the  [@Autowired](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html) annotation later on.
For more information about the IoC/Dependency Injection please visit the following site: [The IoC container](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html).

As an example there are two implementations of [VolumeListItemModelFacade](https://github.com/remeset/aisb-library/blob/master/src/main/java/ui/volume/search/facade/VolumeListItemModelFacade.java) can be found in the application:
- [LocalVolumeListItemModelFacade](https://github.com/remeset/aisb-library/blob/master/src/main/java/ui/volume/search/facade/LocalVolumeListItemModelFacade.java)
- [RemoteVolumeListItemModelFacade](https://github.com/remeset/aisb-library/blob/master/src/main/java/ui/volume/search/facade/RemoteVolumeListItemModelFacade.java)
The first one is collecting the data from the database (via [VolumeDAO](https://github.com/remeset/aisb-library/blob/master/src/main/java/persistence/dao/volume/VolumeDAO.java)), the secound one is using the [Google Book](https://books.google.com/)'s REST API to find the volumes. The consumer of the data sources does not/should not need to know anything about the implementation details behind the interface (as it's considered as a bad practice). To avoid tightly coupled components `Spring` makes it possible to inject the beans based on it's interface in the following way:

```java
@Component
public class Consumer {
   @Autowired
   @Qualifier("localVolumeListItemModelFacade")
   private VolumeListItemModelFacade facade;
}
```

It's also possible to inject both beans into the consumer bean in the following way:

```java
@Component
public class Consumer {
   @Autowired
   private List<VolumeListItemModelFacade> facades;
}
```

With `Spring`'s [@Configuration](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Configuration.html) annotation it's also possible to assemble the consumer bean from Java code in the following way (this approach is applied in `aisb-library` project) in the following way:

```java
@Configuration
...
public class UIConfiguration {
	...
    @Bean
    public VolumeAdminView createVolumeAdminView(
            @Qualifier("localVolumeListItemModelFacade") VolumeListItemModelFacade<LocalVolumeListItemModel, VolumeEntity> localVolumeModelFacade,
            @Qualifier("remoteVolumeListItemModelFacade") VolumeListItemModelFacade<RemoteVolumeListItemModel, Volume> remoteVolumeModelFacade,
            ...) {
        VolumeAdminControl control = new VolumeAdminControl(..., remoteVolumeModelFacade, volumeAdminModelConverter, ...);
        ...
    }
}
```

Please note: even if the qualifier contains the bean name, the direct implementation is not referred in the configuration class at all.

### Spring Data/JPA
With [Spring Data/JPA](http://projects.spring.io/spring-data-jpa/) `Spring` provides an easy way to handle the database operations in your application. From one hand via `Spring Boot`'s autoconfig it's configuration is very easy:

```java
@ComponentScan
@EntityScan
@EnableJpaRepositories
@PropertySource("classpath:/config/persistence.properties")
public class PersistenceConfiguration {
}
```

Only the data source needs to be configured manually in a `properties` file:

```
spring.datasource.url=jdbc:derby:db/library;create=true
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.apache.derby.jdbc.EmbeddedDriver
spring.jpa.hibernate.ddl-auto=update
```

Beside the above listed configuration only the dependencies (in `pom.xml`) needs to be provided. As it configured `Spring Data/JPA` will use a database called [Derby](https://db.apache.org/derby/) which is a Java based database engine which does not required any database additional database server. The database will be stored in your local file system under the `<PROJECT ROOT>/db/library` directory.
[JPA](http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html) provides an API for an object-relational mapping what makes it possible to define a mapping between your Java object and the database table entries moreover it provides an easy way to create/update/find/remove the entries from database via API calls. `Spring Data/JPA` makes these operations even more easy: it makes it possible to define only the boilerplate interfaces for the operations and Spring will provide the implementation behind it. For more information please see: [Spring Data JPA - Reference Documentation](http://docs.spring.io/spring-data/jpa/docs/current/reference/html/).
The last missing peace in this topic is [Hibernate](http://hibernate.org/orm/): as `JPA` is just the API (a bunch of interfaces without implementations) it cannot be used own its own. `Hibernate` is one of the available `JPA` implementation and by default `Spring Boot` is using `Hibernate` as a `JPA Provider`.
Even if `JPA` provides the mapping between your Java objects and the database table entries, the database needs to be created according the the `Schema` defined by the `JPA` entities: fortunately `Hibernate` provides an easy way to do this as well: with the proper setting of `spring.jpa.hibernate.ddl-auto` property `Hibernate` will initialize the database `Schema` automatically during application start-up time and before the first database operation would be done by the application all the tables and constraints will be presented on the database side.
Beside the above described configurations the followind dependencies are reqired (in `pom.xml`):

```xml
<!-- DATABASE -->
<dependency>
  <groupId>org.apache.derby</groupId>
  <artifactId>derby</artifactId>
  <scope>runtime</scope>
</dependency>
<!-- SPRING DATA JPA -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### Feign
Feign is part of a larger library called [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/) and it's providing an easy way to execute [REST](https://en.wikipedia.org/wiki/Representational_state_transfer) calls from your application. As the most of the available APIs on the internet has a `REST` interface, `Feign` makes it easy to access a lot of useful service on the web e.g.: [PublicAPIs](https://www.publicapis.com/).
`Feign` is following the same approach as `Spring Data\JPA`: with the definition of the interface representation of the API `Feign` will provide the necessary implementation behind it e.g.:

```java
@FeignClient(url = "https://www.googleapis.com", name = "googleapis")
public interface GoogleBooksResource {
    @RequestMapping(value = "/books/v1/volumes?key=${remote.api.google.books.apiKey}", method = RequestMethod.GET)
    public ResponseEntity<Volumes> lookup(@RequestParam("q") String query, @RequestParam("startIndex") int startIndex, @RequestParam("maxResults") int maxResults);
}
```

With the described interface `Feign` is able to provide an implementation which is able to execute `REST` calls to [Google's Books](https://developers.google.com/books/docs/v1/getting_started) REST API with the given parameters (`q`, `startIndex`, `maxResults`) with `GET` request method e.g.: [First 20 books with `flowers` in its title](https://www.googleapis.com/books/v1/volumes?q=flowers+intitle&startIndex=0&maxResults=20&key=AIzaSyAWPIu9neXnpAQU5qQql5mK16kHnMlCHSU)

## UI layer - Binding

### High Level Overview

In general it's not a good practice to access the widgets on the application views directly and from design perspective it's always a good practice during the UI development to separate the model from the view and controller [MVC pattern](http://www.oracle.com/technetwork/articles/javase/index-142890.html). The binding framework give you a hand during this separation and let you bind your model fields with the widgets on the screen so if the user updates a field on the UI the model will be instantly updated (and vice-versa) as well and in this way it will represents always the actual state of the view.
For more information how JGoodies Binding is working in practice please see: [Introduction to JGoodies Binding](http://jnb.ociweb.com/jnb/jnbJun2005.html)

### Dependencies

```
<dependency>
  <groupId>com.jgoodies</groupId>
  <artifactId>jgoodies-binding</artifactId>
  <version>2.13.0</version>
</dependency>
```

### Usage

On model side - to make it possible to notify the widgets on the screen - it requires to fire `PropertyChange` events when the value has been changed. For this a special setter method is required which is able to dispatch the event beside setting the field's value. Also the necessary methods (`addPropertyChangeListener`, `removePropertyChangeListener`) are required to let the binding framework register or unregister its event listeners.

```java
public class Model {
    private String email;

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    public Long getEmail() {
        return email;
    }

    public void setEmail(String email) {
        changeSupport.firePropertyChange("email", this.email, this.email = email);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
```

To bind the field in the model with a specific widget on the view the following needs to be done:

``` java
PresentationModel<Model> adapter = new PresentationModel<>(model);
Bindings.bind(view.getEmailField(), adapter.getModel("email"));
```

For more details about the application of the framework please see: [JGoodies: Understanding Binding - Part 1](http://www.javalobby.org/java/forums/t17672), [JGoodies: Understanding Binding - Part 2](http://www.javalobby.org/java/forums/t17707), [JGoodies: Understanding Binding - Part 3](http://www.javalobby.org/java/forums/t17728)