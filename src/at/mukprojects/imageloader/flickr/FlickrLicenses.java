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

package at.mukprojects.imageloader.flickr;

/**
 * Enumeration of all Flickr licenses.
 * 
 * Flickr API
 * https://www.flickr.com/services/api/flickr.photos.licenses.getInfo.html
 * 
 * @author Mathias Markl
 */
public enum FlickrLicenses {
    ARR(0, "All Rights Reserved", ""),
    ANSL(1, "Attribution-NonCommercial-ShareAlike License", "http://creativecommons.org/licenses/by-nc-sa/2.0/"),
    ANCL(2, "Attribution-NonCommercial License", "http://creativecommons.org/licenses/by-nc/2.0/"),
    ANNL(3, "Attribution-NonCommercial-NoDerivs License", "http://creativecommons.org/licenses/by-nc-nd/2.0/"),
    AL(4, "Attribution License", "http://creativecommons.org/licenses/by/2.0/"),
    ASL(5, "Attribution-ShareAlike License", "http://creativecommons.org/licenses/by-sa/2.0/"),
    ANDL(6, "Attribution-NoDerivs License", "http://creativecommons.org/licenses/by-nd/2.0/"),
    NKCR(7, "No known copyright restrictions", "http://flickr.com/commons/usage/"),
    USGW(8, "United States Government Work", "http://www.usa.gov/copyright.shtml");
    
    private int id; 
    private String text;
    private String url;
    
    /**
     * Constructs a new Licenses object.
     *  
     * @param id The license id.
     * @param text The license text.
     * @param url The license url.
     */
    private FlickrLicenses(int id, String text, String url) {
	this.id = id;
	this.text = text;
	this.url = url;
    }
    
    /**
     * Gets the license id.
     * 
     * @return The license id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets the license text.
     * 
     * @return The license text.
     */
    public String getText() {
        return text;
    }
    
    /**
     * Gets the license url.
     * 
     * @return The license url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the Licenses enumeration object.
     * 
     * @param id
     *            The license id.
     * @return The Licenses enumeration object.
     */
    public static FlickrLicenses fromId(int id) {
	for (FlickrLicenses license : FlickrLicenses.values()) {
	    if (id == license.id) {
		return license;
	    }
	}
	return NKCR;
    }
    
    /**
     * Returns the Licenses enumeration object.
     * 
     * @param id
     *            The license id.
     * @return The Licenses enumeration object.
     */
    public static FlickrLicenses fromId(String id) {
	try {
	    return fromId(Integer.parseInt(id));
	} catch (NumberFormatException e) {
	    return NKCR;
	}
    }
}