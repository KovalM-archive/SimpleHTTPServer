package server;

public class SimpleHTTPResponse {
    private String responseInString;
    private String content;

    public SimpleHTTPResponse(String content){
        responseInString = "HTTP/1.1 200 OK\r\n" +
                "Server: Test server\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + content.length() + "\r\n" +
                "Connection: close\r\n\r\n" + content;
        setContent(content);
    }

    public String getResponseInString() {
        return responseInString;
    }

    public void setResponseInString(String responseInString) {
        this.responseInString = responseInString;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
