
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Created by matthewnguyen99 on Jun 27, 2018.
 */

public class SynopsysCommandLineCal {

    /**
     * Returns the final value of the given function. When an input string is entered, method first checks  "let"(s)
     * operator in the input string, and then assigns that input string to appropriate methods.
     * <br />
     * The string must satisfy the input format. It must contain "let", "add", "sub", "mult", and/or "div"
     * <br />
     * @param strInput is the string input which has format "let(a, 5, let(b, mult(a, 10), add(b, a)))";
     *                 or "add(1, mult(2, 3))";
     * @return the final value of program
     */
    public long accessStringData(String strInput){
        ArrayList<Object> arrayList = convertToPrefixSymbol(strInput);
        long result;
        if(arrayList.get(0) == "let" && arrayList.get(2) == "let"){
            Logger.debug("", "Processing 'let'");

            int fromIndex = 2;
            int toIndex = 8;
            ArrayList<Object> tempArrList =  new ArrayList<>(arrayList.subList(fromIndex,toIndex));
            tempArrList = assignData(tempArrList);
            long itemCalculated = calcPrefixExpr(tempArrList);

            arrayList.subList(fromIndex,toIndex).clear();
            arrayList.add(fromIndex,itemCalculated);

            arrayList = assignData(arrayList);
            result    = calcPrefixExpr(arrayList);
        }

        else if(arrayList.get(0) == "let"){
            arrayList = assignData(arrayList);
            result    = calcPrefixExpr(arrayList);
        }
        else{
            result    = calcPrefixExpr(arrayList);
        }

        System.out.println("Output: " + result);
        return result;
    }
    /**
     * Returns arraylist that has prefix expression format. As an input string is
     * entered, the method reads input string and converts some texts to math symbols.
     * <br />
     * For example:
     * "mult(add(2, 2), div(9, 3))"  becomes  [*, +, 2, 2, /, 9, 3]
     * <br />
     * @param strInput is the string input which has format "add(1, mult(2, 3))";
     * @return arraylist that contains numbers and math symbols
     */
    public ArrayList<Object> convertToPrefixSymbol(String strInput){
        ArrayList<Object> arrayList = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(strInput," (),");
        while (stringTokenizer.hasMoreTokens()){
            String token = stringTokenizer.nextToken();
            Logger.debug("", "Processing " + token);
            switch (token){
                case "add":
                    arrayList.add('+');
                    break;
                case "sub":
                    arrayList.add('-');
                    break;
                case "mult":
                    arrayList.add('*');
                    break;
                case "div":
                    arrayList.add('/');
                    break;
                case "let":
                    arrayList.add("let");
                    break;
                default:
                    arrayList.add(token);
                    break;
            }
        }
        Logger.info("","Convert input to Prefix Symbols: " + arrayList);
        return arrayList;
    }
    /**
     * Returns true if an input string is numerical, false otherwise.
     * <br />
     * @param strNum is a numerical string
     * @return numerical boolean (TRUE/FALSE)
     */
    public boolean isInt(String strNum){
        try {
            Integer.parseInt(strNum);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Calculates the given prefix expression in the ArrayList.
     * For example, if arraylist contains [+, 2, 3], result is 2+3 = 5
     * <br />
     * This method reuses the same ArrayList instance.
     * <br />
     * @param arrayList is the object arraylist to calculate
     * @return the calculated expression
     */
    public long calcPrefixExpr(ArrayList<Object> arrayList){
        Stack <Long> myStack = new Stack<>();
        for(int i = arrayList.size() - 1; i >= 0; i--){
            String item = String.valueOf(arrayList.get(i));
            Logger.debug("", "item: " + item);
            if(isInt(item)) {
                myStack.push(Long.parseLong(item));
            }
            else{
                long operand1 = myStack.pop();
                long operand2 = myStack.pop();

                switch(item){
                    case "+":
                        myStack.push(operand1 + operand2);
                        break;
                    case "-":
                        myStack.push(operand1 - operand2);
                        break;
                    case "*":
                        myStack.push(operand1 * operand2);
                        break;
                    case "/":
                        if(operand2 == 0){
                            Logger.error("","Cannot divide by zero");
                            throw new IllegalArgumentException();
                        }
                        myStack.push(operand1 / operand2);
                        break;
                }
            }
        }
        Logger.info("","Calculating Prefix Expression: " + myStack.peek());
        return myStack.peek();
    }


    /**
     * Truncates
     * <br />
     * @param arrayList is the object arraylist. Example: "let(a, 5, let(b, mult(a, 10), add(b, a)))";
     * @return arraylist which variables are assigned with a number. Example: [+,50,5]
     */
    public ArrayList<Object> assignData(ArrayList<Object> arrayList){
        for(int i = 0; i<arrayList.size(); i++){
            if(arrayList.size() == 3) break;

            String operand = String.valueOf(arrayList.get(1) );
            String unknown = String.valueOf(arrayList.get(2) );
            Logger.info("" ,"operand: " + operand);
            Logger.info("" ,"unknown: " + unknown);

            if(isInt(unknown) ){
                arrayList = new ArrayList<> (arrayList.subList(3,arrayList.size()) );
                arrayList = updateArrayList(arrayList, operand, unknown);
            }
            else{
                int fromIndex = 2;
                int toIndex   = 5;
                ArrayList<Object> tempArrList = new ArrayList<>(arrayList.subList(fromIndex,toIndex));
                long itemCalculated = calcPrefixExpr(tempArrList);
                arrayList = new ArrayList<> (arrayList.subList(5,arrayList.size()) );
                arrayList = updateArrayList(arrayList, operand, itemCalculated);
            }
        }
        Logger.info("","Prefix-Expressions: " + arrayList);
        return arrayList;
    }

    /**
     * Assigns a given constant to variables in the expression.
     * For example:
     * input arraylist:   [let, b, 10, +, b, b]
     * will return an updated arraylist: [let, b, 10, +, <b>10</b>, <b>10</b>].
     * <br />
     * This method reuses the same ArrayList instance. Size of updated arraylist is unchanged
     * <br />
     * @param arrayList  is the object arraylist. Example: [let, b, *, 5, 10, +, b, 5]
     * @param operand is the variable with unknown value at the moment before calculation
     * @param item is the number used to replace the operand
     * @return the update object arraylist
     */
    public ArrayList<Object> updateArrayList(ArrayList<Object> arrayList, Object operand, Object item){
        for(int index = 0; index< arrayList.size(); index++){
                if(arrayList.get(index).equals(operand) ){
                arrayList.set(index, item);
            }
        }
        Logger.info("","arraylist updated: " + arrayList);
        return arrayList;
    }

    /**
     * Assumptions :
     * The input format must be correct.
     * Input texts must be "let","add","sub","mult" and/or "div"
     * The bracket must match
     * The numerical input must be valid
     * Each function accepts exactly 2 numerical expression
     * <br/>
     * Example input string:
     * "add(1, 2)"
     * "add(1, mult(2, 3))"
     * "let(a, 5, let(b, mult(a, 10), add(b, a)))"
     * <br/>
     *
     */
    public static void main(String [] args){

        if (args.length == 0) {
            Logger.error("Argument", "No arguments are given.");
            throw new RuntimeException();
        }

        if (args.length >= 2) {
            String verbosity = args[1];
            switch(verbosity.toLowerCase()) {
                case "info":
                    Logger.logLevel = Logger.INFO;
                    break;
                case "debug":
                    Logger.logLevel = Logger.DEBUG;
                    break;
                case "error":
                    Logger.logLevel = Logger.ERROR;
                    break;
                default:
                    Logger.error("Argument", "Unknown verbosity: " + verbosity);
                    throw new RuntimeException();
            }
        }

        SynopsysCommandLineCal instance = new SynopsysCommandLineCal();
        try{
            instance.accessStringData(args[0]);
        }
        catch (Exception e){
            Logger.error(" ","Format not accepted");
        }
    }

}