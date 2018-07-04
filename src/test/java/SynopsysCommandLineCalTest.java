
import junit.framework.TestCase;
//import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by matthewnguyen99 on Jul 02, 2018.
 */
public class SynopsysCommandLineCalTest extends TestCase {

    private final SynopsysCommandLineCal instance = new SynopsysCommandLineCal();

    /**
     * Testing if exception is thrown from invalid verbosity
     * Verbosity must be info, debug, or error.
     */
    public void testInvalidVerbosity() throws Exception {
        boolean didThrowException = false;
        try {
            SynopsysCommandLineCal.main(new String[]{"add(1, 2)", "testing"});
        } catch (RuntimeException e) {
            didThrowException = true;
        }

        assertEquals(true, didThrowException);
    }

    /**
     *  string expression = "add(1, 2)"
     *  final calculated value = 3
     */
    public void testAccessStringData1() throws Exception {
        String strInput = "add(1, 2)";
        int expectedResult = 3;
        long functionResult = instance.accessStringData(strInput);

        assertEquals(expectedResult,functionResult);      //pass
    }
    /**
     *  string expression = "add(1, mult(2, 3))"
     *  final calculated value = 7
     */
    public void testAccessStringData2() throws Exception {
        String strInput = "add(1, mult(2, 3))";
        int expectedResult = 7;
        long functionResult = instance.accessStringData(strInput);

        assertEquals(expectedResult,functionResult);      //pass
    }
    /**
     *  string expression = "mult(add(2, 2), div(9, 3))"
     *  final calculated value = 12
     */
    public void testAccessStringData3() throws Exception {
        String strInput = "mult(add(2, 2), div(9, 3))";
        int expectedResult = 12;
        long functionResult = instance.accessStringData(strInput);

        assertEquals(expectedResult,functionResult);      //pass
    }
    /**
     *  string expression = "let(a, 5, add(a, a))"
     *  final calculated value = 10
     */
    public void testAccessStringData4() throws Exception {
        String strInput = "let(a, 5, add(a, a))";
        int expectedResult = 10;
        long functionResult = instance.accessStringData(strInput);

        assertEquals(expectedResult,functionResult);      //pass
    }
    /**
     *  string expression = "let(a, 5, let(b, mult(a, 10), add(b, a)))"
     *  final calculated value = 55
     */
    public void testAccessStringData5() throws Exception {
        String strInput = "let(a, 5, let(b, mult(a, 10), add(b, a)))";
        int expectedResult = 55;
        long functionResult = instance.accessStringData(strInput);

        assertEquals(expectedResult,functionResult);      //pass
    }
    /**
     *  string expression = "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))"
     *  final calculated value = 40
     */
    public void testAccessStringData6() throws Exception {
        String strInput = "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))";
        int expectedResult = 40;
        long functionResult = instance.accessStringData(strInput);

        assertEquals(expectedResult,functionResult);      //pass
    }
    /**
     *  string expression = "sub(mult(6,5),add(5,5))"
     *  final calculated value = 20
     */
    public void testAccessStringData7() throws Exception {
        String strInput = "sub(mult(6,5),add(5,5))";
        int expectedResult = 20;
        long functionResult = instance.accessStringData(strInput);

        assertEquals(expectedResult,functionResult);      //pass
    }
    /**
     *  string expression = "mult(add(2, 2), div(9, 3))"
     *  expectedResult = [*,+,2,2,/,9,3]
     */
    public void testConvertToPrefixSymbol() throws Exception {
        String strInput = "mult(add(2, 2), div(9, 3))";
        ArrayList<Object> expectedResult = new ArrayList<>(Arrays.asList("*", "+", "2", "2", "/", "9", "3") );
        ArrayList<Object> functionResult = instance.convertToPrefixSymbol(strInput);

        assertEquals(expectedResult.toString(), functionResult.toString());  //pass
    }
    /**
     *  string expression = "add(1, mult(2, 3))"
     *  expectedResult = [+,1,*,2,3]
     */
    public void testConvertToPrefixSymbol2() throws Exception {
        String strInput = "add(1, mult(2, 3))";
        ArrayList<Object> expectedResult = new ArrayList<Object>(){
                                                                {add("+");add("1");add("*");add("2");add("3"); }};
        ArrayList<Object>  functionResult = instance.convertToPrefixSymbol(strInput);

        assertEquals(expectedResult.toString(), functionResult.toString());  //pass
    }

