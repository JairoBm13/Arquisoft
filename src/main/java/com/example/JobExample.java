/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.example.models.Competitor;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author je.bautista10
 */
public class JobExample implements Job{
    
    private static final String USERNAME = "j.bautista.m13@gmail.com";
    private static final String PASSWORD = "emi13021996j13";

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        int competitors = getNumberCompetitors();
	Competitor comp = getWinnerCompetitors();
        sendMail(competitors, comp, "j.bautista.m13@gmail.com", "Jairo", "Bautista Mora", "j.bautista.m13@gmail.com");
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
    
    public Competitor getWinnerCompetitors(){
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("CronPU", System.getProperties());
	EntityManager entityManager = emf.createEntityManager();
	List<Competitor> competitors;
        Competitor comp;
        try {
            Query q = entityManager.createQuery("select u from Competitor u order where u.winner=FALSE by u.surname ASC");
            competitors = q.getResultList();
            comp = competitors.get((int)(Math.random()*competitors.size()));
            comp.setWinner(true);
            Query q2 = entityManager.createNamedQuery("update Competitor SET winner=TRUE where id="+comp.getId());
            q2.executeUpdate();
            emf.close();
            return comp;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.clear();
            entityManager.close();
        }  
        return null;
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
 
public static void sendMail(int numberCompetitors,Competitor comp, String from, String recipentName, String recipentLastName, String recipentEmail){
	Properties props = new Properties();
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.host", "smtp.gmail.com");
	props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USERNAME));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("j.bautista.m13@gmail.com"));
			message.setSubject("Competidores");
			message.setText("La cantidad de concursantes inscritos son: " + numberCompetitors
                        + "\n El ganador fue " + comp.getName() + " con id "+comp.getId());

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    } 
}
