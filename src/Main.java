import java.util.Stack;
import java.util.Scanner;

/**
 * LL1 grammar
 * G -> E$
 * E -> TK
 * K-> +TK | e
 * T -> FH
 * H-> *FH | e
 * F -> (E)  | a
 * <p>
 * Parsing table
 * ====================================================================
 * a        +         *         (         )         $
 * --------------------------------------------------------------------
 * G | G->E$  |         |         | G->E$   |         |        |
 * --------------------------------------------------------------------
 * E | E->TK  |         |         | E->TK   |         |        |
 * --------------------------------------------------------------------
 * K |        | K->+TK  |         |         | K->     | K->    |
 * --------------------------------------------------------------------
 * T | T->FH  |         |         | T->FH   |         |        |
 * --------------------------------------------------------------------
 * H |        | H->     | H->*FH  |         | H->     | H->    |
 * --------------------------------------------------------------------
 * F | F->a   |         |         | F->(E)  |         |        |
 * --------------------------------------------------------------------
 * ====================================================================
 **/
public class Main {
    //input
    public String input = "";
    private int index = -1;
    //Stack
    Stack<String> s = new Stack<String>();
    //Table of rules
    String[][] table = {
            {"E$", null, null, "E$", null, null},
            {"TK", null, null, "TK", null, ""},
            {null, "+TK", null, null, "", ""},
            {"FH", null, null, "FH", null, null},
            {null, "", "*FH", null, "", ""},
            {"a", null, null, "(E)", null, null}
    };

    String[] nonTers = {"G", "E", "K", "T", "H", "F"};    //Non Terminals
    String[] terminals = {"a", "+", "*", "(", ")", "$"};    //Terminals

    public Main(String in) {
        this.input = in;
    }

    private void pushRule(String rule) {
        for (int i = rule.length() - 1; i >= 0; i--) {
            char ch = rule.charAt(i);
            String str = String.valueOf(ch);
            push(str);
        }
    }

    //algorithm
    public void algorithm() {
        push(this.input.charAt(0) + "");
        push("G");

        //Read first token from input
        String token = read();
        String top = null;

        do {
            top = this.pop();
            //if top is non-terminal
            if (isNonTerminal(top)) {
                String rule = this.getRule(top, token);
                this.pushRule(rule);
            }

            //if top is terminal
            else if (isTerminal(top)) {
                if (!top.equals(token)) {
                    error("This token is not correct by Grammar rule. Token : (" + token + ")");
                } else {
                    System.out.println("Terminal read : " + token);
                    token = read();
                }
            } else {
                error("Never happens, because top : ( " + top + " )");
            }

            //end of input expression
            if (token.equals("$")) {
                break;
            }
            //if top is terminal
        } while (true); //out of the loop when $

        //accept
        if (token.equals("$")) {
            System.out.println("\nInput is ACCEPTED by LL1 Parser");
        } else {
            System.out.println("\nInput is NOT ACCEPTED by LL1 Parser");
        }
    }

    private boolean isTerminal(String s) {
        for (int i = 0; i < this.terminals.length; i++) {
            if (s.equals(this.terminals[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isNonTerminal(String s) {
        for (int i = 0; i < this.nonTers.length; i++) {
            if (s.equals(this.nonTers[i])) {
                return true;
            }
        }
        return false;
    }

    private String read() {
        index++;
        char ch = this.input.charAt(index);
        String str = String.valueOf(ch);
        return str;
    }

    private void push(String s) {
        this.s.push(s);
    }

    private String pop() {
        return this.s.pop();
    }

    private void error(String message) {
        System.out.println(message);
        throw new RuntimeException(message);
    }

    public String getRule(String non, String term) {
        int row = getnonTermIndex(non);
        int column = getTermIndex(term);
        String rule = this.table[row][column];
        if (rule == null) {
            error("There is no rule by this Non-Terminal " + non);
        }
        return rule;
    }

    private int getnonTermIndex(String non) {
        for (int i = 0; i < this.nonTers.length; i++) {
            if (non.equals(this.nonTers[i])) {
                return i;
            }
        }
        error(non + " is not NonTerminal");
        return -1;
    }

    private int getTermIndex(String term) {
        for (int i = 0; i < this.terminals.length; i++) {
            if (term.equals(this.terminals[i])) {
                return i;
            }
        }
        error(term + " is not Terminal");
        return -1;
    }

    //driver function
    public static void main(String[] args) {
        System.out.println("\nPlease add $ symbol at end of input expression");
        String input;
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Input Expression: "); //a+a*(a+a)+a*a$
        input = sc.nextLine();
        Main parser = new Main(input);    //parser object
        parser.algorithm();
    }
}