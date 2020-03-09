package voting_system;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestSuiteRunner {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestSuite.class);

        System.out.println("Time: " + result.getRunTime() + "ms");
        if (result.wasSuccessful()) {
            System.out.println("OK<" + result.getRunCount() + " tests>");
        }
        else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
                failure.getException().printStackTrace();
            }

            System.out.println("FAILURES!!!");
            System.out.println("Tests run: " + result.getRunCount() + ", Failures: " + result.getFailures().size());
        }


    }
}
