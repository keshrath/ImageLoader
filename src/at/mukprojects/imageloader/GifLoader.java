/**
 * This code is copyright (c) Mathias Markl 2016
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package at.mukprojects.imageloader;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import at.mukprojects.imageloader.gif.GifList;
import gifAnimation.GifDecoder;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Base class for any GifLoader.
 *
 * @author Mathias Markl
 */
public abstract class GifLoader {

    /**
     * Static PApplet which gets initialized by the constructor.
     */
    private static PApplet appletLoader;

    /**
     * Loads an image file.
     * 
     * @param file
     *            The image file.
     * @return The image as an PImage object.
     * @throws IOException
     *             Is thrown in case an error occurs.
     */
    public static PImage loadImage(String file) throws IOException {
	if (appletLoader == null) {
	    throw new IOException("The PApplet wasn't initialized by the constructor."
		    + " You need to initialize a GifLoader object first.");
	}
	return appletLoader.loadImage(file);
    }

    /**
     * Loads a GIF file.
     * 
     * @param file
     *            The GIF file.
     * @return The GIF as an PImage[] array.
     * @throws IOException
     *             Is thrown in case an error occurs.
     */
    public static PImage[] loadGif(String fileURL) throws IOException {
	URL u = new URL(fileURL);
	HttpURLConnection uc = (HttpURLConnection) u.openConnection();

	GifDecoder decoder = new GifDecoder();
	decoder.read(new BufferedInputStream(uc.getInputStream()));

	int n = decoder.getFrameCount();

	PImage[] frames = new PImage[n];

	for (int j = 0; j < n; j++) {
	    BufferedImage frame = decoder.getFrame(j);
	    frames[j] = new PImage(frame.getWidth(), frame.getHeight(), PImage.ARGB);
	    System.arraycopy(frame.getRGB(0, 0, frame.getWidth(), frame.getHeight(), null, 0, frame.getWidth()), 0,
		    frames[j].pixels, 0, frame.getWidth() * frame.getHeight());
	}

	return frames;
    }

    /**
     * Logger Settings
     */
    private static final String PATTERN = "%5p (%C{1}) - %m%n";
    private static final String DEFAULT_APPENDER = "DefaultConsoleAppender";

    /**
     * The Processing PApplet.
     */
    protected PApplet applet;

    /**
     * Constructs a new ImageLoader.
     * 
     * @param applet
     *            The Processing PApplet.
     */
    public GifLoader(PApplet applet) {
	appletLoader = applet;
	this.applet = applet;

	/*
	 * Logger configuration
	 */
	if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
	    Logger.getRootLogger().setLevel(Level.INFO);

	    ConsoleAppender appender = new ConsoleAppender(new PatternLayout(PATTERN));
	    appender.setName(DEFAULT_APPENDER);
	    Logger.getRootLogger().addAppender(appender);
	}
    }

    /**
     * Starts the loader.
     * 
     * @param searchParam
     *            The search parameter.
     * @return The used ImageList.
     */
    public GifList start(String searchParam) {
	return start(searchParam, new GifList(), false, 60 * 1000, true);
    }

    /**
     * Starts the loader.
     * 
     * @param searchParam
     *            The search parameter.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @return The used ImageList.
     */
    public GifList start(String searchParam, boolean runOnce, long delay) {
	return start(searchParam, new GifList(), runOnce, delay, true);
    }

    /**
     * Starts the loader.
     * 
     * @param searchParam
     *            The search parameter.
     * @param gifList
     *            The GifList, which should be used.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @param lazyLoad
     *            Indicates if the load process is lazy or not. Use lazy mode to
     *            save memory space.
     * @return The used ImageList.
     */
    public abstract GifList start(String searchParam, GifList gifList, boolean runOnce, long delay, boolean lazyLoad);

    /**
     * Restarts the loader.
     * 
     * @param searchParam
     *            The search parameter.
     * @return The used ImageList.
     */
    public GifList restart(String searchParam) {
	return restart(searchParam, new GifList(), false, 60 * 1000, true);
    }

    /**
     * Restarts the loader.
     * 
     * @param searchParam
     *            The search parameter.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @return The used ImageList.
     */
    public GifList restart(String searchParam, boolean runOnce, long delay) {
	return restart(searchParam, new GifList(), runOnce, delay, true);
    }

    /**
     * Restarts the loader.
     * 
     * @param searchParam
     *            The search parameter.
     * @param gifList
     *            The GifList, which should be used.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @param lazyLoad
     *            Indicates if the load process is lazy or not. Use lazy mode to
     *            save memory space.
     * @return The used ImageList.
     */
    public abstract GifList restart(String searchParam, GifList gifList, boolean runOnce, long delay, boolean lazyLoad);

    /**
     * Stops the loader.
     */
    public abstract void stop();
}
