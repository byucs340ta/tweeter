package edu.byu.cs.tweeter.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class exists purely to prove that tests in your androidTest/java folder have the correct dependencies.
 * Click on the green arrow to the left of the class declarations to run. These tests should pass if all
 * dependencies are correctly set up.
 */
public class AndroidTestsWorkingTest {
    class Foo {
        public void foo() {

        }
    }

    @Before
    public void setup() {
        // Called before each test, set up any common code between tests
    }

    @Test
    public void testAsserts() {
        Assert.assertTrue(true);
    }
    @Test
    public void testMockitoSpy() {
        Foo f = Mockito.spy(new Foo());
        f.foo();
        Mockito.verify(f).foo();
    }
    @Test
    public void testMockitoMock() {
        Foo f = Mockito.mock(Foo.class);
        f.foo();
        Mockito.verify(f).foo();
    }
}
