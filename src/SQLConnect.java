
import java.sql.*;
import java.util.ArrayList;

public class SQLConnect {
    static final String USERNAME="root";
    static final String PASSWORD="root";
    public static void main(String args[]){
        String name;
        String source;

        //Insert Name and source.
        name="Athanasios";
        source="'c:dowloads:photo1'";

        InsertPreStatement(name,source); //contains a print table method

        //Update method -> update the photo urls of a user with the name ()
        name="Ioannis";
        source="C:IOANNIS/DESKTOP";
        Update(name,source);

        //Delete method. prividing the name of the user will get the id and get the photos deleted.
        name="Athanasios";
        Delete(name);

    }


    public static void InsertPreStatement(String name , String source) {
        int id=0;
        int photoId=0;
        try
    {
        Connection con=DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb2",USERNAME,PASSWORD);  // establishing connection with db
        PreparedStatement pst = con.prepareStatement("insert into user (name) values (?)");
        pst.setString(1,name);
        pst.executeUpdate();

        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("select id from user where name like '"+name+"' LIMIT 1");
        while (rs.next())
            id= rs.getInt("id");

        pst = con.prepareStatement("insert into photo (source,user_id) values (?,?)");
        pst.setString(1, source);
        pst.setInt(2,id);
        pst.executeUpdate();

        rs=stmt.executeQuery("select id from photo where user_id ="+String.valueOf(id)+" LIMIT 1");
        while (rs.next())
            photoId= rs.getInt("id");
        pst = con.prepareStatement("update user set photoId= ? where id=?;");
        pst.setInt(1,photoId);
        pst.setInt(2,id);
        pst.executeUpdate();

        System.out.println("\nValues Inserted. The Table is Updated ");
        PrintTable();

        con.close();
    }
    catch (Exception e)
    {
        System.err.println("Got an exception!");
        System.err.println(e.getMessage());
    }

    }

   /* public static void InsertStatement( String Name) {

        try
        {
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",USERNAME,PASSWORD);  // establishing connection with db


            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO city (cityName) VALUES ('"+Name+"')");

            System.out.println("\nValues Inserted. The Table is Updated ");
            PrintTable();

            con.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

    }*/

    public static void PrintTable(){
        try{
        Connection con=DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb2",USERNAME,PASSWORD);

        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("select * from user join photo on user.id=photo.user_id");
        System.out.println("\nThe current Table contains: ");

        while (rs.next())
            System.out.println(rs.getString("name")+ " "+rs.getString("source")+" \n");


        con.close();
    }catch(Exception e){ System.out.println(e);}



    }
    public static void  Update(String name, String source){
        int id=0;

        try{
            Connection con=DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb2",USERNAME,PASSWORD);  // establishing connection with db
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select id from user where name like '"+name+"' LIMIT 1");
            while (rs.next())
                id= rs.getInt("id");

            PreparedStatement st = con.prepareStatement("UPDATE photo set source= ? where user_id=(?)");
            st.setString(1, source);
            st.setInt(2, id);
            st.executeUpdate();

            System.out.println("\nDatabase updated successfully ");
            PrintTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void Delete(String name){
        int id=0;
        int photoId=0;
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb2", USERNAME,PASSWORD);  // establishing connection with db
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select id from user where name like '"+name+"' LIMIT 1");
            while (rs.next())
                id= rs.getInt("id");        //getting the id of user

            rs=stmt.executeQuery("select id from photo where user_id = '"+id+"' LIMIT 1");
            while (rs.next())
                photoId= rs.getInt("id"); //using user_id to get the photo id

            PreparedStatement pst = con.prepareStatement("DELETE FROM photo WHERE id = ? ;");
            pst.setInt(1, photoId);     // must delete photo first in order to delete user
            pst.executeUpdate();

            pst = con.prepareStatement("DELETE FROM user WHERE id = ? ;");
            pst.setInt(1, id);
            pst.executeUpdate();





            System.out.println("\nDeletion successful");
            PrintTable();

        }catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

    }

}
