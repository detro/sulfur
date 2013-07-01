import com.amazon.aiv.sulfur.factories.PageConfigFactory;

import java.util.Properties;

public class Main {
    public static void main(String[] argv) throws Exception {
        Properties sysProps = System.getProperties();
        sysProps.list(System.out);

        PageConfigFactory.getInstance();
    }
}
