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
package fr.inria.rivage.engine.concurrency.tools;

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.PointDouble;
import static fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType.*;
import fr.inria.rivage.tools.ObservableSerializable;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GroupParameters extends Parameters implements Observer {

    //Parameter[] params;
    List<Parameters> parames;
    GObjectContainer<GObject> group;


    /* public GroupParameters(GDocument doc,GObjectContainer<GObject> group, ID target, ParameterType... types) {
     super(doc, target, types);
     this.group=group;
     }

     public GroupParameters(GDocument doc,GObjectContainer<GObject> group, Parameters... parameters) {
     super(doc);
     parames = Arrays.asList(parameters);
     this.group=group;
     scanParameter();
     }*/
    public GroupParameters(GObjectContainer<GObject> group) {
        this.group = group;
        
        parames = new LinkedList();
        for (GObject go : group) {
            parames.add(go.getParameters());
        }
        scanParameter();
    }

    @Override
    Parameter newParameter(ParameterType type) {
        return new GroupParameter(type,fCId);
    }
    //private static final int MIN = -1;
    //private static final int MAX = 1;

    /*private void setMinMaxint(int minmax,ParameterType type,PointDouble point){
     for (Parameters pa : parames) {
     Point2D o = (Point2D) pa.getObject(type);
     Double ang=pa.getDouble(Angular);    
     if (ang!=null){
     AffineTransform af=new AffineTransform();
     PointDouble center=pa.getPoint(P1coord).center(pa.getPoint(P2coord));
     af.rotate(ang, center.getX(), center.getY());
     o=af.transform(o, new PointDouble());
     }
     if (o != null) {
     if (minmax * (o.getX() - point.getX()) > 0) {
     point.setX(o.getX());
     }
     if (minmax * (o.getY() - point.getY()) > 0) {
     point.setY(o.getY());
     }
     }
     }
     }
     private void setMinMaxPoint( int minmax,ParameterType type) {
     PointDouble point = new PointDouble((Double.NEGATIVE_INFINITY) * minmax, (Double.NEGATIVE_INFINITY) * minmax);
     setMinMaxint(minmax,P1coord,point);
     setMinMaxint(minmax,P2coord,point);
     this.setInitialValue(type, point);
     }*/
    private void setAvg(ParameterType type) {
        double angular = 0;
        double nb = 0;
        for (Parameters pa : parames) {
            Double o = (Double) pa.getObject(type);
            if (o != null) {
                nb++;
                angular += o.doubleValue();
            }
        }
        this.setInitialValue(type, new Double(angular / nb));
    }

    private void setCopy(ParameterType type, Object unconsistant) {
        Object obj = null;
        for (Parameters pa : parames) {
            Object o = pa.getObject(type);
            if (o != null) {
                if (obj == null) {
                    obj = o;
                } else if (unconsistant != null && !obj.equals(o)) {
                    obj = unconsistant;
                    break;
                }
            }
        }
        if (obj != null) {
            this.setInitialValue(type, obj);
        }
    }

    private void setInitialValue(ParameterType type, Object obj) {
        Parameter par = new GroupParameter(type,fCId, obj);
        this.parametersMap.put(type, par);
    }
    /*void addParamter(Parameter p, boolean newAccepted) {
     GroupParameter param = (GroupParameter) this.parametersMap.get(p.getType());
     if (param == null) {
     if (newAccepted) {
     param = (GroupParameter) newParameter(p.type, doc, null);
     parametersMap.put(p.getType(), param);
     }/*else{
     return;
     }*
     } else {
     }

     }*/

    @Override
    public void setTarget(ID target) {
        super.setTarget(target);
        setInitialValue(Zpos, group.getMax().genNext(group.getId()));
    }
    

    @Override
    public Parameter getParameter(ParameterType type) {
        return super.getParameter(type);
    }

    @Override
    public Object getObject(ParameterType type) {
        return super.getObject(type);
    }

    
    private void scanParameter() {
        if (group.size() < 1) {
            return;
        }
        GBounds2D rec = group.getEuclidBounds();
        
        setInitialValue(Dimension, rec.getDimension());
        setInitialValue(TopLeft, rec.getTopLeftPoint());
        
        for (ParameterType type : ParameterType.values()) {
            switch (type) {
                case Angular:
                    // setAvg(type);
                    this.setInitialValue(Angular, new Double(0));
                    break;
                case TopLeft:
                case Dimension:
                    break;
                case Zpos:
                   
                    // setMinMaxPoint( MAX,type);
                    break;
                default:
                    setCopy(type, null);
            }
        }
    }

    public void update(Observable o, Object arg) {
        scanParameter();
    }

    public class GroupParameter<T> extends Parameter<T> {

        T unConsistantObject;

        private GroupParameter(ParameterType type,ID idFC) {
            super(type, idFC);
        }

        private GroupParameter(ParameterType type,ID idFC, T initialValue) {
           super(type,idFC);
            this.element = initialValue;
        }

        @Override
        public void remoteUpdate(Parameter up) {
            throw new UnsupportedOperationException("The model does not allow this action");
        }

        /*  @Override
         public T getElement() {
         switch (type) {
         case 
         return super.getElement();
         }
         }*/
        /**
         *
         * @param t the value of t
         * @param nice the value of nice
         */
        @Override
        public void localUpdate(T t, int nice) {
            if (t == null) {
                return;
            }
            PointDouble topLeftVirt = GroupParameters.this.getPoint(TopLeft);
            switch (type) {
                case BgColor:
                case FgColor:
                case Stroke:

                    // all same value
                    for (Parameters p : parames) {
                        Parameter par = p.getParameter(type);
                        if (par != null) {
                            par.localUpdate(t, nice + 1);
                        }
                    }
                    break;
                case Angular: {
                    double delta = ((Double) t).doubleValue()
                            - ((Double) this.element).doubleValue();
                    //increment value by delta

                    Parameters.ParameterBounds bound = GroupParameters.this.getBounds();
                    Point2D middle = bound.getCenter();

                    //increment value by delta
                    for (Parameters p : parames) {
                        Parameters.ParameterBounds boundMod = p.getBounds();
                        boundMod.setNice(nice + 1);
                        boundMod.rotate(delta, middle);
                    }
                }
                break;

                case Dimension: {

                    PointDouble fact = ((PointDouble) t).div((PointDouble) this.element);

                    for (Parameters p : parames) {
                        try {
                            PointDouble topLeft = p.getPoint(TopLeft);
                            p.setObject(TopLeft, topLeft.minus(topLeftVirt).mult(fact).plus(topLeftVirt), nice + 1);

                            PointDouble dim2 = p.getPoint(Dimension);

                            AffineTransform af = new AffineTransform();
                            PointDouble centerd = dim2.center(0, 0);
                            af.rotate(p.getDouble(Angular), centerd.x, centerd.y);
                            af.scale(fact.getX(), fact.getY());
                            af.inverseTransform(centerd, centerd);
                            af.rotate(-p.getDouble(Angular), centerd.x, centerd.y);
                            PointDouble dim = (PointDouble) af.transform(dim2, new PointDouble());
                            //PointDouble dim = dim2.mult(fact.rotate(new PointDouble(), p.getDouble(Angular)));
                            p.setObject(Dimension, dim.abs(), nice + 1);
                        } catch (NoninvertibleTransformException ex) {
                            Logger.getLogger(GroupParameters.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                break;
                case TopLeft:
                    PointDouble diff = ((PointDouble) t).minus((PointDouble) this.element);
                    for (Parameters p : parames) {
                        if (p != null) {
                            p.addPoint(TopLeft, diff);
                        }
                    }
                    break;
                    
                case Zpos:
                    throw new UnsupportedOperationException("not yet");
                /*case Ce:
                 etire((Point2D) t, ParameterType.P1coord, ParameterType.P2coord, nice + 1);
                 break;
                 case P2coord:
                 etire((Point2D) t, ParameterType.P2coord, ParameterType.P1coord, nice + 1);
                 break;*/
            }
            element = t;
        }

        /*private void etire(Point2D p, ParameterType that, ParameterType other, int nice) {
         PointDouble p1 = getPoint(other);
         PointDouble p2 = getPoint(that);
         PointDouble pRef = getPoint(other);
         PointDouble fact = new PointDouble((pRef.getX() - p.getX()) / (p1.getX() - p2.getX()),
         (pRef.getY() - p.getY()) / (p1.getY() - p2.getY()));
         for (Parameters pa : parames) {
         pa.setObject(P1coord, pa.getPoint(P1coord).minus(pRef).mult(fact).add(pRef), nice);
         pa.setObject(P2coord, pa.getPoint(P2coord).minus(pRef).mult(fact).add(pRef), nice);
         }
         }*/
        @Override
        public void sendMod(ID id) {
            for (Parameters p : parames) {
                Parameter par = p.getParameter(type);
                if (par != null) {
                    par.sendMod(id);
                }
            }
        }

        @Override
        public void acceptMod() {
            for (Parameters p : parames) {
                Parameter par = p.getParameter(type);
                if (par != null) {
                    par.acceptMod();
                }
            }
        }

        @Override
        public Parameter fork() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
