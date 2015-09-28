/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.example.models.Competitor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.mail.Message;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author je.bautista10
 */
public class JobExample implements Job{
    
    private static final String USERNAME = "j.bautista.m13@outlook.com";
    private static final String PASSWORD = "emi13021996j1310";

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
	sendMail(5, "j.bautista.m13@gmail.com", "Jairo", "Bautista Mora", "j.bautista.m13@gmail.com");
}
    
    public int getNumberCompetitors(){
	int numberCompetitors=0;
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("CronPU", System.getProperties());
	EntityManager entityManager = emf.createEntityManager();
	List<Competitor> competitors;
        try {
            Query q = entityManager.createQuery("select u from Competitor u order by u.surname ASC");
            competitors = q.getResultList();
            numberCompetitors=competitors.size();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
		emf.close();
		return numberCompetitors;
    }
    
    /**
 * Envía un correo electrónico notificando el número de competidores en 
 * la base de datos de Competitors, aplicación desplegada en Heroku. El 
 * timing de la notificación, es independiente a este método.
 * <p>
 * El mensaje enviado, es genérico, se requiere mantenimiento para
 * aumentar usabilidad del código.
 *
 * @param  numberCompetitors el número a notificar al administrador
 * @param  from el email principal de la aplicación, desde aquí se envían los correos
 * @param  recipentName el nombre del administrador del sistema
 * @param  recipentLastName el apellido del administrador del sistema  
 * @param  recipentEmail el email del administrador del sistema
 */
 
public static void sendMail(int numberCompetitors, String from, String recipentName, String recipentLastName, String recipentEmail){
	final Email email = new Email();
	email.setFromAddress("Admin", from);
	email.setSubject("Numero de Concursantes");
	email.addRecipient(recipentName + " " + recipentLastName, recipentEmail, Message.RecipientType.TO);
	email.setText("La cantidad de concursantes inscritos son: " + numberCompetitors);
 
	// USERNAME es una constante con el username de Gmail
	// PASSWORD es una constante con la contraseña de correo de la cuenta correspondiente al email de from
	new Mailer("smtp.outlook.com", 587, USERNAME, PASSWORD, TransportStrategy.SMTP_TLS).sendMail(email);
//        String host = "localhost";
//int port = 443;
//String user = "BruceWayne@example.org";
//String password = "S3cr3tP4ss";
//Session session = Session.getDefaultInstance(props);
//session.setDebug(true);
//
//Message msg = new MimeMessage(session);
//
//msg.setFrom(new InternetAddress(user, "Dark Knight"));
//msg.setSubject("Hello Selina");
//msg.setText("Do you want to have diner ?");
//Transport transport = session.getTransport("smtp");
//transport.connect(host, port, user, password);
//transport.sendMessage(msg, msg.getAllRecipients());
} 
}
