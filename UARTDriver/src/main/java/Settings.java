import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class Settings{
    private static Settings instance;

    public String UART_PORT;
    public Integer UART_BAUDRATE;
    public short UART_STOPBIT;
    public short UART_PARITY;
    public short UART_DATABITS;

    private Settings(){
        Config config = ConfigFactory.load();
        /* Validating configuration */
        config.checkValid(ConfigFactory.defaultReference(), "UART");

        UART_PORT = config.getString("UART.Port");
        UART_BAUDRATE = config.getInt("UART.Baudrate");
        UART_STOPBIT = (short) config.getInt("UART.Stopbit");
        UART_PARITY = (short) config.getInt("UART.Parity");
        UART_DATABITS = (short) config.getInt("UART.Databits");
    }


    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
}
