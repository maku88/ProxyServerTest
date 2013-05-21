//package proxy;
//
//import java.io.*;
//import java.sql.*;
//import proxy.HitCounter;
//import proxy.RequestCountIterator;
//import proxy.URLCacher;
//
///*
// *
// * Configuration -  the runtime parameters and references
// *                  to the attached assistant services.
// *                  Between the parameters are those which
// *                  affect on the cache performance
// *                  Contains routines initializing the environment
// *                  in the execution start
// *
// */
//
//public class Configuration
//{
//
//    private Connection con;
//    private long fileNameEnumerator;
//    private java.util.Vector fileNumberVector;
//
//    private String cacheDirectory;              //explicitely given
//    private long cacheHighWaterMark;            //explicitely given
//    private long cacheLowWaterMark;             //explicitely given
//    private double serverInfoSmoothingFactor;   //explicitely given
//
//    private double weightConnectionTime;        //explicitely given
//    private double weightBandWidth;             //explicitely given
//    private double weightReferencesFrequency;   //explicitely given
//    private double weightSize;                  //explicitely given
//
//    private String serversAccessibleWOProxyBackupFile;  //explicitely given
//
//    private boolean alternativeProxyOn;     //explicitely given
//    private String alternativeProxyName;    //explicitely given
//    private int alternativeProxyPort;       //explicitely given
//
//    private HitCounter hitCounter;
//    private RequestCountIterator requestCountIterator;
//    private URLCacher urlCacher;
//
//
//    public Configuration()
//    {
//        //RandomAccessFile in = null;
//
//        try {
//            //in = new RandomAccessFile("proxy.ini","r");
//            //String s = in.readLine();
//            //in.close();
//            //StringTokenizer st = new StringTokenizer(s);
//
//            String  url = "jdbc:odbc:ActiveProxyDatabaseConfiguration";
//            con = DriverManager.getConnection(url,
//                    "active", "proxy");
//            System.out.println("------------");
//
//            Statement get_init = con.createStatement();
//            String query = "SELECT cacheDirectory, cacheHighWaterMark, " +
//                    "cacheLowWaterMark, serverInfoSmoothingFactor, " +
//                    "weightConnectionTime, weightBandWidth, " +
//                    "weightReferencesFrequency, weightSize, " +
//                    "serversAccessibleWOProxyBackupFile, " +
//                    "alternativeProxyOn, alternativeProxyName, " +
//                    "alternativeProxyPort " +
//                    "FROM Initial_Parameters";
//            ResultSet rs = get_init.executeQuery(query);
//
//            if (rs.next()) {
//                //proxyBroughtDownProperly = (new Boolean(st.nextToken())).booleanValue();
//                //fileNameEnumerator = (new Integer(st.nextToken())).intValue();
//                cacheDirectory = rs.getString(1);
//                cacheHighWaterMark = rs.getLong(2);
//                System.out.println("------------2");
//                cacheLowWaterMark = rs.getLong(3);
//                System.out.println("------------3");
//                //cacheCurrentWaterMark = (new Long(st.nextToken())).longValue();
//                //cacheBackupFile = st.nextToken();
//                //serverDataBaseBackupFile = st.nextToken();
//                serverInfoSmoothingFactor = rs.getDouble(4);
//                System.out.println("------------4");
//                weightConnectionTime = rs.getDouble(5);
//                weightBandWidth = rs.getDouble(6);
//                weightReferencesFrequency = rs.getDouble(7);
//                weightSize = rs.getDouble(8);
//                serversAccessibleWOProxyBackupFile = rs.getString(9);
//                alternativeProxyOn = rs.getBoolean(10);
//                alternativeProxyName = rs.getString(11);
//                alternativeProxyPort = rs.getInt(12);
//                //initServerBroughtDownProperlyParameter(in);
//            }
//            else {
//                System.err.println("The configuration database is empty " +
//                        "or mistaken. The default values are taken.");
//                defaultInit();
//            }
//        } catch (SQLException sqle) {
//            System.err.println("An error occured with the configuration database:");
//            System.err.println(sqle.getMessage());
//            System.err.println("The default values are taken.");
//            defaultInit();
//        } finally {
//            System.out.println("configuration read");
//            setCacheDirectory(cacheDirectory);
//            setFileNumberVector(cacheDirectory);
//            hitCounter = new HitCounter(this, con);
//            hitCounter.start();
//            requestCountIterator = new RequestCountIterator();
//            requestCountIterator.start();
//            urlCacher = new URLCacher(this);
//            urlCacher.start();
//        }
//    }
//
//
//    public Configuration (Connection p_con)
//            throws SQLException
//    {
//        con = p_con;
//        defaultInit();
//        setCacheDirectory(cacheDirectory);
//        hitCounter = new HitCounter(this, con);
//    }
//
//
//    private void defaultInit()
//    {
//        //proxyBroughtDownProperly = false;
//        fileNameEnumerator = 10;
//        cacheDirectory = "cache";
//        cacheHighWaterMark = 200000;
//        cacheLowWaterMark = 100000;
//        serverInfoSmoothingFactor = 0.125;
//        weightConnectionTime = 0.03;
//        weightBandWidth = 25000;
//        weightReferencesFrequency = 1;
//        weightSize = 1;
//        serversAccessibleWOProxyBackupFile = "serverWOProxy.bak";
//        alternativeProxyOn = false;
//        alternativeProxyName = "";
//        alternativeProxyPort = 0;
//    }
//
//
//    public void saveAllValues (boolean toOverwrite)
//            throws SQLException
//    {
//        PreparedStatement pstmt;
//        if (toOverwrite)
//            pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET " +
//                            "cacheDirectory = ?, cacheHighWaterMark = ?, " +
//                            "cacheLowWaterMark = ?, serverInfoSmoothingFactor = ?, " +
//                            "weightConnectionTime = ?, weightBandWidth = ?, " +
//                            "weightReferencesFrequency = ?, weightSize = ?, " +
//                            "serversAccessibleWOProxyBackupFile = ?, " +
//                            "alternativeProxyOn = ?, alternativeProxyName = ?, " +
//                            "alternativeProxyPort = ? ");
//        else
//            pstmt = con.prepareStatement(
//                    "INSERT INTO Initial_Parameters VALUES " +
//                            "( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )");
//        pstmt.setString(1, cacheDirectory);
//        pstmt.setDouble(2, (double) cacheHighWaterMark);
//        pstmt.setDouble(3, (double) cacheLowWaterMark);
//        pstmt.setDouble(4, serverInfoSmoothingFactor);
//        pstmt.setDouble(5, weightConnectionTime);
//        pstmt.setDouble(6, weightBandWidth);
//        pstmt.setDouble(7, weightReferencesFrequency);
//        pstmt.setDouble(8, weightSize);
//        pstmt.setString(9, serversAccessibleWOProxyBackupFile);
//        pstmt.setBoolean(10, alternativeProxyOn);
//        pstmt.setString(11, alternativeProxyName);
//        pstmt.setInt(12, alternativeProxyPort);
//        pstmt.executeUpdate();
//
//        hitCounter.save();
//        System.out.println("The current values are  successfully saved");
//    }
//
//
//    public void shutDown (boolean dummy) {
//        try {
//            saveAllValues(true);
//            con.close();
//        } catch (SQLException e) {}
//    }
//
//
//
//    public synchronized String getNextFileName()
//    {
//        return(new Long(++fileNameEnumerator)).toString();
//    }
//
//
//    public String getCacheDirectory()
//    {
//        return cacheDirectory;
//    }
//
//    public long getCacheHighWaterMark()
//    {
//        return cacheHighWaterMark;
//    }
//
//    public long getCacheLowWaterMark()
//    {
//        return cacheLowWaterMark;
//    }
//
//    public long getCacheCurrentWaterMark()
//    {
//        return 0;
//    }
//
//
//    public double getServerInfoSmoothingFactor()
//    {
//        return serverInfoSmoothingFactor;
//    }
//
//    public double getWeightConnectionTime()
//    {
//        return weightConnectionTime;
//    }
//
//    public double getWeightBandWidth()
//    {
//        return weightBandWidth;
//    }
//
//    public double getWeightReferencesFrequency()
//    {
//        return weightReferencesFrequency;
//    }
//
//    public double getWeightSize()
//    {
//        return weightSize;
//    }
//
//    public String getServersAccessibleWOProxyBackupFile()
//    {
//        return serversAccessibleWOProxyBackupFile;
//    }
//
//    public boolean alternativeProxyOn()
//    {
//        return alternativeProxyOn;
//    }
//
//    public String getAlternativeProxyName()
//    {
//        return alternativeProxyName;
//    }
//
//    public int getAlternativeProxyPort()
//    {
//        return alternativeProxyPort;
//    }
//
//
//    public java.util.Vector getFileNumberVector() {
//        return fileNumberVector;
//    }
//
//
//    public HitCounter getHitCounter()
//    {
//        return hitCounter;
//    }
//
//
//    public RequestCountIterator getRequestCounter() {
//        return requestCountIterator;
//    }
//
//
//    public URLCacher getCacher()
//    {
//        return urlCacher;
//    }
//
//    public void setCacheDirectory(String dir)
//    {
//        cacheDirectory = dir;
//
//        File directory = new File(cacheDirectory);
//        if (!directory.exists() ||
//                (directory.exists() && !directory.isDirectory()))
//            try {
//                directory.mkdir();
//                PreparedStatement pstmt = con.prepareStatement(
//                        "UPDATE Initial_Parameters SET cacheDirectory = ?");
//                pstmt.setString(1, cacheDirectory);
//                pstmt.executeUpdate();
//            } catch (Exception e) {}
//    }
//
//
//    public void setCacheHighWaterMark(long val)
//    {
//        cacheHighWaterMark = val;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET cacheHighWaterMark = ?");
//            pstmt.setDouble(1, (double) val);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//    public void setCacheLowWaterMark(long val)
//    {
//        cacheLowWaterMark = val;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET cacheLowWaterMark = ?");
//            pstmt.setDouble(1, (double) val);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//    public void setServerInfoSmoothingFactor(double val)
//    {
//        serverInfoSmoothingFactor = val;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET serverInfoSmoothingFactor = ?");
//            pstmt.setDouble(1, val);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//    public void setWeightConnectionTime(double val)
//    {
//        weightConnectionTime = val;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET weightConnectionTime = ?");
//            pstmt.setDouble(1, val);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//    public void setWeightBandWidth(double val)
//    {
//        weightBandWidth = val;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET weightBandWidth = ?");
//            pstmt.setDouble(1, val);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//    public void setWeightReferencesFrequency(double val)
//    {
//        weightReferencesFrequency = val;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET weightReferencesFrequency = ?");
//            pstmt.setDouble(1, val);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//    public void setWeightSize(double val)
//    {
//        weightSize = val;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET weightSize = ?");
//            pstmt.setDouble(1, val);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//    public void setServersAccessibleWOProxyBackupFile(String file)
//    {
//        serversAccessibleWOProxyBackupFile = file;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET serversAccessibleWOProxyBackupFile = ?");
//            pstmt.setString(1, file);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//    public void setAlternativeProxyOn(boolean val)
//    {
//        alternativeProxyOn = val;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET alternativeProxyOn = ?");
//            pstmt.setBoolean(1, val);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//    public void setAlternativeProxyName(String name)
//    {
//        alternativeProxyName = name;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET alternativeProxyName = ?");
//            pstmt.setString(1, name);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//
//    public void setAlternativeProxyPort(int port)
//    {
//        alternativeProxyPort = port;
//        try {
//            PreparedStatement pstmt = con.prepareStatement(
//                    "UPDATE Initial_Parameters SET alternativeProxyPort = ?");
//            pstmt.setInt(1, port);
//            pstmt.executeUpdate();
//        } catch (Exception e) {}
//    }
//
//
//    private void setFileNumberVector (String dir) {
//        File directory = new File(dir);
//        String filenames[] = directory.list();
//        int length = filenames.length;
//        long number;
//        Long reprisentation;
//
//        fileNumberVector = new java.util.Vector(length);
//        while (--length >= 0) {
//            try {
//                reprisentation = new Long(filenames[length]);
//                number = reprisentation.longValue();
//                if (fileNameEnumerator < number)
//                    fileNameEnumerator = number;
//                fileNumberVector.addElement(reprisentation);
//            } catch (java.lang.NumberFormatException nfe) {}
//        }
//        System.err.println("Dir: " + filenames.length + "   Vector contains: " + fileNumberVector.size());
//        if (fileNameEnumerator < 10)
//            fileNameEnumerator = 10;
//    }
//
//
//}
