package ACID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Bank {
    private static String url = "jdbc:mysql://localhost:3306/bank";
    private static  String username = "root";
    private static String password = "9032";
    private static final String SQL_QUERY = "update transactions set balance = balance - ? where accountNumber = ?";
     static Connection connection;
     static PreparedStatement preparedStatement;
    private static final int PIN = 1234;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException c){
            c.printStackTrace();
        }
        try{
            connection = DriverManager.getConnection(url,username,password);
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(SQL_QUERY);
            System.out.println("Enter Account No : ");
            int accNo = scan.nextInt();
            System.out.println("Enter money : ");
            int sal = scan.nextInt();
            System.out.println("Enter PIN : ");
            int PIN1 = scan.nextInt();
            if(PIN == PIN1){
                preparedStatement.setInt(1,sal);
                preparedStatement.setInt(2,accNo);
                int row = preparedStatement.executeUpdate();
                System.out.println(row+" row affected");

                System.out.println("Enter receiver's Account No : ");
                int accNo1 = scan.nextInt();
                preparedStatement.setInt(1,-sal);
                preparedStatement.setInt(2,accNo1);
                int row1 = preparedStatement.executeUpdate();
                System.out.println(row1+" row affected");
                if(row == row1){
                    connection.commit();
                }else{
                    connection.rollback();
                }
            }else{
                System.out.println("PIN is incorrect. Please enter Correct PIN");
            }


        }catch (SQLException s){
            s.printStackTrace();
        }
    }
}
