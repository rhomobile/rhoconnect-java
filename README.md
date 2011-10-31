rhoconnect-java
===

Rhoconnect-java library is designed for the [Rhoconnect](http://rhomobile.com/products/rhosync) App Integration Server.

Using Rhoconnect-java plugin, your [Spting 3 MVC](http://www.springsource.org/) application's data will transparently synchronize with a mobile application built on the [Rhodes framework](http://rhomobile.com/products/rhodes), or any of the available [Rhoconnect clients](http://rhomobile.com/products/rhosync/).

## Getting started

For testing and evaluation purposes you might want to use [RhoconnectJavaSample](https://github.com/shurab/RhoconnectJavaSample) application.

### Add to your maven project the following dependencies:

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

### Update your servlet xml configuration file and include rhoconnect-java metadata (packages, converters, and beans): 

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

### Add Add rhoconnect-java jar to maven 2 build classpath:

    <dependency>
        <groupId>rhoconnect-java</groupId>
        <artifactId>rhoconnect-java</artifactId>
        <version>0.0.1</version>
        <scope>system</scope>
        <systemPath>/path-to-jar-directory/rhoconnect-java-0.0.1.jar</systemPath> <!-- Set yuor path to rhoconnect-java jar -->
    </dependency>


### Implement Rhoconnect interface:

    package com.rhomobile.rhoconnect;
    import java.util.Map;

    public interface Rhoconnect {
        boolean authenticate(String login, String password, Map<String, Object> attribures);
	    Map<String, Object> query_objects(String resource, String partition);
	    Integer create(String resource, String partition, Map<String, Object> attributes);
	    Integer update(String resource, String partition, Map<String, Object> attributes);
	    Integer delete(String resource, String partition, Map<String, Object> attributes);
    }

For example, RhoconnectJavaSample application implementation is based on contactService API:

    /**
    * RhoconnectImpl.java 
    */
    package com.rhomobile.rhoconnect;

    import java.util.HashMap;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Map;

    import org.apache.commons.beanutils.BeanUtils;
    import org.springframework.beans.factory.annotation.Autowired;

    import com.rhomobile.contact.form.Contact;
    import com.rhomobile.contact.service.ContactService;

    public class RhoconnectImpl implements Rhoconnect {

        @Autowired
        private ContactService contactService;	

        @Override
        public boolean authenticate(String login, String password, Map<String, Object> attribures) {
            // TODO: your authentication code goes here ...
            return true;
        }
	
        @Override
        public Map<String, Object> query_objects(String resource, String partition) {
            Map<String, Object> h = new HashMap<String, Object>();
            List<Contact> contacts =  contactService.listContact();		
            Iterator<Contact> it = contacts.iterator( );
            while(it.hasNext()) {
                Contact c =(Contact)it.next();
          	    h.put(c.getId().toString(), c);
            }
            return h;
        }

        @Override
        public Integer create(String resource, String partition, Map<String, Object> attributes) {
            Contact contact = new Contact();
            try {
                BeanUtils.populate(contact, attributes);
                int id = contactService.addContact(contact);
                return id;
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Integer update(String resource, String partition, Map<String, Object> attributes) {
            Integer id = Integer.parseInt((String)attributes.get("id"));
            Contact contact = contactService.getContact(id);
            try {
                BeanUtils.populate(contact, attributes);
    		    contactService.updateContact(contact);
    		    return id;
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Integer delete(String resource, String partition, Map<String, Object> attributes) {
            String objId = (String)attributes.get("id");
            Integer id = Integer.parseInt(objId);
            contactService.removeContact(id);		
            return id;
        }
    }


## Meta
Created and maintained by Alexander Babichev.

Released under the [MIT License](http://www.opensource.org/licenses/mit-license.php).

