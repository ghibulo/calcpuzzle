package cz.ghibulo.kalkulacka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by ghibulo on 15.3.15.
 */
public class ExpressionParser {
     // Associativity constants for operators
    private static final int LEFT_ASSOC  = 0;
    private static final int RIGHT_ASSOC = 1;
    public String expr;

    public ExpressionParser() {

        expr = "";

    }


    // Operators
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
    static
    {
        // Map<"token", []{precendence, associativity, num-operands}>
        OPERATORS.put("+", new int[] { 0, LEFT_ASSOC, 2 });
        OPERATORS.put("-", new int[] { 0, LEFT_ASSOC, 2 });
        OPERATORS.put("*", new int[] { 5, LEFT_ASSOC, 2 });
        OPERATORS.put("/", new int[] { 5, LEFT_ASSOC, 2 });
        OPERATORS.put("od", new int[] { 10, LEFT_ASSOC, 1 }); //odmocnina
        OPERATORS.put("cs", new int[] { 10, LEFT_ASSOC, 1 }); //zmena znamenka
        OPERATORS.put("m", new int[] { 10, LEFT_ASSOC, 2 }); //mocnina

    }

    public void addTokenToExpr(String token) {
        if ((isOperator(token)))
            expr += (" "+token+" ");
        else
            if (token.equals(")")) {
                expr += " )";
            } else
            if (token.equals("(")) {
                expr += "( ";
            } else
                expr += token;
    }

    public void addDoubleToExpr(double cislo) {
        expr += (" "+cislo+" ");
    }

    public double dejVysledek() {

        String[] output = infixToRPN(expr.split(" "));
        double vysledek = RPNtoDouble( output );
        return vysledek;
    }

    public void smazVstup() {

        expr = "";
    }

    public void uzavorkuj() {
        expr = "( "+expr+" )";
    }

    // Test if token is an operator
    private static boolean isOperator(String token)
    {
        return OPERATORS.containsKey(token);
    }

    // Test associativity of operator token
    private static boolean isAssociative(String token, int type)
    {
        if (!isOperator(token))
        {
            throw new IllegalArgumentException("Invalid token: " + token);
        }

        if (OPERATORS.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    // Compare precedence of operators.
    private static final int cmpPrecedence(String token1, String token2)
    {
        if (!isOperator(token1) || !isOperator(token2))
        {
            throw new IllegalArgumentException("Invalid tokens: " + token1
                    + " " + token2);
        }
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }

    // Convert infix expression format into reverse Polish notation
    public static String[] infixToRPN(String[] inputTokens)
    {
        ArrayList<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();

        // For each token
        for (String token : inputTokens)
        {
            // If token is an operator
            if (isOperator(token))
            {
                // While stack not empty AND stack top element
                // is an operator
                while (!stack.empty() && isOperator(stack.peek()))
                {
                    if ((isAssociative(token, LEFT_ASSOC)         &&
                         cmpPrecedence(token, stack.peek()) <= 0) ||
                        (isAssociative(token, RIGHT_ASSOC)        &&
                         cmpPrecedence(token, stack.peek()) < 0))
                    {
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }
                // Push the new operator on the stack
                stack.push(token);
            }
            // If token is a left bracket '('
            else if (token.equals("("))
            {
                stack.push(token);  //
            }
            // If token is a right bracket ')'
            else if (token.equals(")"))
            {
                while (!stack.empty() && !stack.peek().equals("("))
                {
                    out.add(stack.pop());
                }
                stack.pop();
            }
            // If token is a number
            else
            {
                if (!(token.equals(""))) out.add(token);
            }
        }
        while (!stack.empty())
        {
            out.add(stack.pop());
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

    public static double RPNtoDouble(String[] tokens)
    {
        Stack<String> stack = new Stack<String>();

        // For each token
        for (String token : tokens)
        {
            // If the token is a value push it onto the stack
            if (!isOperator(token))
            {
                stack.push(token);
            }
            else
            {
                Double result =0.0;
                if (OPERATORS.get(token)[2]==1) {
                //if (token.equals("od")) { //zatim jedina unarni operace
                    Double d1 = Double.valueOf( stack.pop() );
                    result = token.compareTo("od") == 0 ? Math.sqrt(d1) :
                             -d1;


                } else { //zatim ternarni operaci nemame
                    // Token is bin-operator: pop top two entries
                    Double d2 = Double.valueOf(stack.pop());
                    Double d1 = Double.valueOf(stack.pop());

                    //Get the result
                    result = token.compareTo("+") == 0 ? d1 + d2 :
                             token.compareTo("-") == 0 ? d1 - d2 :
                             token.compareTo("*") == 0 ? d1 * d2 :
                             token.compareTo("/") == 0 ? d1 / d2 :
                             Math.pow(d1,d2);


                }

                // Push result onto stack
                stack.push(String.valueOf(result));

            }
        }

        return Double.valueOf(stack.pop());
    }




}