    /**
     * string numerical = 13579
     * return true
     */
    public void testIsInt() throws Exception {
        String strNum = "13579";
        assertTrue(instance.isInt(strNum) );  // pass
    }
    /**
     * string numerical = 1357ppp
     * return false
     */
    public void testIsInt2() throws Exception{
        String strNum = "1357ppp";
        assertFalse(instance.isInt(strNum) ); // pass
    }
    /**
     * string numerical = -94193
     * return true
     */
    public void testIsInt3() throws Exception{
        String strNum = "-94193";
        assertTrue(instance.isInt(strNum));   // pass
    }
    /**
     * arraylist input = [*,+,2,2,/,9,3]
     * expected result = 12
     */
    public void testCalcPrefixExpr() throws Exception {
        ArrayList<Object> arrayListInput = new ArrayList<>(Arrays.asList("*", "+", "2", "2", "/", "9", "3") );
        int expectedResult = 12;
        long functionResult = instance.calcPrefixExpr(arrayListInput);

        assertEquals(expectedResult,functionResult);      //pass
    }
    /**
     * arraylist input = [+,1,*,2,3]
     * expected result = 7
     */
    public void testCalcPrefixExpr2() throws Exception {
        ArrayList<Object> arrayListInput = new ArrayList<>(Arrays.asList("+","1","*","2","3"));
        int expectedResult = 7;
        long functionResult = instance.calcPrefixExpr(arrayListInput);

        assertEquals(expectedResult,functionResult);     // pass
    }
    /**
     * [let, a, 5, add, a, a] becomes [add, 5, 5]
     */
    public void testAssignData() throws Exception {
        ArrayList<Object> arrayListInput = new ArrayList<>(Arrays.asList("let","a","5","add","a","a"));
        ArrayList<Object> expectedResult = new ArrayList<>(Arrays.asList("add","5","5"));
        ArrayList<Object> functionResult = instance.assignData(arrayListInput);

        assertEquals(expectedResult,functionResult);   // pass
    }
    /**
     * [let, a, 5, let, b, 10, add, b, a] becomes [add, 10, 5]
     */
    public void testAssignData2() throws Exception {
        ArrayList<Object> arrayListInput = new ArrayList<>(Arrays.asList("let","a","5","let","b","10","add","b","a"));
        ArrayList<Object> expectedResult = new ArrayList<>(Arrays.asList("add","10","5"));
        ArrayList<Object> functionResult = instance.assignData(arrayListInput);

        assertEquals(expectedResult,functionResult);   // pass
    }
    /**
     * [let, b, *, a, 10, +, b, a] becomes [let, b, *, 5, 10, +, b, 5]
     */
    public void testUpdateArrayList() throws Exception {
        ArrayList<Object> arrayListInput = new ArrayList<>(Arrays.asList("let","b","*","a","10","+","b","a"));
        Object operand = "a";
        Object item = "5";
        ArrayList<Object> expectedResult = new ArrayList<>(Arrays.asList("let","b","*","5","10","+","b","5"));
        ArrayList<Object> functionResult = instance.updateArrayList(arrayListInput,operand,item);

        assertEquals(expectedResult,functionResult);   // pass
    }

    /**
     * [let, b, 99, +, b, a] becomes [let, 99,99, +, 99, a]
     */
    public void testUpdateArrayList2() throws Exception {
        ArrayList<Object> arrayListInput = new ArrayList<>(Arrays.asList("let","b","99","+","b","a"));
        Object operand = "b";
        Object item = "99";
        ArrayList<Object> expectedResult = new ArrayList<>(Arrays.asList("let","99","99","+","99","a"));
        ArrayList<Object> functionResult = instance.updateArrayList(arrayListInput,operand,item);

        assertEquals(expectedResult,functionResult);   // pass
    }

    /**
     * test when arraylist is not updated
     */
    public void testUpdateArrayList3() throws Exception {
        ArrayList<Object> arrayListInput = new ArrayList<>(Arrays.asList("let","b","11","+","b","a"));
        Object operand = "b";
        Object item = "11";
        ArrayList<Object> wrongResult = new ArrayList<>(Arrays.asList("let","b","11","+","b","a"));
        ArrayList<Object> functionResult = instance.updateArrayList(arrayListInput,operand,item);

        assertNotSame(wrongResult,functionResult);   // pass
    }
}