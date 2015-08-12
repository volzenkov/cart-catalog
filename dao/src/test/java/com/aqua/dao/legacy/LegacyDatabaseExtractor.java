package com.aqua.dao.legacy;


import com.aqua.dao.CommonDAO;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public abstract class LegacyDatabaseExtractor {

    @Autowired
    protected CommonDAO commonDAO;

    protected Connection initDatasource() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.out.println("MySQL JDBC Driver Registered!");

        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/aqua_source", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    interface DbStatementWorker {

        public void doWork(Statement statement) throws SQLException;

        public void handleError(Exception e);
    }

    protected void handleStatement(DbStatementWorker dbStatementWorker) throws Exception {

        Connection dbConnection = null;
        Statement statement = null;
        try {
            dbConnection = initDatasource();
            statement = dbConnection.createStatement();
            dbStatementWorker.doWork(statement);
        } catch (SQLException e) {
            dbStatementWorker.handleError(e);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

}
