package jakarta.config.tck;

import jakarta.config.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigTest {
    @Test
    void bootstrap() {
        Config config = Config.bootstrap();
        assertNotNull(config);
    }

    @Test
    void path() {
        Config config = Config.bootstrap();
        assertNotNull(config);

        Config another = config.path("another");
        assertNotNull(config);
        assertNotEquals(config, another);
    }
}
