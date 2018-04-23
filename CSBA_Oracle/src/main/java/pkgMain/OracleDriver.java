package pkgMain;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import util.HibernateUtil;

public class OracleDriver {
	
	public static void main(String[] args) {
		
		try {
			DriverManager.getConnection("jdbc:oracle:thin:@cisc437.cppycbjtmhb4.us-east-1.rds.amazonaws.com:1521:ORCL","ProjectSpring2018","ProjectSpring2018");
			System.out.println("Connection Successful!");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
