package voting_system;

import org.junit.Test;

import voting_system.Menu;

import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MenuTest {

   String runElection_command = "runElection";
   String Setup_command = "setup";
   String file_path = "./testing/sampleBallot1.txt";
   
   @Test
   public void testRunElection() {	
      System.out.println("test runElection");
      Menu menu = new Menu();
      String res = menu.deal_with_command(runElection_command);
      assertEquals(res, "Please setup your election first.");
   }
   
//   @Test
//   public void testSetup() {	
//      System.out.println("test setup");
//      Menu menu = new Menu();
//      String res = menu.deal_with_command(Setup_command);
//
//      String input = file_path;
//      InputStream in = new ByteArrayInputStream(input.getBytes());
//      System.setIn(in);
//      assertEquals(res, "Successly setup.");
//   }
}
