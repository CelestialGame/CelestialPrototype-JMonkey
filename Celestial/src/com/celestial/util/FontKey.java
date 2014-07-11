package com.celestial.util;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import com.jme3.asset.AssetKey;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

/**
 * A key to find fonts in asset manager.
 * 
 * @author Marcin Roguski
 */
public class FontKey extends AssetKey
{
    /**
     * The type of the font. available values: Font.TRUETYPE_FONT,
     * Font.TYPE1_FONT
     */
    protected int type;
    /** The size of the font (should be greater than zero of course). */
    protected int size;
    /** The color of the font. */
    protected Color color;
    /**
     * The style of the font; available values: Font.ITALIC, Font.PLAIN,
     * Font.BOLD.
     */
    protected int style;
    /**
     * The amount of pixels to be added above the ascent line. Available units:
     * %, px.
     */
    protected String aboveAscentBuffer;
    
    /**
     * The amount of pixels to be added below the descent line. Available units:
     * %, px.
     */
    protected String belowDescentBuffer;
    
    /**
     * Constructor with the aseet name. The constructor does not validate the
     * given data. If the data is wrong it will arise exceptions during font
     * creation. The aboveAscent and belowDescent buffers are both set to 0.
     * 
     * @param fontName
     *            the name of the font asset
     * @param type
     *            the type of the font; available values: Font.TRUETYPE_FONT,
     *            Font.TYPE1_FONT
     * @param color
     *            the color of the font
     * @param size
     *            the size of the font (should be greater than zero of course)
     * @param style
     *            the style of the font; available values: Font.ITALIC,
     *            Font.PLAIN, Font.BOLD
     */
    public FontKey(String fontName, int type, Color color, int size, int style)
    {
	super(fontName);
	this.type = type;
	this.color = color;
	this.size = size;
	this.style = style;
    }
    
    /**
     * Constructor with the aseet name. The constructor does not validate the
     * given data. If the data is wrong it will arise exceptions during font
     * creation.
     * 
     * @param fontName
     *            the name of the font asset
     * @param type
     *            the type of the font; available values: Font.TRUETYPE_FONT,
     *            Font.TYPE1_FONT
     * @param color
     *            the color of the font
     * @param size
     *            the size of the font (should be greater than zero of course)
     * @param style
     *            the style of the font; available values: Font.ITALIC,
     *            Font.PLAIN, Font.BOLD
     * @param aboveAscentBuffer
     *            the amount of pixels to be added above the ascent line
     * @param belowDescentBuffer
     *            the amount of pixels to be added below the descent line
     */
    public FontKey(String fontName, int type, Color color, int size, int style,
	    String aboveAscentBuffer, String belowDescentBuffer)
    {
	this(fontName, type, color, size, style);
	this.aboveAscentBuffer = aboveAscentBuffer;
	this.belowDescentBuffer = belowDescentBuffer;
    }
    
    /**
     * This method returns the type of the font.
     * 
     * @return the type of the font
     */
    public int getType()
    {
	return type;
    }
    
    /**
     * This method returns the color of the font.
     * 
     * @return the color of the font
     */
    public Color getColor()
    {
	return color;
    }
    
    /**
     * This method returns the size of the font.
     * 
     * @return the size of the font
     */
    public int getSize()
    {
	return size;
    }
    
    /**
     * This method returns the style of the font.
     * 
     * @return the style of the font
     */
    public int getStyle()
    {
	return style;
    }
    
    /**
     * This method returns the amount of pixels to be added above the ascent
     * line.
     * 
     * @return the amount of pixels to be added above the ascent line
     */
    public String getAboveAscentBuffer()
    {
	return aboveAscentBuffer;
    }
    
    /**
     * This method returns the amount of pixels to be added below the descent
     * line.
     * 
     * @return the amount of pixels to be added below the descent line
     */
    public String getBelowDescentBuffer()
    {
	return belowDescentBuffer;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException
    {
	super.write(ex);
	OutputCapsule oc = ex.getCapsule(this);
	oc.write(type, "type", Font.TRUETYPE_FONT);
	oc.write(size, "size", 18);
	oc.write(style, "style", Font.PLAIN);
	oc.write(
		new float[] { color.getRed(), color.getGreen(), color.getBlue() },
		"color", null);
    }
    
    @Override
    public void read(JmeImporter im) throws IOException
    {
	super.read(im);
	InputCapsule ic = im.getCapsule(this);
	type = ic.readInt("type", Font.TRUETYPE_FONT);
	size = ic.readInt("size", 18);
	style = ic.readInt("style", Font.PLAIN);
	float[] colorTable = ic.readFloatArray("color", new float[] { 0.0f,
		0.0f, 0.0f });
	color = new Color(colorTable[0], colorTable[1], colorTable[2]);
    }
    
    @Override
    public int hashCode()
    {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((color == null) ? 0 : color.hashCode());
	result = prime * result + size;
	result = prime * result + style;
	result = prime * result + type;
	return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
	if(this == obj)
	{
	    return true;
	}
	if(!super.equals(obj))
	{
	    return false;
	}
	if(getClass() != obj.getClass())
	{
	    return false;
	}
	FontKey other = (FontKey) obj;
	if(color == null)
	{
	    if(other.color != null)
	    {
		return false;
	    }
	}
	else if(!color.equals(other.color))
	{
	    return false;
	}
	if(size != other.size)
	{
	    return false;
	}
	if(style != other.style)
	{
	    return false;
	}
	if(type != other.type)
	{
	    return false;
	}
	return true;
    }
}
