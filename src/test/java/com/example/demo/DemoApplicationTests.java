package com.example.demo;

import com.example.demo.jpa.b.Test123;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Resource
	private Test123 test;

	@Test
	public void contextLoads() {
		test.sss();
	}

}
