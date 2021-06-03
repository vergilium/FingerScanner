import Abstract.IDriver;

public class main {
    public static void main(String[] args){

        try {
            IDriver driver = new ZKTDriver();
            if(driver.OpenDevice() == 1){
                System.out.println("Port opened!");
            }
            while (true) {
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
