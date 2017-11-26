/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.staticmap.layers;

import com.doubotis.staticmap.layers.Layer;

/**
 *
 * @author cbrasseur
 */
public abstract class Control implements Layer {
    
    public enum Position {
        TOP_LEFT,
        TOP,
        TOP_RIGHT,
        LEFT,
        RIGHT,
        BOTTOM_LEFT,
        BOTTOM,
        BOTTOM_RIGHT
    }
}
