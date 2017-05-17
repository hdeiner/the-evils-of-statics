package test.com.solutionsiq.badcode.microcontroller;

import com.solutionsiq.badcode.microcontroller.Microcontroller;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;

public class MicrocontrollerTest {
    @Test
    public void hardToSayWhatWasTested() {
        // Note that we never really know exactly what getDayQuadrant returns.
        // To really validate the method, we would need a complicated test that runs for 24 hours.
        String dayQuadrant = Microcontroller.getDayQuadrant();
        assertThat(dayQuadrant, anyOf(equalTo("Morning"), equalTo("Afternoon"), equalTo("Evening"), equalTo("Night")));
    }
}