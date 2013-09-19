Gogo
=====================
A tiny java application launcher.

Current build status: [![Build Status](https://buildhive.cloudbees.com/job/decebals/job/gogo/badge/icon)](https://buildhive.cloudbees.com/job/decebals/job/gogo/)

Features/Benefits
-------------------
Gogo is an open source (Apache license) tiny (around 12KB) java application launcher, with zero dependencies and a quick learning curve.   
With gogo you can add resources (jars and classes) to your application classpath in a dynamically mode, specifying the main class add VM options and run your application with a simple double-click.

Using Maven
-------------------
In your pom.xml you must define the dependencies to Gogo artifacts with:

```xml
<dependency>
    <groupId>ro.fortsoft.gogo</groupId>
    <artifactId>gogo</artifactId>
    <version>${gogo.version}</version>
</dependency>    
```

where ${gogo.version} is the last gogo version.

You may want to check for the latest released version using [Maven Search](http://search.maven.org/#search%7Cga%7C1%7Cgogo)

How to use
-------------------

It's very simple to use gogo with your application:

- copy gogo.jar file in your application folder
- create a gogo.properties file ([few properties](https://github.com/decebals/gogo/blob/master/src/main/resources/gogo.properties))
- run application with `java -jar gogo.jar`

Gogo supports a hierarchy of properties file. In decreasing precedence they are:

- java system properties (ex: `java -Dapp.class.path=lib/*;jdbc-drivers gogo.jar`)
- gogo.properties from current directory
- gogo.properties from gogo.jar

You can specify at runtime the location of the gogo properties file using gogo.properties property (ex: `java -Dgogo.properties=/home/decebal/.gogo/gogo.properties`). 

In gogo.properties are defined by default two sections (kinds) of parameters: app and vm.   
The app parameters are parameters prefixed with `app.` and they are used by gogo as informations for launch your application.    
The vm parameters are parameters prefixed with `vm.` and are passed as system properties in your application.  

Example

__$ ls__
````shell
gogo.jar  
gogo.properties  
jdbc-drivers  
lib
````

__$ cat gogo.properties__ 
````shell
# specify additional class path resources. Optional
app.class.path=lib/*;jdbc-drivers
# specify the main class of the application to run. Required
app.main.class=ro.fortsoft.gogo.demo.Main
# specify main jar. Optional
app.main.jar=

vm.http.proxyHost=192.168.16.1
vm.http.proxyPort=128
````


__$ java -jar gogo-0.1.jar__ 
````shell
propertiesFile = gogo.properties
-- listing properties --
app.main.class=ro.fortsoft.gogo.demo.Main
app.class.path=lib/*;jdbc-drivers
app.main.jar=
launcherClassLoader = 'ro.fortsoft.gogo.LauncherClassLoader@1cfb549'
-- listing class loader urls --
file:/home/decebal/work/gogo/demo/jdbc-drivers/
file:/home/decebal/work/gogo/demo/lib/poi-3.7.jar
file:/home/decebal/work/gogo/demo/lib/swingx-action-1.6.5-1.jar
file:/home/decebal/work/gogo/demo/lib/swingx-autocomplete-1.6.5-1.jar
file:/home/decebal/work/gogo/demo/lib/swingx-common-1.6.5-1.jar
file:/home/decebal/work/gogo/demo/lib/swingx-core-1.6.5-1.jar
file:/home/decebal/work/gogo/demo/lib/swingx-graphics-1.6.5-1.jar
file:/home/decebal/work/gogo/demo/lib/swingx-painters-1.6.5-1.jar
file:/home/decebal/work/gogo/demo/lib/swingx-plaf-1.6.5-1.jar
file:/home/decebal/work/gogo/demo/lib/TableLayout-20050920.jar
file:/home/decebal/work/gogo/demo/lib/xpp3_min-1.1.4c.jar
file:/home/decebal/work/gogo/demo/lib/xstream-1.3.1.jar
mainClassName = 'ro.fortsoft.gogo.demo.Main'
>>> Hello from "Demo"
```

Now I want to add some comments to above example.

First, you can see that my example contains two folders (lib and jdbc-drivers) and the gogo's components (gogo.jar and gogo.properties).  
The lib folders contains jar files used by my application. The jdbc-drivers contains additional jdbc drivers.  
In gogo.properties file I specify the application classpath (lib/*;jdbc-drivers) and the application class that contains the main method.  
The the classpath entries of your application are separated by ';' character. If the classpath entry ended with /* means that you want to load all jars (recursively) from that directory else that directory is a classes directory.  
With `java -jar gogo.jar` you launch your application. You can see that your main method is called (displaying - Hello from "Demo") after some info messages.

Also, you can use variables in gogo.properties.  
For example:

```
myapp.version= 1.0
app.main.jar=${myapp.version}
vm.myapp.logs=${user.home}/.myapp/logs
```

In above example I declared a variable `myapp.version` and I used this variable to define app.main.jar. Also I declared myapp.logs as vm parameters (accessible in my applicaton as a system property).   
In gogo.properties you can create new variables and you can use any java system properties (user.home in above example).   

License
--------------
Copyright 2013 Decebal Suiu
 
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
the License. You may obtain a copy of the License in the LICENSE file, or at:
 
http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
