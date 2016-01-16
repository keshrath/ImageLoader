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

import java.util.*;

import at.mukprojects.imageloader.*;
import at.mukprojects.imageloader.file.*;
import at.mukprojects.imageloader.image.*;

ImageLoader loader;
ImageList list;
Image img;

void setup() {
  size(800, 450);
  noLoop();

  loader = new FileLoader(this);
  list = loader.start(dataPath(""));
  
  // Manually add Images to the list.
  String myID = "MyID01";
  String myImageInfo = "MyImage";
  long timestamp = new Date().getTime();
  String imgUrl = "https://cloud.githubusercontent.com/assets/4851083/12145974/82de1be2-b491-11e5-8644-37cb69b6d7e0.jpg";
  PImage img = loadImage(imgUrl);
  
  Image myImage = new Image(myID, myImageInfo, timestamp, imgUrl, img);
  list.addImage(myImage);
}

void draw() {
  println("Size: " + list.size());
  
  println("getImage:\n " + list.getImage("MyID01"));
  println("MostRecentImage:\n " + list.getMostRecentImage());
  println("getRandom:\n " + list.getRandom());
  
  list.clearList();
  
  println("Size: " + list.size());
  
}