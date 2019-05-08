package com.example.demo.jpa.b;

import com.example.demo.service.impl.TestServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author yang chen
 * @date 2019/4/17 16:29
 */
@Controller
public class Test123 {

    @Resource
    private TestServiceImpl testServiceImpl;

    @RequestMapping("/test123")
    @ResponseBody
    public String sss (){

      return testServiceImpl.getDatabase();
    }
}
