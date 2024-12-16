package Basics;

import java.sql.*;
import java.util.Scanner;

public class User {

    private static final String SQL_QUERY = "select * from employee_details";

    private static Connection con;
    private static Statement statement;
    private static ResultSet resultSet;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        LoadDriver.load();
        con=Connect.dbConnection();

        try{
            //Creating Statement
            statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,0);
            //Executing Query
            resultSet = statement.executeQuery(SQL_QUERY);

        }catch (SQLException s){
            s.printStackTrace();
        }

        //Fetching Results
        System.out.println("------------------------------------------");
        try{
            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String designation = resultSet.getString(3);
                int salary = resultSet.getInt(4);
                String location = resultSet.getString(5);
                System.out.printf("| %d | %-9s | %-15s | %d | %-10s |\n",id,name,designation,salary,location);
            }
        }catch (SQLException s){
            s.printStackTrace();
        }
        System.out.println("-------------------------------------------");
        resultSet.first();

        System.out.printf("| %d | %-9s | %-7s | %d | %-10s |\n",resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getInt(4),resultSet.getString(5));

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for(int i=1;i<=resultSetMetaData.getColumnCount();i++){
            System.out.println("Column Name : "+resultSetMetaData.getColumnName(i));
            System.out.println("Column DataType : "+resultSetMetaData.getColumnTypeName(i));
            System.out.println("Column DataType range : "+resultSetMetaData.getColumnType(i));
            System.out.println();
        }

        //For Inserting and Updating use executeUpdate() -> DML
//        String SQL_INSERT = "insert into employee_details values (4,'Shyam','Java Developer',15000,'Bengaluru')";
//        int res = statement.executeUpdate(SQL_INSERT);
//        System.out.println(res + " rows affected");
//
//        System.out.println();

        //DB will crash if we hit DB with more Query to overcome this use Batch
//        String SQL_INSERT1 = "insert into employee_details values (8,'Sha','Java Developer',25000,'Bengaluru')";
//        String SQL_INSERT2 = "insert into employee_details values (9,'Dhu','Automation Tester',13000,'Chennai')";
//        String SQL_INSERT3 = "insert into employee_details values (10,'Dha','Python Developer',45000,'Hyderabad')";
//
//        statement.addBatch(SQL_INSERT1);
//        statement.addBatch(SQL_INSERT2);
//        statement.addBatch(SQL_INSERT3);
//
//        int[] res = statement.executeBatch();
//        for(int i : res){
//            System.out.println(i +" row affected");
//        }

        //PreparedStatement
        String QUERY = "insert into employee_details values (?,?,?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(QUERY);
        Scanner scan = new Scanner(System.in);
        String s=null;
        do{
            try{
                System.out.println("Enter Id : ");
                int id = scan.nextInt();
                scan.nextLine();
                System.out.println("Enter Name : ");
                String name = scan.nextLine();
                System.out.println("Enter Designation : ");
                String designation = scan.nextLine();
                System.out.println("Enter Salary : ");
                int salary = scan.nextInt();
                scan.nextLine();
                System.out.println("Enter Location : ");
                String location = scan.nextLine();

                preparedStatement.setInt(1,id);
                preparedStatement.setString(2,name);
                preparedStatement.setString(3,designation);
                preparedStatement.setInt(4,salary);
                preparedStatement.setString(5,location);

                preparedStatement.addBatch();
                System.out.println("Do you want add more Employees? Enter if (Yes) or (No)");
                s = scan.next();
            }catch (SQLException sql){
                sql.printStackTrace();
            }
        }while (s.equalsIgnoreCase("yes"));

        preparedStatement.executeUpdate();


    }

}
