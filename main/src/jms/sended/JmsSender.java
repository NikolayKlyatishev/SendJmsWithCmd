package jms.sended;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class JmsSender {

    private String fileName = "file_to_send.xml";
    private String addressName = "http://localhost";
    private String portName = "8161";
    private String apiPath = "/api/message/";
    private String queueName;
    private String user = "admin";
    private String password = "admin";
    private Map<String, String> headers;
    private byte[] body;
    private String queueType = "queue";

    public JmsSender() {

        Properties properties = new Properties();

        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("setting.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (properties.isEmpty()) {
//            throw new RuntimeException("Properties do not loaded");
//        }

        fileName = properties.getProperty("fileName");
        addressName = properties.getProperty("addressName");
        portName = properties.getProperty("portName");
        apiPath = properties.getProperty("apiPath");
        queueName = properties.getProperty("queueName");
        user = properties.getProperty("user");
        password = properties.getProperty("password");
        queueType = properties.getProperty("queueType");
    }

    public void sendMessage() {

         // запуск команды через cmd
        /*

        curl -u admin:admin -d "body=<?xml version="1.0" encoding="UTF-8"?><docProjectStatusRequest xmlns="http://zakupki.gov.ru/busMonitoring" xmlns:as2="http://zakupki.gov.ru/oos/integration/1" schemeVersion="10.2"><docProjectsIds><id>63384298-2673-4e24-89e3-99233389d4dc</id></docProjectsIds></docProjectStatusRequest>
" -d "receiver=OOC&sender=VSRZ_EAIST" http://localhost:8161/api/message/resendInQueue?type=queue
         */

        BufferedInputStream bis = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(fileName));
        try {
            body = bis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        StringBuilder commandBuilder = new StringBuilder();
        // cmd команда
        commandBuilder.append("curl -u ").append(user).append(":").append(password);
        // тело пакета
        commandBuilder.append(" -d \"body=").append(new String(body, StandardCharsets.UTF_8)).append("\"");
        // jms заголовки
        commandBuilder.append(" -d \"");

        for (Map.Entry<String, String> header : headers.entrySet()) {
            commandBuilder.append(header.getKey()).append("=").append(header.getValue()).append("&");
        }
        // Удаляем последний &
        commandBuilder.deleteCharAt(commandBuilder.length() - 1).append("\" ");

        // Адрес очереди
        commandBuilder.append(addressName).append(":").append(portName).append(apiPath).append(queueName).append("?type=").append(queueType);

        String command = commandBuilder.toString();

        System.out.println(command);
        Process cmd = null;

        try {
            cmd = new ProcessBuilder("cmd.exe").redirectErrorStream(true).start();
            OutputStream os = cmd.getOutputStream();
            os.write(command.getBytes());
            os.flush();
            cmd.destroy();
        } catch (IOException e) {
            e.printStackTrace();

            if (Objects.nonNull(cmd)) {
                cmd.destroy();
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }
}
