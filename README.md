The Evils of Statics
====================
Statics break object oriented thinking in many ways.

Object-orientation is about three things:

* messaging,
* local retention and protection and hiding of state-process, and
* extreme late-binding of all things.

Of those three, the most important one is messaging.

Static methods violate at least messaging and late-binding.

The idea of messaging means that in OO, computation is performed by networks of self-contained objects which send messages to each other. Sending a message is the only way of communication/computation.

Static methods don't do that. They aren't associated with any object. They really aren't methods at all, according to the usual definition. They are really just procedures. There's pretty much no difference between a Java static method Foo.bar and a BASIC subroutine FOO_BAR.  If you're interested in writing Fortran in Java, this is how it's done.

As for late-binding: a more modern name for that is dynamic dispatch. Static methods violate that, too, in fact, it's even in their very name: static methods.

Static methods break some very nice properties of object-orientation. For example, object-oriented systems are automatically capability-safe with objects acting as capabilities. Static methods (or really any statics, be that static state or static methods) break that property.

If you use true object oriented design, you can also execute every object in parallel in its own process, since they only communicate via messaging, thus providing some trivial concurrency.  Statics (both static methods, and especially static state) break that nice property.

*From a practical standpoint, and the one that you'll run into every day of your coding life, the problem with static methods is that your code becomes hard wired to that static method. There is no easy way to replace the reference to the static method with something else, and if you are testing your code using automated tests, this is exactly what you want to do.  Designing your code so that it doesn’t require statics means that your code is being designed with testability in mind.*

The basic issue with static methods is they are procedural code. There is no true repeatable way to break dependencies and unit test procedural code. Unit testing assumes that I can instantiate a piece of my application in isolation. During the instantiation I wire the dependencies with mocks/friendlies which replace the real dependencies. With procedural programing there is nothing to “wire” since there are no objects.  The code and data are separate.

This project will give you some insight into how to design your code with testability in mind by avoiding statics.

Guide to this code
------------------
We want to build a simple home automation controller that can do different things if it is morning, afternoon, evening, or night.  Marketing has some great videos on what our automation applicance does and how easy it is to use.  But for now, we just want to write a simple method called "getDayQuadrant()", which will return either "morning", "afternoon", "evening", or "night". 

The bad code example looks like this:
```java
package com.solutionsiq.badcode.microcontroller;

import java.time.LocalDateTime;

public class Microcontroller {
    public static String getDayQuadrant() {
        int hour = LocalDateTime.now().getHour();
        if (hour >= 0 && hour < 6) return "Night";
        if (hour >= 6 && hour < 12) return "Morning";
        if (hour >= 12 && hour < 18) return "Afternoon";
        return "Evening";
    }
}
```
But what's wrong with that?  Well, now about how do you test this?  And test it well?

We can cobble together a test that looks something like this:
```java
public class MicrocontrollerTest {
    @Test
    public void hardToSayWhatWasTested() {
        String dayQuadrant = Microcontroller.getDayQuadrant();
        assertThat(dayQuadrant, anyOf(equalTo("Morning"), equalTo("Afternoon"), equalTo("Evening"), equalTo("Night")));
    }
}
```
But the problem is that we don't know when the test gets run.  It's external state to us.  Do we run this test four times a day?  At exactly 6:00AM to make sure that we got the right result?  We didn't design this with testability in mind.

A better implementation would be to not rely on the time of day being in the same code as the code it relies on.  Take a look at this simple change.

```java
package com.solutionsiq.goodcode.microcontroller;

public class Microcontroller {
    public String getDayQuadrant(int hour) throws Exception {
        if ((hour < 0) || (hour > 23)) throw new Exception("Microcontroller given an hour of " + hour + " and that's not possible");
        if (hour >= 0 && hour < 6) return "Night";
        if (hour >= 6 && hour < 12) return "Morning";
        if (hour >= 12 && hour < 18) return "Afternoon";
        return "Evening";
    }
}
```
It's now child's play to test this for all conditions.  Repeatably and reliablebly.
```java
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
```