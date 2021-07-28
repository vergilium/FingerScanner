package ua.ks.hogo.fingerscanner.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.stereotype.Component;

@Component
public final class Settings{
    public String UART_PORT;
    public Integer UART_BAUDRATE;
    public short UART_STOPBIT;
    public short UART_PARITY;
    public short UART_DATABITS;
    public int UART_RECEIVE_TIMEOUT;

    public int HTTP_REQUEST_TIMEOUT;
    public String HTTP_SERVER_HOST;
    public int HTTP_SERVER_PORT;

    public Settings(){
        Config config = ConfigFactory.load();
        /* Validating configuration */
        config.checkValid(ConfigFactory.defaultReference(), "UART");

        UART_PORT = config.getString("UART.Port");
        UART_BAUDRATE = config.getInt("UART.Baudrate");
        UART_STOPBIT = (short) config.getInt("UART.Stopbit");
        UART_PARITY = (short) config.getInt("UART.Parity");
        UART_DATABITS = (short) config.getInt("UART.Databits");
        UART_RECEIVE_TIMEOUT = config.getInt("UART.ReceiveTimeout");

        HTTP_SERVER_HOST = config.getString("HTTP.Server");
        HTTP_SERVER_PORT = config.getInt("HTTP.Port");
        HTTP_REQUEST_TIMEOUT = config.getInt("HTTP.Timeout");

    }

}
