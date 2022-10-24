package si.um.feri.nanoclm.repo;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.logging.Logger;

public class MakeItSureTest {

    private static final Logger log = Logger.getLogger(MakeItSureTest.class.toString());

    @Test
    void testEventConverter1() {
        LocalDateTime n=LocalDateTime.now();
        log.info("Original: "+n);

        long l=n.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        log.info("Long: "+l);

        LocalDateTime t=LocalDateTime.ofInstant(Instant.ofEpochMilli(l),TimeZone.getDefault().toZoneId());
        log.info("Converted: "+t);

        //Assertions.assertEquals(n.toString(),t.toString());

    }

}
