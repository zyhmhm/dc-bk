package top.dc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"top.dc.mapper","top.dc.bk.mapper"})
public class SpringBootRun {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootRun.class);
    }
}
