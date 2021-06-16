package db;

import config.Settings;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public final class DB {
    Connection conn;
    Statement stmt = null;
    private final Logger log;
    private final String url;
    private static DB instance = null;

    private DB(Settings config)  {
        url = "jdbc:sqlite:" + config.DB_PATH;
        log = LogManager.getLogger(DB.class);
    }

    public static DB getInstance(){
        if(instance != null){
            return instance;
        }
        return (instance = new DB(Settings.getInstance()));
    }

    public int connect(){
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            if(conn != null){
                log.info("Connection to DB has been established.");
                return 0;
            }
            throw new SQLException("test");
           // return 1;
        } catch (SQLException ex) {
            log.error("Error connection to DB!", ex);
            return -1;
        }
    }

    public int disconnect(){
        try{
            if(conn != null) {
                conn.close();
                log.info("Connection to DB has been closed.");
                return 0;
            }
            return 1;
        }catch(SQLException ex){
            log.error("In process of disconnect from DB has been errored!", ex);
            return -1;
        }
    }

    public int sync(){
        try{
            if(this.connect() != 0){
                log.debug("Could not connect to DB!");
                return 1;
            }
            String sql = "CREATE TABLE IF NOT EXISTS finger_template (" +
                    "    id INTEGER PRIMARY KEY NOT NULL," +
                    "    uid INTEGER NOT NULL," +
                    "    last_visit NUMERIC NULL," +
                    "    scan_count INTEGER NULL," +
                    "    template BLOB NOT NULL" +
                    ")";
            stmt = conn.createStatement();
            log.debug(sql);
            int cnt_fields = stmt.executeUpdate(sql);
            log.debug(cnt_fields>0? "Table has been created in DB!" : "Table 'finger_template' is now exist!");
            stmt.close();
            return 0;
        }catch(SQLException ex){
            log.error("Sync db throwed error!", ex);
            return -1;
        }
    }
}
