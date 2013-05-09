/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2013 LORIA / Inria / SCORE Team
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
package fr.inria.rivage.engine.concurrency.tools;

import fr.inria.rivage.elements.PointDouble;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
final public class AffineTransformeParameter implements Serializable {

    Parameters param;
    AffineTransform af;

    public AffineTransformeParameter(Parameters param) {
        this.param = param;
        /*if(!isReady()){
         init();
         }
         getAf();*/
    }

    public void init() {
        af = new AffineTransform();
        saveAf(af);
        param.acceptMod();
    }

    private void checkAf() {
        if (af == null) {
            if (isReady()) {
                loadAf();
            } else {
                af = new AffineTransform();
            }
        }
    }

    public boolean isReady() {
        return (param.isParameter(Parameters.ParameterType.Scale)
                && param.isParameter(Parameters.ParameterType.Translate)
                && param.isParameter(Parameters.ParameterType.Shear));
    }

    public AffineTransform loadAf() {
        PointDouble shear = param.getPoint(Parameters.ParameterType.Shear);
        PointDouble scale = param.getPoint(Parameters.ParameterType.Scale);
        PointDouble translate = param.getPoint(Parameters.ParameterType.Translate);
        af = new AffineTransform(scale.x, shear.y, shear.x, scale.y, translate.x, translate.y);
        return af;
    }

    public AffineTransform getAf() {
        checkAf();
        return af;
    }

    private void saveAf(AffineTransform af2) {
        param.setPoint(Parameters.ParameterType.Shear, af2.getShearX(), af2.getShearY());
        param.setPoint(Parameters.ParameterType.Scale, af2.getScaleX(), af2.getScaleY());
        param.setPoint(Parameters.ParameterType.Translate, af2.getTranslateX(), af2.getTranslateY());
    }

    public void setAf(AffineTransform af) {
        this.af = af;
        saveAf(af);
    }

    public void concat(AffineTransform af2) {
        checkAf();
        AffineTransform aftmp=new AffineTransform(af2);
        aftmp.concatenate(af);
        saveAf(aftmp);
    }

    public void rot(double theta, PointDouble center) {
        //checkAf();
        AffineTransform af2=new AffineTransform();
        af2.rotate(theta, center.x, center.y);
        af2.concatenate(af);
        saveAf(af2);
    }

    public void translate(PointDouble p) {
        checkAf();
        AffineTransform af2 = new AffineTransform();
        af2.translate(p.x, p.y);
        concat(af2);
        /*af2.concatenate(af);
        //af=af2;
        saveAf(af2);*/
    }

    public void shear(PointDouble shear, PointDouble center) {
        AffineTransform shearAf = new AffineTransform();
        shearAf.translate(center.x, center.y);
        shearAf.shear(shear.x, shear.y);
        shearAf.translate(-center.x, -center.y);
        concat(shearAf);
    }

    public void scale(PointDouble scale, PointDouble center) {
        AffineTransform shearAf = new AffineTransform();
        shearAf.translate(center.x, center.y);
        shearAf.scale(scale.x, scale.y);
        shearAf.translate(-center.x, -center.y);
        concat(shearAf);
    }

    public void sendMod() {
        param.sendMod();
        loadAf();
    }
    public void reset(){
        af=null;
    }
}
