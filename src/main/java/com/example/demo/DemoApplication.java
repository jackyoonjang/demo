package com.example.demo;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.repository.RoleDao;
import com.example.demo.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {


	// main메소드는 Spring이 관리 안한다.
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	RoleDao roleDao;

	@Autowired
	UserDao userDao;

	@Override
	public void run(String... args) throws Exception {

		User user = new User();
//		user.setName("홍길동");
//		user.setPassword("123456");
//		user.setEmail("hong@gmail.com");
//		boolean b = userDao.addUser(user);
//		System.out.println(b);

//		boolean b = userDao.deleteUser(3);
//		System.out.println(b);

		User user1 = userDao.getUser(1);
		if(user1 != null) {
			System.out.println(user1.toString());
		}

		System.out.println("-----");

		List<User> allUsers = userDao.getAllUsers();
		for (User users : allUsers){
			System.out.println(users.toString());
		}

	}
}
