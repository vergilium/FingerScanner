package ua.ks.hogo.fingerscanner.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.NetworkInterface;


@Component
@SuppressWarnings("unused")
public class Sysinfo {
    @Value("${NET.Interface}")
    private String netInterface;

    public String getSerial(){
        String serialNumber = null;
        try {
            final String serialCommand = "cat /proc/cpuinfo | grep Serial | cut -d ' ' -f 2";
            Process SerialNumberProcess = Runtime.getRuntime().exec(serialCommand);
            // getting the input stream using
            // InputStreamReader using Serial Number Process
            InputStreamReader ISR = new InputStreamReader(SerialNumberProcess.getInputStream());
            // declaring the Buffered Reader
            BufferedReader br = new BufferedReader(ISR);
            // reading the serial number using
            // Buffered Reader
            serialNumber = br.readLine().trim();
            // waiting for the system to return
            // the serial number
            SerialNumberProcess.waitFor();
            // closing the Buffered Reader
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return serialNumber;
    }

    public String getMAC(){
        try {
            NetworkInterface networkInterfaces = NetworkInterface.getByName(netInterface);
            byte[] mac = networkInterfaces.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
            }
            return sb.toString();
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Integer getCpuTemp(){
        Integer temp = null;
        try{
            File file = new File("/sys/class/thermal/thermal_zone0/temp");
            if(file.exists() && file.canRead()) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file)));
                temp = Integer.getInteger(reader.readLine().trim());
                if(temp != null){
                    temp = temp/1000;
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return temp;
    }
}
