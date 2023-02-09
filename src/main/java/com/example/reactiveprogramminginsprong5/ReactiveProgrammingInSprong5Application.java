package com.example.reactiveprogramminginsprong5;

import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication
public class ReactiveProgrammingInSprong5Application {

    public static void main(String[] args) {

//        GenericApplicationContext context =
//                new GenericApplicationContext();
//        new XmlBeanDefinitionReader(context)
//                .loadBeanDefinitions("services.xml");
//        new GroovyBeanDefinitionReader(context)
//                .loadBeanDefinitions("services.groovy");
//        new PropertiesBeanDefinitionReader(context)
//                .loadBeanDefinitions("services.properties");
//        context.refresh();

        SpringApplication.run(ReactiveProgrammingInSprong5Application.class, args);


    }

}
