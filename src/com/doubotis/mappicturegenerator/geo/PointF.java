/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.geo;

/**
 *
 * @author Christophe
 */
public final class PointF 
{
    public double x;
    public double y;

    public PointF(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{x: " + this.x + ", y: " + this.y + "}";
    }
    
    
}
