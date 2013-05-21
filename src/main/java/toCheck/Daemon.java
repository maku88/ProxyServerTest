//package ProxyServer;
//
///**
// * Created with IntelliJ IDEA.
// * User: mno
// * Date: 17.05.13
// * Time: 15:41
// * To change this template use File | Settings | File Templates.
// */
//public class Daemon {
//
//    public static void main(String[] args)
//    {
//        ServerSocket cDaemonSocket = new ServerSocket(3128);
//
//        while(true)
//        {
//            try
//            {
//                Socket ClientSocket = cDaemonSocket.accept();
//                (new ClientHandler(ClientSocket )).start();
//            }catch(Exception e) { }
//        }
//    }
//}
