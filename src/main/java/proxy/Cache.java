//package proxy;
//
//import java.net.*;
//import java.io.*;
//import java.awt.*;
//import java.sql.*;
//import proxy.DocumentInfo;
//import proxy.ServerInfo;
//import proxy.Configuration;
//import proxy.DocumentMark;
//import proxy.HttpReplyHdr;
//import proxy.LogTracer;
//
///*
// *
// * cache -  responsible of registration of the cached document
// *          information in the attached DB
// *          The class is a medium between the proxy and its DB
// *
// */
//
//public class cache
//{
//    private LogTracer mainFrame;
//    private Connection read_con;
//    private Connection write_con;
//    private CacheCleaner cacheCleaner;
//    public static String CACHED_FILENAME_PREFIX = "_";
//    public static String TEMPORARY_FILENAME_PREFIX = "temp_";
//    public static String CACHE_DIRECTORY_PATH = "cache";//"cache";
//
//
//    public cache(CacheCleaner p_cacheCleaner, LogTracer p_mainFrame)
//            throws SQLException
//    {
//        mainFrame = p_mainFrame;
//        cacheCleaner = p_cacheCleaner;
//        read_con = write_con = p_cacheCleaner.getWriteConnection();
//    }
//
//
//    //Inspector checking if a URL is cached
//    public boolean containsKey (URL url) {//$$
//        return getKeyId(url) != 0;
//    }
//
//
//    //Yields information attached to the URL
//    public void getDocumentInfo(URL url, DocumentInfo documentInfo)
//    {
//        String host = getHostName(url);
//        documentInfo.copy(cacheCleaner.getDBCache().getDocumentInfo(url));
//    }
//
//
//    //Deprecated
//    public InputStream getSourceStream (boolean areDataStoredInDB, int id) {
//        if (areDataStoredInDB)
//            try {
//                PreparedStatement pstmt;
//                pstmt = read_con.prepareStatement(
//                        "SELECT Content FROM DocumentFileStorage WHERE Doc_Id = ?");
//                pstmt.setInt(1, id);
//                ResultSet rs = pstmt.executeQuery();
//                if (rs.next())
//                    return rs.getBinaryStream(1);
//            } catch (SQLException e) {
//                System.err.println("An SQL-error in Proxy.getSourceStream: ");
//                System.err.println(e.getMessage());
//            }
//        else
//            try {
//                File source = getCachedFile(id);
//                //if (source.exists())
//                return new FileInputStream(source);
//            } catch (IOException e2) {
//                System.err.println("An IO-error in Proxy.getSourceStream: ");
//                System.err.println(e2.getMessage());
//            }
//        return null;
//    }
//
//
//    //Increments reference counter of a stored document (in DB)
//    public void incrementReferences (int id, int howMuch) {
//        synchronized (write_con) {
//            try {
//                PreparedStatement incRef = write_con.prepareStatement(
//                        "UPDATE DocumentInfo SET Request_Count = Request_Count + ? " +
//                                "WHERE Doc_Id = ?");
//                incRef.setInt(1, howMuch);
//                incRef.setInt(2, id);
//                incRef.executeUpdate();
//                write_con.commit();
//            } catch (SQLException e) {
//                System.err.println("An SQL-error in Proxy.incrementReferences: ");
//                System.err.println(e.getMessage());
//            }
//        }
//    }
//
//
//    //Inserts or updates information bound to a new or refreshed
//    //document (consistently)
//    public void updateURL (URL url,
//                           long docSize, String filename, HttpReplyHdr reply)
//    {
//        int id;
//        PreparedStatement updateHead;
//        PreparedStatement updateContent;
//
//        mainFrame.updateLog("Filename: " + filename);
//        cacheCleaner.incrementCurrentWaterMark(docSize);
//        id = getKeyId(url);
//
//
//        synchronized (write_con) {
//            try {
//                //Updating of the information
//                if (id != 0 && cacheCleaner.getDBCache().isSaved(id)) {  //the document is stored, but to be updated
//                    cacheCleaner.decrementCurrentWaterMark(id);
//                    updateHead = write_con.prepareStatement(
//                            "UPDATE DocumentInfo SET LMD = ? , " +
//                                    "Previous_Request_Count = Request_Count, " +
//                                    "Request_Count = 1 , " +
//                                    "Previous_Fetch_Date = Fetch_Date, " +
//                                    "Fetch_Date = ? , " +
//                                    "Size = ? " +
//                                    "WHERE Doc_Id = ?");
//                    updateHead.setDouble(1, (double) reply.getLastModified());
//                    updateHead.setDouble(2, (double)((new java.util.Date()).getTime()));
//                    updateHead.setDouble(3, docSize);
//                    updateHead.setInt(4, id);
//                    updateHead.executeUpdate();
//                    updateContent = write_con.prepareStatement(
//                            "UPDATE DocumentFilenames SET Filename = ? " +
//                                    "WHERE Doc_Id = ?");
//                    updateContent.setString(1, filename);
//                    updateContent.setInt(2, id);
//                    updateContent.executeUpdate();
//                    mainFrame.updateLog("UPDATE DocumentFilenames SET Filename = ?");
//                }
//                else {
//                    //Insertion of the new document information
//                    id = url.toString().hashCode();
//                    PreparedStatement createEntry = write_con.prepareStatement(
//                            "INSERT INTO Documents VALUES (?, ?, ?)");
//                    createEntry.setInt(1, id);
//                    createEntry.setString(2, getHostName(url));
//                    createEntry.setString(3, url.getFile());
//                    createEntry.executeUpdate();
//                    createEntry.close();
//                    mainFrame.updateLog("INSERT INTO Documents VALUES (?, ?, ?)");
//                    updateHead = write_con.prepareStatement(
//                            "INSERT INTO DocumentInfo VALUES (?, ?, ?, 1, ?, ?, ?, ?, ?)");
//                    updateHead.setInt(1, id);
//                    updateHead.setDouble(2, (double)  reply.getLastModified());
//                    updateHead.setInt(3, 0);     //Previous_Request_Count
//                    updateHead.setDouble(4, 0.);    //Previous_Fetch_Date
//                    updateHead.setDouble(5, (double)((new java.util.Date()).getTime()));
//                    updateHead.setDouble(6, docSize);
//                    updateHead.setString(7, reply.getContentType());
//                    updateHead.setInt(8, 1); //Is_New
//                    updateHead.executeUpdate();
//                    mainFrame.updateLog("INSERT INTO DocumentInfo VALUES (?, ?, 1, ?, ?, ?, ?)");
//                    updateContent = write_con.prepareStatement(
//                            "INSERT INTO DocumentFilenames VALUES (?, ?)");
//                    updateContent.setInt(1, id);
//                    updateContent.setString(2, filename);
//                    updateContent.executeUpdate();
//                    cacheCleaner.getDBCache().setSaved(id);
//                }
//                updateHead.close();
//                updateContent.close();
//                write_con.commit();
//            } catch (Exception err) {
//                System.err.println("An SQL-error occured during document caching:");
//                System.err.println(err.getMessage());
//                try {
//                    write_con.rollback();
//                } catch (SQLException e) {}
//            }
//        }
//    }
//
//
//
//    public void updateAverageConnectionTime (URL serverURL,
//                                             double p_connectionTime,
//                                             double smoothingFactor)
//    {
//        String serverName = getHostName(serverURL);
//        boolean isFound;
//
//        try {
//            isFound = serverInfoFound(serverName);
//        } catch (SQLException err1) {
//            System.err.println("An SQL-error occured during Average connection time updating (Server existence check failed):");
//            System.err.println(err1.getMessage());
//            return;
//        }
//        if (isFound) {
//            synchronized (write_con) {
//                //incrementPriority();
//                try {
//                    PreparedStatement update = write_con.prepareStatement(
//                            "UPDATE ServerInfo SET Average_Connection_Time = " +
//                                    "? * Average_Connection_Time + ? " +
//                                    "WHERE Server_Name=?");
//                    update.setDouble(1, 1-smoothingFactor);
//                    update.setDouble(2, smoothingFactor*p_connectionTime);
//                    update.setString(3, serverName);
//                    update.executeUpdate();
//                    write_con.commit();
//                    update.close();
//                } catch (SQLException err) {
//                    System.err.println("An SQL-error occured during Average connection time updating:");
//                    System.err.println(err.getMessage());
//                }
//            }
//        }
//    }
//
//
//    public void updateAverageConnectionTimeAndBandwidth (URL serverURL,
//                                                         double p_bandWidth,
//                                                         double p_connectionTime,
//                                                         double smoothingFactor)
//    {
//        String serverName = getHostName(serverURL);
//        boolean isFound;
//
//        try {
//            isFound = serverInfoFound(serverName);
//        } catch (SQLException err1) {
//            System.err.println("An SQL-error occured during Server info updating (Server existence check failed):");
//            System.err.println(err1.getMessage());
//            return;
//        }
//        synchronized (write_con) {
//            //incrementPriority();
//            try {
//                if (isFound) {
//                    PreparedStatement update = write_con.prepareStatement(
//                            "UPDATE ServerInfo SET Average_Connection_Time = " +
//                                    "? * Average_Connection_Time + ?, " +
//                                    "Average_Bandwidth = ? * Average_Bandwidth + ? " +
//                                    "WHERE Server_Name=?");
//                    update.setDouble(1, 1-smoothingFactor);
//                    update.setDouble(2, smoothingFactor*p_connectionTime);
//                    update.setDouble(3, 1-smoothingFactor);
//                    update.setDouble(4, smoothingFactor*p_bandWidth);
//                    update.setString(5, serverName);
//                    update.executeUpdate();
//                    write_con.commit();
//                    update.close();
//                }
//                else {
//                    PreparedStatement insert = write_con.prepareStatement(
//                            "INSERT INTO ServerInfo VALUES (?, ?, ?)");
//                    insert.setString(1, serverName);
//                    insert.setDouble(2, p_connectionTime);
//                    insert.setDouble(3, p_bandWidth);
//                    insert.executeUpdate();
//                    write_con.commit();
//                    insert.close();
//                }
//            } catch (SQLException err) {
//                System.err.println("An SQL-error occured during Server info updating:");
//                System.err.println(err.getMessage());
//            }
//            //restorePriority();
//        }
//    }
//
//
//
//    //Returns the current water mark of the cache
//    public long getCurrentWaterMark() {
//        return cacheCleaner.getCurrentWaterMark();
//    }
//
//
//    public proxy.DBCache getDBCache()
//    {
//        return cacheCleaner.getDBCache();
//    }
//
//
//    public static String getHostName (URL url) {
//        if (url.getPort() >= 0)
//            return url.getHost() + ':' + url.getPort();
//        return url.getHost();
//    }
//
//
//    public File getCachedFile (int id)
//    {
//        return new File("temp_" + id);
//    }
//
//
//    public File getTemporaryFile (int id)
//    {
//        return new File("temp_" + id);
//    }
//
//
//
//
//
//
//    private int getKeyId (URL url) {//$$
//        String host = getHostName(url);
//        int result;
//
//        DocumentInfo di = cacheCleaner.getDBCache().getDocumentInfo(url);
//        if (di != null)
//            return di.getId();
//        return 0;
//
//    }
//
//
//    private boolean serverInfoFound (String serverName)
//            throws SQLException
//    {
//        try {
//            PreparedStatement check = read_con.prepareStatement(
//                    "SELECT * FROM ServerInfo WHERE Server_Name=?");
//            check.setString(1, serverName);
//            ResultSet rs = check.executeQuery();
//            boolean result = rs.next();
//            check.close();
//            return result;
//        } catch (SQLException err) {
//            System.err.println("An SQL-error occured during Server info existense checking:");
//            System.err.println(err.getMessage());
//            throw new SQLException();
//        }
//    }
//
//
//
//    private void commitFileCaching (int id) {
//        File dest;
//        File temp;
//
//        dest = getCachedFile(id);
//        temp = getTemporaryFile(id);
//        for (;!temp.renameTo(dest);)
//            Thread.yield();
//    }
//
//
//    private void incrementPriority()
//    {
//        Thread currThread = Thread.currentThread();
//        currThread.setPriority(currThread.getPriority() + 3);
//    }
//
//
//    private void restorePriority()
//    {
//        Thread currThread = Thread.currentThread();
//        currThread.setPriority(currThread.getPriority() - 3);
//    }
//
//
//
//
//    public boolean shutDown() {
//        return true;
//    }
//
//}
//
//
//
//
//
//
