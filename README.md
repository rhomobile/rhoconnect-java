rhoconnect-java
===

Rhoconnect-java library is designed for the [Rhoconnect](http://rhomobile.com/products/rhosync) App Integration Server.

Using Rhoconnect-java plugin, your [Spting 3 MVC](http://www.springsource.org/) application's data will transparently synchronize with a mobile application built on the [Rhodes framework](http://rhomobile.com/products/rhodes), or any of the available [Rhoconnect clients](http://rhomobile.com/products/rhosync/).

## Getting started

For testing and evaluation purposes you might want to use [RhoconnectJavaSample](https://github.com/shurab/RhoconnectJavaSample) application.

1. Add to your maven project the following dependencies:

    <!-- apache commons beanutils -->
    <dependency>
        <groupId>commons-beanutils</groupId>
        <artifactId>commons-beanutils</artifactId>
        <version>1.8.3</version>
    </dependency>   
	<!-- Jackson JSON Mapper -->
    <dependency>
		<groupId>org.codehaus.jackson</groupId>
		<artifactId>jackson-mapper-asl</artifactId>
		<version>1.9.0</version>
		<type>jar</type>
		<optional>false</optional>
	</dependency>
	
2. Update your servlet xml configuration file and include rhoconnect-java configuration metadata (packages, converters, and beans): 

	<!-- rhoconnect-java plugin packages -->
 	<context:component-scan base-package="com.rhomobile.rhoconnect.controller" /> 

 	<!-- rhoconnect-java plugin converters -->
 	<bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter">
     	<property name="order" value="1" />
     	<property name="messageConverters">
         	<list>
             	<ref bean="stringHttpMessageConverter"/>
             	<ref bean="jsonConverter" />
         	</list>
     	</property>
 	</bean>
 	<bean id="jsonConverter" class="org.springframework.http.converter.json.MappingJacksonHttpMessageConverter">
     	<property name="supportedMediaTypes" value="application/json" />
 	</bean>
 	<bean id="stringHttpMessageConverter" class="org.springframework.http.converter.StringHttpMessageConverter">
 	</bean>
 	<bean id="restTemplate" class="org.springframework.web.client.RestTemplate">
     	<property name="messageConverters">
         	<list>
             	<ref bean="jsonConverter" />
             	<ref bean="stringHttpMessageConverter"/>
         	</list>
     	</property>
 	</bean>    

 	<!-- rhoconnect-java plugin beans -->
 	<bean id="rhoconnect" class = "com.rhomobile.rhoconnect.RhoconnectImpl" />
 	<bean id="rhoconnectClient" class = "com.rhomobile.rhoconnect.RhoconnectClient" init-method="setAppEndpoint" >
     	<property name="restTemplate"><ref bean="restTemplate"/></property>
     	<property name="endpointUrl" value="http://localhost:9292" />
     	<property name="appEndpoint" value="http://localhost:8080/contacts" />
     	<property name="apiToken" value="sometokenforme" />
 	</bean>


## Meta
Created and maintained by Alexander Babichev.

Released under the [MIT License](http://www.opensource.org/licenses/mit-license.php).

