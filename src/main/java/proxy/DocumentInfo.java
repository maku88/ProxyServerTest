//package proxy;
//
//import java.util.*;
//import java.io.*;
//import proxy.cache;
//
//
///*
// *
// * DocumentInfo - structure containing information bound to a cached document
//		1. id;
//		2. previous references;
//		3. current references;
//		4. fetch date;
//		5. size;
//		6. filename;
// *
// */
//
//public class DocumentInfo
//{
//    public DocumentInfo (Configuration p_config) {
//        config = p_config;
//    }
//
//
//    public DocumentInfo (int p_id, int prevRC, int currRC,
//                         Date fetchDate, long p_size,
//                         String p_filename)
//    {
//        id = p_id;
//        prevReferences = prevRC;
//        references = currRC;
//        creationDate = fetchDate;
//        size = p_size;
//        filename = p_filename;
//        initialized = true;
//    }
//
//
//    public void initialize (java.sql.ResultSet rs)
//            throws java.sql.SQLException
//    {
//        id = rs.getInt(1);
//        //lmd = new Date(rs.getLong(2));
//        prevReferences = rs.getInt(2);
//        references = rs.getInt(3);
//        creationDate = new Date(rs.getLong(4));
//        size = rs.getLong(5);
//        //type = rs.getString(6);
//        //areDataStoredInDB = rs.getBoolean(5);
//        filename = rs.getString(6);
//        initialized = true;
//    }
//
//
//    public void copy (DocumentInfo other)
//    {
//        if (other==null || other==this)
//            return;
//        id = other.id;
//        prevReferences = other.prevReferences;
//        references = other.references;
//        creationDate = other.creationDate;
//        size = other.size;
//        filename = other.filename;
//        initialized = other.initialized;
//    }
//
//
//    public boolean isInitialized() {
//        return initialized;
//    }
//
//
//    public void setCache (cache p_cache) {
//        cache = p_cache;
//    }
//
//
//    public void setSize (long p_size)
//    {
//        size = p_size;
//    }
//
//
//    //public void setLMD (Date p_lmd) {
//    //lmd = p_lmd;
//    //}
//
//
//    public Date getCreationDate()
//    {
//        return creationDate;
//    }
//
//
//    public int getReferences() {
//        return references;
//    }
//
//
//    public void incReferences() {
//        ++references;
//    }
//
//
//    public long getSize()
//    {
//        return size;
//    }
//
//
//    public int getId() {
//        return id;
//    }
//
//
//
//    public double getReferencesPerHour ()
//    {
//        Date currentDate = new Date();
//
//        return (60.0 * 60.0 * 1000.0 * (double) (references + prevReferences) /
//                (double)(1 + currentDate.getTime()
//                        - creationDate.getTime()));
//        // add 1 to prevent division by zero
//        // due to clock inaccuracy
//    }
//
//
//    public String getFilename() {
//        return filename;
//    }
//
//
//    public byte[] prepareHead() {
//        String headerString = "HTTP/1.0 200 OK\r\n" +
//                "Date: " + (new Date()).toString() + " GMT\r\n" +
//                "Proxy: Expiremental Caching Proxy\r\n" +
//                "MIME-version: 1.0\r\n" +
//                "Content-length: " + size + "\r\n" +
//                "\r\n";
//        return headerString.getBytes();
//    }
//
//
//    public java.io.InputStream getSourceStream() {
//        try {
//            return new FileInputStream(new File(
//                    config.getCacheDirectory(), filename));
//        } catch (java.io.FileNotFoundException fnfe) {
//            System.err.println("File " + filename + " not found in directory " + config.getCacheDirectory());
//            return null;
//        }
//    }
//
//
//    public java.io.OutputStream getDestBufferStream () {
//        String newFilename = config.getNextFileName();
//        try {
//            return new FileOutputStream(new File (
//                    config.getCacheDirectory(),
//                    filename = config.getNextFileName()));
//        } catch (Exception err) {
//            System.err.println("An IO-error in documentInfo.getDestBufferStream: ");
//            System.err.println(err.getMessage());
//            return null;
//        }
//    }
//
//
//    public synchronized void updateRC()
//    {
//        prevReferences = references;
//        references = 1;
//    }
//
//
//    public synchronized void updateFile (String name, long p_size)
//    {
//        filename = name;
//        size = p_size;
//    }
//
//
//    public synchronized void updateCreationDate (Date date)
//    {
//        creationDate = date;
//    }
//
//    Configuration config;
//    private int id;
//    private int prevReferences;
//    private int references;
//    private Date creationDate;
//    private long size;
//    private boolean areDataStoredInDB;
//    private String filename;
//    private cache cache = null;
//    private boolean initialized = false;
//}
