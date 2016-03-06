package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class SimpleHTTPServer extends Thread implements ServerConstants, HTTPConstants {
    private ServerWorkPanel workPanel;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String clientName;
    private SimpleHTTPRequest request;
    private SimpleHTTPResponse response;

    public SimpleHTTPServer(Socket socket, ServerWorkPanel workPanel) throws IOException{
        this.workPanel = workPanel;
        clientName = socket.getInetAddress().getCanonicalHostName();
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        outputStream.flush();
        start();
    }

    public void run() {
        try {
            while (true){
                readRequest();
                if (getRequest() != null){
                    response = new SimpleHTTPResponse(createContentOfResponse(getRequest()));
                    outputStream.write(response.getResponseInString().getBytes());
                    writeDebug();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeDebug(){
        if (!getRequest().getUrl().equals("/favicon.ico")){
            workPanel.getTextArea().append("Request from " + clientName + " :\n");
            workPanel.getTextArea().append(request.getRequestInString() + "\n\n");
            workPanel.getTextArea().append("Response to " + clientName + " : \n");
            workPanel.getTextArea().append(response.getResponseInString());
            workPanel.getTextArea().append("\n\n");
        }
    }

    private void readRequest() throws IOException {
        byte buf[] = new byte[1024*1024];
        int s = inputStream.read(buf);
        if (s>0){
            setRequest(new SimpleHTTPRequest(new String(buf, 0, s)));
        } else{
            setRequest(null);
        }
    }

    private String createContentOfResponse(SimpleHTTPRequest pRequest) {
        String content = "";
        String url = pRequest.getUrl().equals("/")?HOME_PAGE:pRequest.getUrl();
        File f = new File(START_DIRECTORY + url);
        try {
            Scanner in = new Scanner(f);
            while (in.hasNext()){
                content = content + in.nextLine();
            }
            if (POST.equals(pRequest.getRequestType())){
                content = insertParamsFromMap(content, pRequest.getRequestParams());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            content = ERROR_MESSAGE;
        }
        return content;
    }

    public String insertParamsFromMap(String pContent, Map pParams){
        String key, value;
        int indexParam;
        Set keySet = pParams.keySet();
        Iterator i = keySet.iterator();

        while (i.hasNext()){
            key = (String) i.next();
            value = (String) pParams.get(key);
            key = "${" + key + "}";
            indexParam = pContent.indexOf(key);
            if (indexParam>0){
                pContent = pContent.substring(0, indexParam) +
                        value +
                        pContent.substring(indexParam + key.length(), pContent.length());
            }
        }
        return pContent;
    }
    public SimpleHTTPRequest getRequest() {
        return request;
    }

    public void setRequest(SimpleHTTPRequest request) {
        this.request = request;
    }
}
