package voting_system;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        StatisticsTest.class,
        CandidateTest.class,
        AuditTest.class,
        ElectionTest.class,
        PartyTest.class,
        System_test.class
})

public class TestSuite {

}
