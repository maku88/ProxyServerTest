//package proxy;
//
//import java.io.*;
//import java.sql.*;
//import proxy.DocumentInfo;
//import proxy.Configuration;
//import proxy.DocumentMark;
//import proxy.ServerInfo;
//import proxy.DBCache;
//import proxy.LogTracer;
//
///*
// *
// * CacheCleaner -   responsible of the cache removal algorithm
// *                  and all the related routines. Runs as a thread
// *                  Also performs the cache sturt up missions, like:
// *                  removal of files unregistered in the DB, removal
// *                  DB entries which are bound to no file in the cache
// *
// */
//
//public class CacheCleaner extends Thread
//{
//    public CacheCleaner(Configuration p_config, LogTracer p_mainFrame)
//            throws SQLException
//    {
////        p_config = p_mainFrame;
////        config = p_config;
////        read_con = write_con = DriverManager.getConnection("jdbc:odbc:ActiveProxyDatabase", "active", "proxy");
////        write_con.setAutoCommit(false);
////        dbCache = new DBCache(read_con);
////        mainFrame.updateLog("DB-cache unit started");
////        getCacheInformation();
////        clearCacheIfNecessary();
////        mainFrame.updateCacheBar(config.getCacheHighWaterMark(),
////                config.getCacheLowWaterMark(),
////                getCurrentWaterMark());
//        start();
//    }
//
//
//    public long getCurrentWaterMark()
//    {
//        return 0;//currentWaterMark;
//    }
//
//
//    //Increases the current water mark. Performs cache cleaning
//    //If the  water mark is exceeded. Called outside
//    public void incrementCurrentWaterMark (long incremention)
//    {
////        synchronized (write_con) {
////            currentWaterMark += incremention;
////            ++size;
////            if (cleanUpNecessary())
////                resume();   //the method is called not by this thread
////        }
//    }
//
//
//    //Decreases the current water mark
//    public void decrementCurrentWaterMark (int id)
//    {
////        try {
////            Statement getDocSize = read_con.createStatement();
////            ResultSet rs = getDocSize.executeQuery(
////                    "SELECT Size FROM DocumentInfo WHERE Doc_Id = " + id);
////            synchronized(this) {
////                if (rs.next()) {
////                    currentWaterMark -= rs.getLong(1);
////                    --size;
////                }
////            }
////            getDocSize.close();
////        } catch (SQLException err) {
////        }
//    }
//
//
//    public Connection getWriteConnection() {
//        return write_con;
//    }
//
//
//    public DBCache getDBCache()
//    {
//        return dbCache;
//    }
//
//
//    //The main runtime function of the cleaner's thread
//    public void run() {
//        while(true) {
//            suspend();
//            clearCacheIfNecessary();
//        }
//    }
//
//
//    //Performs cache clean up if the current water mark is exceeded
//    private boolean cleanUpNecessary()
//    {
//        long lowWM = config.getCacheLowWaterMark();
//        long barrier = lowWM + ((long) ((config.getCacheHighWaterMark() - lowWM) * BARRIER_PERCENT));
//        //mainFrame.updateLog("Barrier=" + barrier + "    Currently=" + currentWaterMark);
//        return currentWaterMark >= barrier;
//    }
//
//
//
//    private void clearCacheIfNecessary() {
//        while (cleanUpNecessary() && makeRoom());
//    }
//
//
//    //This routine manages the cache clean up. It calls all the
//    //assistant routines, s.a. sorting and DB information removal
//    private boolean makeRoom()
//    {
//        DocumentMark[] array = new DocumentMark[size];
//        int amount;
//        long lowWaterMark;
//        long highWaterMark;
//        boolean result = false;
//
//
//        mainFrame.updateLog("\nPerforming cache cleanup...\n");
//        synchronized (config) {
//            lowWaterMark = config.getCacheLowWaterMark();
//            highWaterMark = config.getCacheHighWaterMark();
//        }
//        try {
//            if ((amount = fillArrayOfMarks(array)) > 0) {
//                mainFrame.updateLog("The document marks are yielded.");
//                sort(array, 0, amount-1);
//                mainFrame.updateLog("The document marks are sorted.");
//                result = cleanCache(array, lowWaterMark, highWaterMark);
//            }
//        } catch (SQLException err) {
//            System.err.println("An SQL-error during the cache cleaning: ");
//            System.err.println(err.getMessage());
//        }
//        return result;
//
//    }
//
//
//    //Fills array of document marks (in the point of view of their removal)
//    private int fillArrayOfMarks (DocumentMark[] array)
//            throws SQLException
//    {
//        synchronized (write_con) {
//            int i=0;
//            double mark;
//            double wCT,wBW,wRF,wS;
//            DocumentInfo documentInfo;
//            ServerInfo serverInfo;
//            Statement stmt = read_con.createStatement();
//            ResultSet rs = stmt.executeQuery(
//                    "SELECT Documents.Doc_Id, Previous_Request_Count, " +
//                            "Request_Count, Fetch_Date, Size, " +
//                            "Filename " +
//                            //$$"Documents.Server_Name, " +
//                            //$$"Average_Connection_Time, Average_Bandwidth " +
//                            "FROM Documents, DocumentInfo, " +
//                            "DocumentFilenames " +
//                            //$$"WHERE Documents.Server_Name=ServerInfo.Server_Name AND " +
//                            "WHERE Documents.Doc_Id=DocumentInfo.Doc_Id " +
//                            "AND Documents.Doc_Id=DocumentFilenames.Doc_id ");
//
//            synchronized (config) {
//                wCT = config.getWeightConnectionTime();
//                wBW = config.getWeightBandWidth();
//                wRF = config.getWeightReferencesFrequency();
//                wS  = config.getWeightSize();
//            }
//            try {
//                for (; rs.next(); ++i) {
//                    (documentInfo = new DocumentInfo(config)).initialize(rs);
//                    serverInfo = new ServerInfo();//$$.initialize(rs);
//                    mark = documentMark(documentInfo, serverInfo,
//                            wCT, wBW, wRF, wS);
//                    array[i] = new DocumentMark(mark, documentInfo);
//                }
//            } catch (ArrayIndexOutOfBoundsException iaoube) {
//            }
//            return i;
//        }
//    }
//
//
//
//    private final static double documentMark (DocumentInfo documentInfo,
//                                              ServerInfo serverInfo,
//                                              double wCT, double wBW,
//                                              double wRF, double wS)
//    {
//        // Refer to documentation for marking policy.
//
//        return ((wCT * serverInfo.getAverageConnectionTime() +
//                wBW / serverInfo.getAverageBandWidth()) *
//                Math.pow(documentInfo.getReferencesPerHour(),wRF) /
//                Math.pow(documentInfo.getSize(),wS));
//
//    }
//
//
//    //2 following routines sort an array of the marks
//    private final static void sort(DocumentMark[] array, int left, int right)
//    {
//        if (left >= right)
//            return;
//
//        int mid = split(array,left,right);
//        sort(array, left, mid-1);
//        sort(array, mid+1, right);
//
//    }
//
//    private final static int split(DocumentMark[] array, int p_left, int p_right)
//    {
//        double mid = array[p_left].getMark();
//        int left = p_left+1;
//        int right = p_right;
//        DocumentMark temp;
//
//        while(left <= right) {
//            while (left <= right && array[left].getMark() < mid)
//                left++;
//            while (left <= right && array[right].getMark() > mid)
//                right--;
//            if (left <= right) {
//                temp = array[left];
//                array[left] = array[right];
//                array[right] = temp;
//                left++;
//                right--;
//            }
//        }
//
//        temp = array[right];
//        array[right] = array[p_left];
//        array[p_left] = temp;
//
//        return right;
//    }
//
//    //Performs relevant information removal from the DB
//    private boolean cleanCache (DocumentMark[] array,
//                                long lowWaterMark,
//                                lo