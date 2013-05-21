//package proxy;
//
//import java.util.*;
//import java.io.*;
//import proxy.Configuration;
//
///*
// *
// * ServersAccessibleWOProxy
// *
// */
//
//public class ServersAccessibleWOProxy extends Hashtable
//{
//    private Configuration config;
//
//    public ServersAccessibleWOProxy(Configuration p_config)
//    {
//        super();
//
//        config = p_config;
//
//        try {
//            RandomAccessFile in = new RandomAccessFile(
//                    config.getServersAccessibleWOProxyBackupFile(),"r");
//
//            String s = in.readLine().substring(1);
//            in.close();
//
//            StringTokenizer st = new StringTokenizer(s);
//            String key;
//            Object dummy = new Object();
//
//            try {
//                while(st.hasMoreElements()) {
//                    key = st.nextToken("=");
//                    while (key.substring(0,1).equals(" "))
//                        key = key.substring(1);
//                    st.nextToken(",}");
//                    put(key, dummy);
//                }
//            } catch (NoSuchElementException e) {	// backup file is corrupted
//                clear();
//                in.close();
//                File f = new File(
//                        config.getServersAccessibleWOProxyBackupFile());
//                f.delete();
//            }
//        } catch (IOException e) {}      // cannot access backup file
//    }
//
//
//    public boolean shutDown()
//    {
//        File file = new File(config.getServersAccessibleWOProxyBackupFile());
//        if (file.exists()) file.delete();
//
//        try {
//            RandomAccessFile out = new RandomAccessFile(file,"rw");
//            out.writeBytes(toString()+'\n');
//            out.close();
//        } catch (IOException e) {
//            return false;
//        }
//
//        return true;
//    }
//
//}
