/*
 * JUnit test file to make sure that the
 * program runs as expected. The class runs some 
 * of the methods and checks that they 
 * run and work correctly, by comparing
 * their output with the predicted/expected
 * output.
 */
package cst2550_coursework2;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ammar
 */
public class CST2550_CourseWork2Test {
    
//    /**
//     * Test of testIsCorrectTimeValue1 method, of class CST2550_CourseWork2.
//     */
    @Test
    public void testIsCorrectTimeValue1() {
        System.out.println("isCorrectTimeValue");
        String time = "123";
        boolean expResult = true;
        boolean result = CST2550_CourseWork2.isCorrectTimeValue1(time);
        assertEquals(expResult, result);

    }
//
//    /**
//     * Test of isCorrectFileName1 method, of class CST2550_CourseWork2.
//     */
    @Test
    public void testIsCorrectFileName1() {
        System.out.println("isCorrectFileName");
        String fileName = "test";
        boolean expResult = true;
        boolean result = CST2550_CourseWork2.isCorrectFileName1(fileName);
        assertEquals(expResult, result);
        
    }

//    /**
//     * Test of writeToFile method, of class CST2550_CourseWork2.
//     */
    @Test
    public void testWriteToFile() {
//        CST2550_CourseWork2 test = new CST2550_CourseWork2();
        System.out.println("writeToFile");
        Song song = (new Song("a", "b", "111", "test"));
        boolean expResult = true;
        boolean result = CST2550_CourseWork2.writeToFile(song);
        assertEquals(expResult, result);
    }
    
}
