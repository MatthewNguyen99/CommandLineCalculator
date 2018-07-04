# CommandLineCalculator
Readme:

PART 1: REQUIREMENT
Functional Requirements
Write a calculator program in Java that evaluates expressions in a very simple integer expression language. The program takes an input on the command line, computes the result, and prints it to the console.  For example:
% java calculator.Main "add(2, 2)"
4
Few more examples:
Input	Output
add(1, 2)	3
add(1, mult(2, 3))	7
mult(add(2, 2), div(9, 3))	12
let(a, 5, add(a, a))	10
let(a, 5, let(b, mult(a, 10), add(b, a)))	55
let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))	40
An expression is one of the of the following:
•	Numbers: integers between Integer.MIN_VALUE and Integer.MAX_VALUE
•	Variables: strings of characters, where each character is one of a-z, A-Z
•	Arithmetic functions: add, sub, mult, div, each taking two arbitrary expressions as arguments.  In other words, each argument may be any of the expressions on this list.
•	A “let” operator for assigning values to variables:
	let(<variable name>, <value expression>, <expression where variable is used>)
As with arithmetic functions, the value expression and the expression where the variable is used may be an arbitrary expression from this list. 
Logging
Implement a logging layer to log all relevant information. Manage at least 3 levels of verbosity: INFO, ERROR, and DEBUG.  Allow verbosity to be set via a command-line option.
Build
Create a Maven or Gradle build definition so your project may be built in any standard Java environment.
PART 2: INTRUCTIONS
This program was developed on IntelliJ IDEA platform
To run the app, download or clone to your computer. Then open command promp, cd to directory of file
1.	In command promp, type:
~/Desktop/USB/SynopsysCalculator$ ./gradlew build
2.	Then cd to directory as follows:
~/Desktop/USB/SynopsysCalculator/build/classes/java/main$
3.	To run, type one of the options:
~/Desktop/USB/SynopsysCalculator/build/classes/java/main$ java SynopsysCommandLineCal "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))" info

~/Desktop/USB/SynopsysCalculator/build/classes/java/main$ java SynopsysCommandLineCal "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))" debug

~/Desktop/USB/SynopsysCalculator/build/classes/java/main$ java SynopsysCommandLineCal "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))" error


Or choose second commands
1.	In command promp, type:
~/Desktop/USB/SynopsysCalculator$ ./gradlew jar
2.	Then cd to directory as follows:
~/Desktop/USB/SynopsysCalculator/build/libs$
~/Desktop/USB/SynopsysCalculator/build/libs$ java -jar
3.	To run, type one of the options:
~/Desktop/USB/SynopsysCalculator/build/libs$ java -jar SynopsysCalculator.jar  "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))" info

~/Desktop/USB/SynopsysCalculator/build/libs$ java -jar SynopsysCalculator.jar  "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))" debug

~/Desktop/USB/SynopsysCalculator/build/libs$ java -jar SynopsysCalculator.jar  "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))" error

Where:
project name: SynopsysCalculator
class name: 	SynopsysCommandLineCal
verbosity : info debug error
example of expressions: "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))" 

PART 3: EXPLAINATION OF METHODS
The program uses Prefix Expression for calculation
1.	public int accessStringData(String strInput)
    *  First using convertToPrefixSymbol() to convert String input to Arraylist input
    *  1. if mainlist format is:
    *          [let, a, let, b, 10, +, b, b, let, b, 20, +, a, b]
    *          1. get sublist from 2-8, which is [let, b, 10, +, b, b]
    *          2. use calcAssignedData() to calculate value of that sublist, which b = 20
    *          3. delete sublist from main list, replace that sublist by the calculated value
    *              mainlist becomes [let, a, 20, let, b, 20, +, a, b]
    *          4. use assignData() to assign value,  mainlist becomes [+, 20, 20]
    *          5. use calcPrefixExpr() to calculate mainlist, which = 40
    *  2. if mainlist format is:
    *          [let, a, 5, let, b, *, a, 10, +, b, a]
    *          1. use assignData() to assign the value 5 to 'a', 
    *              mainlist becomes [let, b, *, 5, 10, +, b, 5]
    *              a. keep looping, use calcPrefixExpr() to check the next 'let',
    *              b. find next 'let' for 'b', get sublist from index 2 to 5, which is [*,5,10],
    *                  use calPrefixExpr() to get value of that sublist, which is b =  50
    *              c. use assignData() to assign value,  mainlist becomes [+, 50, 5]
    *              d. use calcPrefixExpr() to calculate results, which = 55
    *  3.else:
    *          1. calculate data using prefix-Expressions

2.	public ArrayList<Object> convertToPrefixSymbol(String strInput)
    * convert string to prefix-Expressions
    * "add"   = "+"
    * "sub"   = "-"
    * "mult" = "*"
    * "div"    = "/"
    * example:
    *         string input:   "mult(add(2, 2), div(9, 3))"  
    *          becomes arraylist: [*, +, 2, 2, /, 9, 3]
    * example:
    *          string input:   "let(a, 5, add(a, a))"        
    *          becomes arraylist: [let, a, 5, +, a, a]

3.	public boolean isInt(String strNum)
    * if the string can be converted to Int, then return true
    * else return false



4.	public int calcPrefixExpr(ArrayList<Object> arrayList)
    * calculate data using prefix-Expressions:
    *   1. loop arrayList backward to beginning
    *        if item is  numerical, add to stack
    *        if item is one of + - * / then pop stack twice, do calculation, push result back  
    * 	to stack
    *   3. peek() to see result.

5.	public ArrayList<Object> assignData(ArrayList<Object> arrayList)
    * loop the arraylist  
    *   if at index 2, the unknown is int. For example: [let, b, 10, +, b, b] 
    *       a. get sublist from index 3 to the end, which is [+,b,b]
    *       b. use updateArrayList() to that sublist, set the unknown to b, which is 
  		[+,10,10]
   *   else if at index 2, the unknown is not an int.  For example [let, b, *, 5, 10, +, b, 5]
   *       a. get sublist from index 2 to 5, which is [*,5,10]
   *       b. use calcPrefixExpr() to calculate that sublist, which b = 50
   *       c. get sublist from 5 to end, which is [+,b,5]
   *       d. use assignData() to assign value, which is [+,50,5]

6.	public ArrayList<Object> updateArrayList(ArrayList<Object> arrayList, Object operand, Object item)
         * loop through arraylist, if char in arraylist equals char, set char equals item
  * example: let(a,5.....(a,b)) then 'a' will be replaced by number 5 in the whole list

JUNIT TEST
 	Testing 18 different test cases.  All tests passed.
