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
import at.mukprojects.imageloader.giphy.*;
import at.mukprojects.imageloader.gif.*;

String apiKey = "dc6zaTOxFJmzC";

GifLoader loader;
GifList list;
GifData data;
PImage[] imgs;
int index;

void setup() {
  size(800, 450);
  frameRate(25);

  loader = new GiphyLoader(this, apiKey);
  list = loader.start("cat funny", false, 60 * 1000);

  index = 0;
}

void draw() {
  if (imgs == null) {
    if (list.size() > 0) {
       data = list.getRandom();
       imgs = data.getGifFrames();
    }
  } else {
    image(imgs[index], 0, 0, width, height);
    delay(data.getDelay());
    index++;
    if (index > imgs.length - 1) {
      index = 0;
    }
  }
}

void mousePressed() {
  imgs = list.getRandom().getGifFrames();
  index = 0;
}