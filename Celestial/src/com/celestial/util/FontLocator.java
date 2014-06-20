package com.celestial.util;

import java.awt.Color;
import java.awt.Font;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ClasspathLocator;

/**
* Enables truetype fonts to be loaded using a location string.
*
* String format:
*  font(size):font_resource_name
*  font(size,color):font_resource_name
*  font(size,color,style):font_resource_name
*
* Color is expressed in hex (0xRRGGBB). Style can be BOLD or ITALIC. Default
* color is black.
*
* Example: font(15,0xFF0000,BOLD):Fonts/MyFont.ttf
*
* Registering the locator: <code>assetManager.registerLocator("", FontLocator.class);</code>
* Don’t forget to register the TTF FontLoader:
* <code>assetManager.registerLoader(TTFFontLoader.class.getName(), "ttf");</code>
*
* @author Stan Hebben
*/
public class FontLocator implements AssetLocator {
private AssetLocator base;

public FontLocator() {
base = new ClasspathLocator();
}

public void setRootPath(String rootPath) {
base.setRootPath(rootPath);
}

public AssetInfo locate(AssetManager manager, AssetKey key) {
if (!key.getName().startsWith("font(")) {
return null;
} else {
/* Separate the query string from the asset name */
int ix = key.getName().indexOf(':');
String query = key.getName().substring(5, ix - 1);
String name = key.getName().substring(ix + 1);

/* Split and process the query parameters */
String[] params = query.split(",");
int size = Integer.parseInt(params[0].trim());
Color color;
int style = Font.PLAIN;
if (params.length > 1) {
color = new Color(Integer.decode(params[1].trim()));

if (params.length > 2) {
String styleString = params[2].trim();
if (styleString.toLowerCase().equals("bold")) {
style = Font.BOLD;
} else if (styleString.toLowerCase().equals("italic")) {
style = Font.ITALIC;
}
}
} else {
color = Color.BLACK;
}

/* Delegates the font location to the actualy loader */
FontKey newkey = new FontKey(name, Font.TRUETYPE_FONT, color, size, style);
return base.locate(manager, newkey);
}
}
}