/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codechallenge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zie
 */
public class CodeChallenge {

    /**
     * @param args the command line arguments
     */
   
   private static final String DB_URL = "jdbc:mysql://localhost:3306/codechallenge";
   //  Database credentials
   private static final String USER = "root";
   private static final String PASS = "";
   private static List<String> logData = new ArrayList<String>();
   
    public static void main(String[] args) throws SQLException, ParseException, IOException {
        Scanner user = new Scanner( System.in ); 
        String  clientsFileName, transactionsFileName;
        createDatabase();
        createTables();
    // prepare the input file
        System.out.print("Input Clients Table File Name: ");
        clientsFileName = user.nextLine().trim();
        System.out.print("Input Transactions Table File Name: ");
        transactionsFileName = user.nextLine().trim();
        processClientTable(clientsFileName);
        processTransactionsTable(transactionsFileName);
        createLogFile(logData);
    }
    public static void createDatabase(){
        Connection conn = null;
        Statement state = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USER, PASS);
            state = conn.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS codechallenge";
            state.executeUpdate(sql);
        } catch(SQLException se){
            se.printStackTrace();;
        } catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(state!=null)
                    state.close();
            } catch(SQLException se2){
            }
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
    public static void createTables() throws SQLException{
        try{
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement create = conn.prepareStatement("CREATE TABLE IF NOT EXISTS clients(_clientid int NOT NULL AUTO_INCREMENT, _clientname varchar(255) NOT NULL, _clientnum varchar(255), _clientaddress varchar(255), _clientsince date, _clientbranch varchar(255), PRIMARY KEY(_clientid))");
            create.executeUpdate();
            create = conn.prepareStatement("CREATE TABLE IF NOT EXISTS transactions (_transactionid int NOT NULL AUTO_INCREMENT, _clientname varchar(255) NOT NULL, _clientid int NOT NULL, _paymode varchar(255), _itemname varchar(255), _netamount double, _vatamount double, _branch varchar(255), _timestamp varchar(255), PRIMARY KEY (_transactionid))");
            create.executeUpdate();
            
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
          System.out.println("Tables Created!");
        }
    }
    public static void processClientTable(String fileName) throws ParseException, IOException{
        String label = "clients";
        List<String> badData = new ArrayList<String>();
        File file = new File(fileName);
        int col = 0;
        try{
            int successEntry=0, errorEntry=0, numEntry=0;
            Scanner inputStream = new Scanner(file);
            String head = inputStream.nextLine();
            String[] value = head.split(",");
            col = value.length;
            while (inputStream.hasNext()){
                String data = inputStream.nextLine();
                String[] tokens = data.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                numEntry++;
                if(col==tokens.length){
                   tokens[2] = removeQuotes(tokens[2]);
                   tokens[3] = dateFormat(tokens[3]);
                   insertClient(tokens);
                   successEntry++;
                } else{
                    badData.add(data);
                    System.out.println("Entry not suitable for table entry.");
                    errorEntry++;
                }
            }
            inputStream.close();
            writeBadRecord(badData, label);
            logData.add("i.  Clients");
            logData.add("     Number of Entries: " + numEntry);
            logData.add("     Successful Entries: " + successEntry);
            logData.add("     Unsuccessful Entries: " + errorEntry);
            System.out.println("Number of Entries: " + numEntry);
            System.out.println("Successful Entries: " + successEntry);
            System.out.println("Unsuccessful Entries: " + errorEntry);
            
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public static void processTransactionsTable(String fileName) throws ParseException, SQLException, IOException{
        String label = "transactions";
        List<String> badData = new ArrayList<String>();
        File file = new File(fileName);
        int col = 0;
        try{
            int successEntry=0, errorEntry=0, numEntry=0;
            Scanner inputStream = new Scanner(file);
            String head = inputStream.nextLine();
            String[] value = head.split(",");
            col = value.length;
            while (inputStream.hasNext()){
                String data = inputStream.nextLine();
                String[] tokens = data.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                numEntry++;
                if(col==tokens.length){
                   tokens[3] = removeQuotes(tokens[3]);
                   tokens[6] = removeQuotes(tokens[6]);
                   insertTransaction(tokens);
                   successEntry++;
                } else{
                    badData.add(data);
                    System.out.println("Entry not suitable for table entry.");
                    errorEntry++;
                }
            }
            inputStream.close();
            writeBadRecord(badData, label);
            logData.add("ii.  Transactions");
            logData.add("     Number of Entries: " + numEntry);
            logData.add("     Successful Entries: " + successEntry);
            logData.add("     Unsuccessful Entries: " + errorEntry);
            System.out.println("Number of Entries: " + numEntry);
            System.out.println("Successful Entries: " + successEntry);
            System.out.println("Unsuccessful Entries: " + errorEntry); 
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public static void createLogFile(List<String> data) throws IOException {
        String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String logFileName = "logfile_" + time + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName));
        for(String values: data){
            writer.write(values);
            writer.newLine();
        }
        writer.close();
    }
    public static void insertClient(String[] entry){
        try{
            Connection conn;
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement ps = conn.prepareStatement("INSERT INTO clients (_clientname, _clientnum, _clientaddress, _clientsince, _clientbranch) VALUES ('"
                    + entry[0] + "', '"
                    + entry[1] + "', '"
                    + entry[2] + "', '"
                    + entry[3] + "', '"
                    + entry[4] + "')");
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch(SQLException e){
            System.out.println(e);
        }
    }
    public static void insertTransaction(String[] entry) throws SQLException{
        try{
            int clientID = 0;
            clientID = findClientId(entry[0]);
            Connection conn;
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Double netamount = Double.valueOf(entry[3].replace(",", ""));
            Double vatamount = Double.parseDouble(entry[4].replace(",", ""));
            PreparedStatement ps = conn.prepareStatement("INSERT INTO transactions (_clientname, _clientid, _paymode, _itemname, _netamount, _vatamount, _branch, _timestamp) VALUES ('"
                    + entry[0] + "', "
                    + clientID + ", '"
                    + entry[1] + "', '"
                    + entry[2] + "', '"
                    + netamount + "', '"
                    + vatamount + "', '"
                    + entry[5] + "', '"
                    + entry[6] + "')");
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch(SQLException e){
            System.out.println(e);
        }
    }
    public static int findClientId(String name){
        try{
            int x=0;
            Connection conn;
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement ps = conn.prepareStatement("SELECT _clientid FROM clients WHERE _clientname='" + name + "'");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                 x = rs.getInt("_clientid");
            }
            rs.close();
            ps.close();
            conn.close();
            return x; 
        } catch(SQLException e){
            System.out.println(e);
        }
        return 0;
    }
    public static String dateFormat(String date) throws ParseException{
        date = removeQuotes(date);
        String pattern = "MMMM dd, yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        java.util.Date formattedDate = formatter.parse(date);
        java.sql.Date sqlDate = new java.sql.Date(formattedDate.getTime());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = df.format(sqlDate);
        return newDate;
    }
    public static String removeQuotes(String str){
        str = str.replace("\"", "");
        return str;
    }
    public static void writeBadRecord(List<String> data, String label) throws IOException{
        String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String badFileName = "bad-record_" + label + "_" + time + ".csv";
        BufferedWriter writer = new BufferedWriter(new FileWriter(badFileName));
        for(String values: data){
            writer.write(values);
            writer.newLine();
        }
        writer.close();   
    }
}

   
