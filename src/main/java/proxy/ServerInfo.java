//package proxy;
//
//
//
///*
// *
// * ServerInfo - stores information bound to a remote
// *              HTTP server:
// *              1. server name;
// *              2. average connection time;
// *              3. average bandwidth;
// *
// */
//
//public class ServerInfo
//{
//
//    public ServerInfo() {
//        serverName = "";
//        averageConnectionTime = 10.0;
//        averageBandWidth = 10.0;
//    }
//
//
//    public void initialize (java.sql.ResultSet rs)
//            throws java.sql.SQLException
//    {
//        serverName = rs.getString(START_INDEX);
//        averageConnectionTime = rs.getDouble(START_INDEX + 1);
//        averageBandWidth = rs.getDouble(START_INDEX + 2);
//    }
//
//
//    public boolean isInitialized() {
//        return averageBandWidth != 0.;
//    }
//
//
//
//    public double getAverageConnectionTime()
//    {
//        return averageConnectionTime;
//    }
//
//
//    public double getAverageBandWidth()
//    {
//        return averageBandWidth;
//    }
//
//
//    private String serverName;
//    private double averageConnectionTime;
//    private double averageBandWidth = 0.;
//
//    private final static String delimiter = " ";
//    private final static int START_INDEX = 7;
//
//
//}
