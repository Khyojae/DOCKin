package com.example.DOCKin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DbTest implements CommandLineRunner {
    private final DataSource dataSource;

    public DbTest(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception{
        try(Connection connection = dataSource.getConnection()){
            System.out.println("Db 연결 성공: "+connection.getMetaData().getURL());
        } catch(Exception e){
            System.out.println("실패 원인 : "+e.getMessage());
        }
    }
}
