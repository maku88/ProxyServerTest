package ProxyServer.request;

import lombok.Data;
import lombok.ToString;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 31.05.13
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */
@Data
@ToString
public class ReqestBody {

    private String fullBody;
    private String tagID;
    private String appVersion;

    public ReqestBody(String fullBody) {
        this.fullBody = fullBody;
        parserBody(fullBody);
    }


    private void parserBody(String s) {

        String[] elements = s.split(Pattern.quote(","));
        for(String elem : elements) {
            String[] tmp = elem.split(Pattern.quote(":"));
            if(tmp[0].contains("mTagId")) {
                this.tagID = tmp[1].replace("\"","");
            }else if(tmp[0].contains("mApplicationVersion")) {
                this.appVersion = tmp[1].replace("\"","").replace("}","");
            }
        }



    }



}
