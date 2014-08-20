package sample;

import java.sql.*;

/**
 * Created by darko on 17.8.14.
 */
public class DatabaseOperations {

    public String dbName;
    private Connection con;

    public DatabaseOperations(String dbname){
        this.dbName = dbname;
//        createTable();
//        insertData();
        searchNoun("пиперка");
    }

    private void Connect(){
        con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void createTable(){
        Connect();
        Statement stmt = null;
        try {

            stmt = con.createStatement();
            String sql = "CREATE TABLE COMPANY " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " AGE            INT     NOT NULL, " +
                    " ADDRESS        CHAR(50), " +
                    " SALARY         REAL)";
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public void insertData(){
    Connect();
    Statement stmt = null;
    try {
        con.setAutoCommit(false);

        stmt = con.createStatement();
        String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
        stmt.executeUpdate(sql);

        stmt.close();
        con.commit();
        con.close();
    } catch ( Exception e ) {
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.exit(0);
    }
    System.out.println("Records created successfully");
}

    public boolean searchNoun(String noun)
    {
        boolean res = false;
        Connect();
        PreparedStatement stmt;
        ResultSet rs;
        try {
            stmt = con.prepareStatement("select * from nouns_table where derived like ? ");

            int i=1;
            stmt.setString(i++, noun);
            rs = stmt.executeQuery();
            rs.next();
            if(rs.getRow() > 0){
                res = true;
            }
            rs.close();
            stmt.close();
            con.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation noun done successfully");

        return res;
    }

    public boolean searchVerb(String verb)
    {
        boolean res = false;

        Connect();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("select * from verbs_table where derived like ? ");

            int i=1;
            stmt.setString(i++, verb);
            ResultSet rs = stmt.executeQuery();
            rs.next();
           if(rs.getRow() > 0){
               res = true;
           }
            rs.close();
            stmt.close();
            con.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation verb done successfully");
        return res;
    }


}





