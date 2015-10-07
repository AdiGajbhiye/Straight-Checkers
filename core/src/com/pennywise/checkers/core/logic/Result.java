package com.pennywise.checkers.core.logic;

import com.pennywise.checkers.core.logic.enums.ReturnCode;

import java.util.Vector;

/**
 * Created by joshua.nabongo on 10/3/2015.
 */
public class Result {
    ReturnCode retCode;
    String message;
    Vector<Step> steps;

    public Result(ReturnCode retCode, String message, Vector<Step> steps) {
        this.retCode = retCode;
        this.message = message;
        this.steps = steps;
    }
}
