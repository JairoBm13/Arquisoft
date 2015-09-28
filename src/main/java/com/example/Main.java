package com.example;

import static com.example.JobExample.sendMail;
import javax.persistence.EntityManager;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import static org.quartz.SimpleScheduleBuilder.repeatHourlyForever;
import org.quartz.Trigger;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory; 
/**
 *
 * This class launches the web application in an embedded Jetty container.
 * This is the entry point to your application. The Java command that is used for
 * launching should fire this main method.
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
//        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//        scheduler.start();
//        JobDetail jobDetail = newJob(JobExample.class).build();
// 
//        Trigger trigger = newTrigger()
//                .startNow()
//                .withSchedule(repeatSecondlyForever(2))
//                .build();
// 
//        scheduler.scheduleJob(jobDetail, trigger);
        
        JobExample.sendMail(5, "j.bautista.m13@gmail.com", "Jairo", "Bautista Mora", "j.bautista.m13@gmail.com");
    }

}
