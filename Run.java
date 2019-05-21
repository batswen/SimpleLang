/**
 * Rosetta code lexer/parser/interpreter
 * in a single jar
 * 
 * mostly translated from python source
 */

package interpreter;

import static interpreter.Lexer.error;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                File f = new File(args[0]);
                Scanner s = new Scanner(f);
                StringBuilder source = new StringBuilder();
                source.append(" ");
                while (s.hasNext()) {
                    source.append(s.nextLine());
                    source.append("\n");
                }
                
                Lexer l = new Lexer(source.toString());
                Parser p = new Parser(l.getTokens());
                Interpreter i = new Interpreter(p.parse());
                
            } catch(FileNotFoundException e) {
                error(-1, -1, "Exception: " + e.getMessage());
            } catch (Exception e) {
                error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            error(-1, -1, "No args");
        }
    }
}
