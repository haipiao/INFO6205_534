

import org.junit.Test;


import java.io.File;

import static org.junit.Assert.*;

public class test {

    private File data = null;

    //Test the GAK in one thread
    @Test
    public void testMove0() {
        GAK gaKnapsack = new GAK(1000, 20, 500, 0.8f, 0.9f, 0.5f, data);
        gaKnapsack.solve();
    }

    @Test
    public void testMove1() {
        GAK gaKnapsack = new GAK(1000, 200, 500, 0.8f, 0.9f, 0.5f, data);
        gaKnapsack.solve();
    }

    @Test
    public void testMove2() {
        GAK gaKnapsack = new GAK(1000, 500, 500, 0.8f, 0.9f, 0.5f, data);
        gaKnapsack.solve();
    }

    @Test
    public void testMove3() {
        GAK gaKnapsack = new GAK(1000, 1000, 500, 0.8f, 0.9f, 0.5f, data);
        gaKnapsack.solve();
    }

    @Test
    public void testMove4() {
        GAK gaKnapsack = new GAK(1000, 2000, 500, 0.8f, 0.9f, 0.5f, data);
        gaKnapsack.solve();
    }

    @Test
    public void testMove5() {
        GAK gaKnapsack = new GAK(1000, 5000, 500, 0.8f, 0.9f, 0.5f, data);
        gaKnapsack.solve();
    }


    //Test the GAK in multiple threads
    @Test
    public void testMoveTh0() {
        th hs = new th();
        th.build(1000, 20, 500, 0.8f, 0.9f, 0.5f);
    }

    @Test
    public void testMoveTh1() {
        th hs = new th();
        th.build(1000, 60, 500, 0.8f, 0.9f, 0.5f);
    }

    @Test
    public void testMoveTh2() {
        th hs = new th();
        th.build(1000, 100, 500, 0.8f, 0.9f, 0.5f);
    }

    @Test
    public void testMoveTh3() {
        th hs = new th();
        th.build(1000, 200, 500, 0.8f, 0.9f, 0.5f);
    }

    @Test
    public void testMoveTh4() {
        th hs = new th();
        th.build(1000, 500, 500, 0.8f, 0.9f, 0.5f);
    }

    @Test
    public void testMoveTh5() {
        th hs = new th();
        th.build(1000, 500, 1000, 0.8f, 0.9f, 0.5f);
    }

}
