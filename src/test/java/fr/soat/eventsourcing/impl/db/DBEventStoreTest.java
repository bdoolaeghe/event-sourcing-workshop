package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DbEventStoreConfiguration.class)
public class DBEventStoreTest {

    @Test
    public void name() {

    }
}
