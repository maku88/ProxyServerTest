//
//package proxy;
//
//import java.sql.*;
//import proxy.Configuration;
//
////HitCounter -  counts number of cache hits and misses
////Continually stores the data in the appropriate database
//public class HitCounter extends Thread
//{
//    public HitCounter (Configuration p_config, Connection p_con)
//    {
//        config = p_config;
//        con = p_con;
//        readFromDB();
//        counter = 0;
//    }
//
//
//    public int getHits()
//    {
//        return hits;
//    }
//
//
//    public int getMisses()
//    {
//        return misses;
//    }
//
//
//    public synchronized void incHits()
//    {
//        ++hits;
//        incCounter();
//    }
//
//
//    public synchronized void incMisses()
//    {
//        ++misses;
//        incCounter();
//    }
//
//
//    public void run()
//    {
//        while (true) {
//            suspend();
//            save();
//        }
//    }
//
//
//    //Saves the current hit statistics in a DB
//    public void save()
//    {
//        int saved_hits, saved_misses;
//
//        synchronized (this) {
//            saved_hits = hits;
//            saved_misses = misses;
//        }
//
//        synchronized (config) {
//            try {
//                PreparedStatement sav = con.prepareStatement(
//                        "UPDATE HitsMisses SET Hits = ?, Misses = ?");
//                sav.setInt(1, saved_hits);
//                sav.setInt(2, saved_misses);
//                sav.executeUpdate();
//            } catch (SQLException sqle) {
//                System.err.println("An SQL-error occured during Hits-Misses table saving:");
//                System.err.println(sqle.getMessage());
//            }
//        }
//    }
//
//
//    //resets the statistics in a DB
//    public synchronized void reset()
//    {
//        hits = misses = 1;
//        try {
//            Statement stmt = con.createStatement();
//            stmt.executeUpdate(
//                    "DELETE FROM HitsMisses");
//            stmt.executeUpdate(
//                    "INSERT INTO HitsMisses VALUES (1, 1)");
//        } catch (SQLException sqle) {
//        }
//    }
//
//
//    //Reads hit statistics from the DB
//    private void readFromDB()
//    {
//        try {
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(
//                    "SELECT Hits, Misses FROM HitsMisses");
//            if (rs.next()) {
//                hits = rs.getInt(1);
//                misses = rs.getInt(2);
//                return;
//            }
//        } catch (SQLException sqle) {
//        }
//        hits = misses = 1;      //On error
//    }
//
//
//    private void incCounter()
//    {
//        if (++counter > SAVING_INTERVAL) {
//            counter = 0;
//            //save();
//            resume();
//        }
//    }
//
//
//
//
//    private int hits;
//    private int misses;
//    private int counter;
//    private Configuration config;
//    private Connection con;
//
//    private final static int SAVING_INTERVAL = 20;
//}