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

import at.mukprojects.imageloader.*;
import at.mukprojects.imageloader.flickr.*;
import at.mukprojects.imageloader.image.*;

String apiKey = "yourApiKey";
String apiSecret = "yourApiSecret";

ImageLoader loader;
ImageList list;
Image img;

void setup() {
  size(800, 450);

  loader = new FlickrLoader(this, apiKey, apiSecret);
  list = loader.start("sunset beach", false, 60 * 1000);
}

void draw() {
  if (img == null) {
    img = list.getRandom();
  } else {
    image(img.getImg(), 0, 0, width, height);
  }
}

void mousePressed() {
  img = list.getRandom();
}