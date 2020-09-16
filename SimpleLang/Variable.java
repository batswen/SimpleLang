/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.batswen.simplelang;
enum VarType {
    NONE, INT, STRING
}
/**
 *
 * @author Swen
 */
public class Variable {
    private VarType vartype;
    private String svalue;
    private int ivalue;
    Variable(VarType vartype) {
        this.vartype = vartype;
    }
    public void setStrVar(String value) {
        this.svalue = value;
    }
    public String getStrVar() {
        return this.svalue;
    }
    public void setIntVar(int value) {
        this.ivalue = value;
    }
    public int getIntVar() {
        return this.ivalue;
    }
}
