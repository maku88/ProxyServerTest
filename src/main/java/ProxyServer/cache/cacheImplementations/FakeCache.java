package ProxyServer.cache.cacheImplementations;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 24.07.13
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class FakeCache implements Cache {

    private static FakeCache instance ;

    private FakeCache(int capacity){}

    public static FakeCache getInstance(int capacity){
        if(instance == null ) instance = new FakeCache(capacity);
        return instance;
    }


    @Override
    public void addToCache(String tagID, String returnObject) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCachedValue(String tagID) {
        return "{\"condition\":null,\"trueActionGroup\":[{\"id\":0,\"mActionType\":\"URL\",\"mActionParams\":{\"application\":\"WWW\",\"dataType\":\"text/html\",\"url\":\"http://wp.pl\"}}],\"falseActionGroup\":null,\"nextTrue\":null,\"nextFalse\":null}";
    }
}
