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

package at.mukprojects.imageloader.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mukprojects.imageloader.image.Image;
import at.mukprojects.imageloader.image.ImageList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class represents the FileImageLoader task.
 * 
 * @author Mathias Markl
 */
public class FileImageTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileImageTask.class);

    private PApplet applet;
    private String searchParam;
    private ImageList imageList;

    private boolean runOnce;
    private long delay;
    private boolean lazyLoad;

    private volatile boolean running;

    /**
     * Constructs a new FileImageTask.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param searchParam
     *            The search parameter.
     * @param imageList
     *            The ImageList object.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @param lazyLoad
     *            Indicates if the load process is lazy or not. Use lazy mode to
     *            save memory space.
     */
    public FileImageTask(PApplet applet, String searchParam, ImageList imageList, boolean runOnce, long delay,
	    boolean lazyLoad) {
	this.applet = applet;
	this.searchParam = searchParam;
	this.imageList = imageList;

	this.runOnce = runOnce;
	this.delay = delay;
	this.lazyLoad = lazyLoad;

	running = true;
    }

    /**
     * Stops the task.
     */
    public void stop() {
	running = false;
    }

    @Override
    public void run() {
	logger.info("Task is running...");

	while (running) {
	    try {
		logger.debug("Task loads data.");

		List<File> files = getFiles(searchParam);

		for (File file : files) {
		    String id = "File#" + file.getAbsolutePath();

		    String imgInfo = "";
		    imgInfo += "Name: " + file.getName() + "\n";
		    imgInfo += "Last Modified: " + new Date(file.lastModified()).toString();

		    long timestamp = new Date().getTime();
		    String imgUrl = file.getAbsolutePath();

		    PImage img = null;
		    if (!lazyLoad) {
			img = applet.loadImage(imgUrl);
		    }

		    imageList.addImage(new Image(id, imgInfo, timestamp, imgUrl, img));
		}

		if (runOnce) {
		    logger.debug("Task is finished.");
		    running = false;
		} else {
		    logger.debug("Task is delayed...");
		    Thread.sleep(delay);
		}
	    } catch (InterruptedException | IOException e) {
		logger.error("An error occured. The task will be stopped.", e);
		running = false;
	    }
	}

	logger.info("Task has stopped.");
    }

    private List<File> getFiles(String path) throws IOException {
	List<File> files = new ArrayList<File>();
	File pathLocation = new File(path);

	if (!pathLocation.exists()) {
	    throw new IOException("Path does not exist.");
	}

	if (pathLocation.isFile()) {
	    if (pathLocation.getName().endsWith(".gif") || pathLocation.getName().endsWith(".jpg")
		    || pathLocation.getName().endsWith(".tga") || pathLocation.getName().endsWith(".png")) {
		files.add(pathLocation);
	    }
	} else if (pathLocation.isDirectory()) {
	    File[] fileArray = pathLocation.listFiles();

	    for (int i = 0; i < fileArray.length; i++) {
		files.addAll(getFiles(fileArray[i].getAbsolutePath()));
	    }
	}

	return files;
    }
}
