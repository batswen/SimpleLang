package com.batswen.simplelang;

import java.util.Map;
import java.util.HashMap;

class Interpreter {
    Map<String, Variable> globals;
    /*
    List<Variable> vars; = new ArrayList<>();
    List<String> strings; = new ArrayList<>();
    */
    Node n;

    Interpreter(Node n) throws Exception {
        this.n = n;
        this.globals = new HashMap<>();
        interpret(this.n);
    }
    
    static String str(String s) {
        String result = "";
        int i = 0;
        s = s.replace("\"", "");
        while (i < s.length()) {
            if (s.charAt(i) == '\\' && i + 1 < s.length()) {
                if (s.charAt(i + 1) == 'n') {
                    result += '\n';
                    i += 2;
                } else if (s.charAt(i) == '\\') {
                    result += '\\';
                    i += 2;
                } 
            } else {
                result += s.charAt(i);
                i++;
            }
        }
        return result;
	}
    static boolean itob(int i) {
        return i != 0;
    }
    static int btoi(boolean b) {
        return b ? 1 : 0;
    }
    final int interpret(Node n) throws Exception {
        if (n == null) {
            return 0;
        }
        switch (n.nt) {
            case nd_Integer:
                return Integer.parseInt(n.value);
            case nd_Ident:
                if (globals.containsKey(n.value)) {
                    return globals.get(n.value).getIntVar();
                } else {
                    throw new Exception("Variable " + n.value + " not found!");
                }
            case nd_String:
                return 1;//n.value;
            case nd_Assign:
                Variable var;
                String name = n.left.value;
                if (globals.containsKey(name)) {
                    var = globals.get(name);
                } else {
                    var = new Variable(VarType.INT);
                    globals.put(name, var);
                }
                var.setIntVar(interpret(n.right));
                return 0;
            case nd_Add:
                return interpret(n.left) + interpret(n.right);
            case nd_Sub:
                return interpret(n.left) - interpret(n.right);
            case nd_Mul:
                return interpret(n.left) * interpret(n.right);
            case nd_Div:
                return interpret(n.left) / interpret(n.right);
            case nd_Mod:
                return interpret(n.left) % interpret(n.right);
            case nd_Lss:
                return btoi(interpret(n.left) < interpret(n.right));
            case nd_Leq:
                return btoi(interpret(n.left) <= interpret(n.right));
            case nd_Gtr:
                return btoi(interpret(n.left) > interpret(n.right));
            case nd_Geq:
                return btoi(interpret(n.left) >= interpret(n.right));
            case nd_Eql:
                return btoi(interpret(n.left) == interpret(n.right));
            case nd_Neq:
                return btoi(interpret(n.left) != interpret(n.right));
            case nd_And:
                return btoi(itob(interpret(n.left)) && itob(interpret(n.right)));
            case nd_Or:
                return btoi(itob(interpret(n.left)) || itob(interpret(n.right)));
            case nd_Not:
                if (interpret(n.left) == 0) {
                    return 1;
                } else {
                    return 0;
                }
            case nd_Negate:
                return -interpret(n.left);
            case nd_If:
                if (interpret(n.left) != 0) {
                    interpret(n.right.left);
                } else {
                    interpret(n.right.right);
                }
                return 0;
            case nd_While:
                while (interpret(n.left) != 0) {
                    interpret(n.right);
                }
                return 0;
            case nd_Prtc:
                System.out.printf("%c", (char)interpret(n.left));
                return 0;
            case nd_Prti:
                System.out.printf("%d", interpret(n.left));
                return 0;
            case nd_Prts:
                System.out.print(str(n.left.value));//interpret(n.left));
                return 0;
            case nd_Sequence:
                interpret(n.left);
                interpret(n.right);
                return 0;
            default:
                throw new Exception("Error: '" + n.nt + "' found, expecting operator");
        }
    }
}
