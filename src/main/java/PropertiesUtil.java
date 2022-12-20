import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author lujunqi
 * @version 1.0
 * @date 2022/12/20 18:55
 */
public class PropertiesUtil {
    private static PropertiesUtil instance = null;
    private final Properties properties = new Properties();

    private PropertiesUtil() {
        try (FileInputStream is = new FileInputStream("./application.properties")) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized PropertiesUtil getInstance() {
        if (instance == null) {
            instance = new PropertiesUtil();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
