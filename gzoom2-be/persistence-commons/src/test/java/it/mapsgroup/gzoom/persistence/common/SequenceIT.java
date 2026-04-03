package it.mapsgroup.gzoom.persistence.common;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.sql.DataSource;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Andrea Fossi.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = CommonPersistenceConfiguration.class)
@TestPropertySource("/gzoom.properties")
public class SequenceIT {

    @Autowired
    private SequenceGenerator sequenceGenerator;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private String seqName;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
        seqName = "Test03";
    }


    @Test
    public void SequenceTest() throws Exception {
        Long v1 = getCurrentSeqValue();
        if (v1 == null) v1 = 10000L;
        String id1 = sequenceGenerator.getNextSeqId(seqName);
        assertThat(id1, is(String.valueOf(v1)));

        Long v2 = getCurrentSeqValue();
        assertThat(v1 + 10, is(v2));
        for (int i = 0; i < 9; i++) {
            sequenceGenerator.getNextSeqId(seqName);
        }
        Long v3 = getCurrentSeqValue();
        assertThat(v1 + 10, is(v3));

    }

    private Long getCurrentSeqValue() {
        try {
            Long value = jdbcTemplate.queryForObject("SELECT SEQ_NAME, SEQ_ID from SEQUENCE_VALUE_ITEM WHERE SEQ_NAME=?",
                    (rs, rowNum) -> rs.getLong(2), seqName);
            return value;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
