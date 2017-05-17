package test.com.solutionsiq.goodcode.microcontroller;


import com.solutionsiq.goodcode.microcontroller.Microcontroller;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class MicrocontrollerTest {

    @Test
    public void testForMorning() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(7);
        assertThat(dayQuadrant, is("Morning"));
    }

    @Test
    public void testForMorningEdgeCase1() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(6);
        assertThat(dayQuadrant, is("Morning"));
    }

    @Test
    public void testForMorningEdgeCase2() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(12);
        assertThat(dayQuadrant, not("Morning"));
    }

    @Test
    public void testForAfternoon() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(13);
        assertThat(dayQuadrant, is("Afternoon"));
    }

    @Test
    public void testForAfternoonEdgeCase1() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(12);
        assertThat(dayQuadrant, is("Afternoon"));
    }

    @Test
    public void testForAfternoonEdgeCase2() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(18);
        assertThat(dayQuadrant, not("Afternoon"));
    }

    @Test
    public void testForEvening() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(20);
        assertThat(dayQuadrant, is("Evening"));
    }

    @Test
    public void testForEveningEdgeCase1() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(18);
        assertThat(dayQuadrant, is("Evening"));
    }

    @Test
    public void testForEveningEdgeCase2() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(0);
        assertThat(dayQuadrant, not("Evening"));
    }

    @Test
    public void testForNight() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(2);
        assertThat(dayQuadrant, is("Night"));
    }

    @Test
    public void testForNightEdgeCase1() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(0);
        assertThat(dayQuadrant, is("Night"));
    }

    @Test
    public void testForNightEdgeCase2() throws Exception {
        String dayQuadrant = new Microcontroller().getDayQuadrant(6);
        assertThat(dayQuadrant, not("Night"));
    }

    @Rule
    public final ExpectedException microcontrollerException = ExpectedException.none();

    @Test
    public void ensureLowHourFails() throws Exception {
        microcontrollerException.expect(Exception.class);
        microcontrollerException.expectMessage( is("Microcontroller given an hour of -1 and that's not possible"));
        String dayQuadrant = new Microcontroller().getDayQuadrant(-1);
    }

    @Test
    public void ensureHighHourFails() throws Exception {
        microcontrollerException.expect(Exception.class);
        microcontrollerException.expectMessage( is("Microcontroller given an hour of 24 and that's not possible"));
        String dayQuadrant = new Microcontroller().getDayQuadrant(24);
    }

}