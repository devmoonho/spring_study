package jpabook.jpashop;

import org.springframework.boot.SpringApplication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hello {
   public static void main(String[] args) {
		Hello hello = new Hello();
		hello.setData("data1");
	
		hello.getData();
		// String data = hello.getData();
		// System.out.println("data : " + data);
	
		
		SpringApplication.run(DemoApplication.class, args);
	}

private String data;
}
