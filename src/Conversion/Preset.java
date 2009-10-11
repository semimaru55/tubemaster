/*
 * TubeMaster++ - An Internet Multimedia Capture Tool.
 * Copyright (C) 2009 GgSofts
 * Contact: admin@tubemaster.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package Conversion;

public class Preset
{
	private String label = "";
	private String params = "";
	private String extension = "";
	private String category = "";
	
	public Preset(String label, String params, String extension, String category)
	{
		this.label = label;
		this.params = params;
		this.extension = extension;
		this.category = category;
	}
	
	public String getLabel() {return this.label;}
	public String getParams() {return this.params;}
	public String getExtension() {return this.extension;}
	public String getCategory() {return this.category;}

}
