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

package at.mukprojects.imageloader.giphy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.imageloader.GifLoader;
import at.mukprojects.imageloader.gif.GifList;
import processing.core.PApplet;

/**
 * This class represents the GiphyLoader.
 * 
 * @author Mathias Markl
 */
public class GiphyLoader extends GifLoader {

    private static final Logger logger = LoggerFactory.getLogger(GiphyLoader.class);

    private String apiKey;

    private Thread thread = null;
    private GiphyTask runnable = null;

    private Giphy giphy4j;

    /**
     * Constructs a new ImageLoader.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param apiKey
     *            The API key.
     */
    public GiphyLoader(PApplet applet, String apiKey) {
	super(applet);
	this.apiKey = apiKey;
	giphy4j = new Giphy(apiKey);
    }

    @Override
    public GifList start(String searchParam, GifList gifList, boolean runOnce, long delay, boolean lazyLoad) {
	if (thread != null) {
	    logger.info("Loader is already started.");
	    logger.info("The restart method will be used instead.");

	    return restart(searchParam, gifList, runOnce, delay, lazyLoad);
	} else {
	    runnable = new GiphyTask(applet, searchParam, gifList, apiKey, giphy4j, runOnce, delay, lazyLoad);
	    thread = new Thread(runnable, "GiphyTask");

	    logger.info("Starting Thread: " + thread + "...");
	    thread.start();
	    logger.debug(thread + " successfully started.");

	    return gifList;
	}
    }

    @Override
    public GifList restart(String searchParam, GifList gifList, boolean runOnce, long delay, boolean lazyLoad) {
	logger.info("Stopping the current thread: " + thread + "...");
	runnable.stop();
	logger.debug(thread + " successfully stopped.");

	runnable = new GiphyTask(applet, searchParam, gifList, apiKey, giphy4j, runOnce, delay, lazyLoad);
	thread = new Thread(runnable, "GiphyTask");

	logger.info("Starting Thread: " + thread + "...");
	thread.start();
	logger.debug(thread + " successfully started.");

	return gifList;
    }

    @Override
    public void stop() {
	logger.info("Stopping the current thread: " + thread + "...");

	runnable.stop();
	thread = null;
	runnable = null;

	logger.debug(thread + " successfully stopped.");
    }
}
