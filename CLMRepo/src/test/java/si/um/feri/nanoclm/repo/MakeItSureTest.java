package si.um.feri.nanoclm.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import si.um.feri.nanoclm.repo.vao.Contact;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

class MakeItSureTest {

    private static final Logger log = Logger.getLogger(MakeItSureTest.class.toString());

    @Test
    void testEventConverter1() {
        LocalDateTime n=LocalDateTime.now();
        log.info("Original: "+n);

        long l=n.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        log.info("Long: "+l);

        LocalDateTime t=LocalDateTime.ofInstant(Instant.ofEpochMilli(l),TimeZone.getDefault().toZoneId());
        log.info("Converted: "+t);

        Assertions.assertTrue(n.toString().startsWith(t.toString()));
    }

    @Test
    void testContactUniqueIdGenerator() {
        Set<String> uniqueNames=new HashSet<>();
        Contact c=new Contact("Contact");
        for (int i=0;i<20_000;i++) {
            String gen=c.generateUniqueId("TENANT");
            Assertions.assertFalse(uniqueNames.contains(gen));
            uniqueNames.add(gen);
        }
    }

}
