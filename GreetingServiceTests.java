package edu.nyu.welcomehome;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GreetingServiceTests {

    @Test
    void testGetGreeting() {

        GreetingService service = new GreetingService();


        String result = service.getGreeting("Rahul");
        System.out.println(result);
        assertEquals("Hello Rahul!", result);

    }
}
