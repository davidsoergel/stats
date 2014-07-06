stats
=====

_A Java library for simple statistics_

 * Some basic data structures and distributions for statistical computations.

Documentation
-------------

 * [API docs](http://davidsoergel.github.io/stats/)

Download
--------

[Maven](http://maven.apache.org/) is by far the easiest way to make use of stats.  Just add these to your pom.xml:
```xml
<repositories>
	<repository>
		<id>dev.davidsoergel.com releases</id>
		<url>http://dev.davidsoergel.com/nexus/content/repositories/releases</url>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
	</repository>
	<repository>
		<id>dev.davidsoergel.com snapshots</id>
		<url>http://dev.davidsoergel.com/nexus/content/repositories/snapshots</url>
		<releases>
			<enabled>false</enabled>
		</releases>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>com.davidsoergel</groupId>
		<artifactId>stats</artifactId>
		<version>0.931</version>
	</dependency>
</dependencies>
```

If you really want just the jar, you can get the [latest release](http://dev.davidsoergel.com/nexus/content/repositories/releases/com/davidsoergel/stats/) from the Maven repo; or get the [latest stable build](http://dev.davidsoergel.com/jenkins/job/stats/lastStableBuild/com.davidsoergel$stats/) from the build server.

