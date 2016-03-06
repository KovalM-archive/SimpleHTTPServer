package server;

import java.util.HashMap;
import java.util.Map;

public class SimpleHTTPRequest implements HTTPConstants {
    private String requestInString;
    private Map requestParams;
    private String requestType;
    private String url;
    private int contentLength;

    public SimpleHTTPRequest(String requestInString){
        setRequestInString(requestInString);
        setRequestType(parseRequestType(requestInString));
        setUrl(parseURL(requestInString));
        setContentLength(parseContentLength(requestInString));
        setRequestParams(parseRequestParams(requestInString));
    }

    private Map parseRequestParams(String pRequest){
        Map requestParams = null;
        if (POST.equals(getRequestType())){
            int n = pRequest.length();
            int r, separatorIndex;
            String param;
            String paramsLine = pRequest.substring(n-getContentLength(), n);

            requestParams = new HashMap<Object, Object>();
            paramsLine = paramsLine + "&";

            while (paramsLine.length()>0){
                r = paramsLine.indexOf('&');
                param = paramsLine.substring(0, r);
                separatorIndex = param.indexOf('=');
                requestParams.putIfAbsent(param.substring(0, separatorIndex),
                        param.substring(separatorIndex+1, param.length()));
                paramsLine = paramsLine.substring(r+1, paramsLine.length());
            }
        }
        return requestParams;
    }

    private int parseContentLength(String pRequest){
        int contentLength = 0;
        if (POST.equals(getRequestType())){
            String buffer = pRequest.substring(
                    pRequest.indexOf(CONTENT_LENGTH) +CONTENT_LENGTH.length()+2,
                    pRequest.length());
            contentLength = Integer.parseInt(buffer.substring(0, buffer.indexOf('\r')));
        }
        return contentLength;
    }

    private String parseURL(String pRequest){
        String url;
        int l = pRequest.indexOf(32) + 1;
        int r = l;

        while (pRequest.charAt(r) != 32){
            r++;
        }
        url = pRequest.substring(l, r);

        return url;
    }

    private String parseRequestType(String pRequest){
        return pRequest.substring(0, pRequest.indexOf(32));
    }

    public String getRequestInString() {
        return requestInString;
    }

    public void setRequestInString(String requestInString) {
        this.requestInString = requestInString;
    }

    public Map getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map requestParams) {
        this.requestParams = requestParams;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}
