package com.pennywise.findwords.core.logic;

/**
 * Owner is anybody who owns a color.
 * In a two player game the owners of both the colors will be Owner.HUMAN
 * In a single player game the owner of one color will be Owner.HUMAN and the owner of the other will be Owner.ROBOT.
 * @author apurv
 */
public enum Owner{
    HUMAN,
    ROBOT
}
