/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2012 LORIA / Inria / SCORE Team
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
package fr.inria.rivage.tools;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class FilesTools {

    public static String getExtention(File f) {

        String[] ff = f.getName().split("\\.");
        return ff.length==0?null:ff[ff.length - 1].toLowerCase();
    }

    public static class ImageFileFilter extends FileFilter {

        public ImageFileFilter() {
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtention(f);
            return (extension != null && 
                    (extension.equals("tiff")
                        || extension.equals("tif")
                        || extension.equals("gif")
                        || extension.equals("jpeg")
                        || extension.equals("jpg")
                        || extension.equals("png"))) 
                    ;
        }

        @Override
        public String getDescription() {
            return "Image files (*.tiff,*.jpg,*.png)";
        }
    }
}
