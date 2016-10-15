/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doubotis.mappicturegenerator.layers;

import com.doubotis.mappicturegenerator.StaticMap;
import java.awt.Graphics2D;

/**
 *
 * @author Christophe
 */
public interface Layer {
    
    public void draw(Graphics2D graphics, StaticMap mp);
    
}
