// Main class with the entry point for running the program
public class Main {
  public static void main(String[] args) {
    System.out.println("Running Main class...");
    // Example usage of the Example class method
    int result1 = Example.calculate(1, 0);   // Expected output: 2 (Decision: q == 0)
    System.out.println("Intermediate Result 1 calculated: " + result1);

    int result2 = Example.calculate(2, 3);   // Expected output: 36 (Decision: q != 0)
    System.out.println("Intermediate Result 2 calculated: " + result2);

    System.out.println("Result 1: " + result1);  // Output: 2
    System.out.println("Result 2: " + result2);  // Output: 36
  }
}

// Example class with a static method that takes two parameters, p and q, and performs some calculations based on the value of q.
class Example {
  public static int calculate(int p, int q) {
    System.out.println("Calculating with p = " + p + ", q = " + q);
    int r = 2 * p;           // Calculate r as twice the value of p
    System.out.println("Value of r after initial calculation: " + r);
    
    // If q equals 0, return the product of p and r
    if (q == 0) {           // Decision 1 (True branch)
      System.out.println("Branch q == 0 taken, returning p * r");
      return p * r;          // Return statement when q is 0
    } else {                // Decision 1 (False branch)
      // If q is not equal to 0, perform additional calculations
      int s = q * q * r;     // Calculate s as q squared multiplied by r
      System.out.println("Value of s after calculation: " + s);
      r += 1;                // Increment r by 1
      System.out.println("Value of r after increment: " + r);
    }
    
    // Return r squared
    System.out.println("Returning r squared: " + (r * r));
    return r * r;            // Final return statement when q is not 0
  }
}

// Test class to validate the Example class functionality
public class ExampleTest {
  public static void main(String[] args) {
    System.out.println("Running ExampleTest class...");
    try {
      testCalculate();
      System.out.println("All test cases passed.");
    } catch (AssertionError e) {
      System.err.println(e.getMessage());
    }
  }

  public static void testCalculate() {
    System.out.println("Testing Example.calculate method...");
    int result1 = Example.calculate(1, 0);  // Test case covering decision: q == 0 (True branch)
    System.out.println("Test case 1 result: " + result1);
    assert result1 == 2 : "Test case 1 failed: Expected 2, but got " + result1;

    int result2 = Example.calculate(2, 3);  // Test case covering decision: q != 0 (False branch)
    System.out.println("Test case 2 result: " + result2);
    assert result2 == 36 : "Test case 2 failed: Expected 36, but got " + result2;

    int result3 = Example.calculate(0, 0);  // Test case covering decision: q == 0 (True branch)
    System.out.println("Test case 3 result: " + result3);
    assert result3 == 0 : "Test case 3 failed: Expected 0, but got " + result3;

    int result4 = Example.calculate(3, 2);  // Test case covering decision: q != 0 (False branch)
    System.out.println("Test case 4 result: " + result4);
    assert result4 == 81 : "Test case 4 failed: Expected 81, but got " + result4;
  }
}

// To run the tests, use the command: java ExampleTest
