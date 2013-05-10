package fr.inria.rivage.engine.concurrency.resconcurrency;

import fr.inria.rivage.engine.concurrency.exceptions.CCRuntimeException;
import fr.inria.rivage.engine.concurrency.utils.BlockingQueue;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.engine.operations.UnableToApplyException;
import fr.inria.rivage.net.queues.OutputQueue;
import fr.inria.rivage.tools.Graph;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * The main thread for the ResConcurrencyController. This thread does all the
 * work. Taking all incoming operation, applying them to the tree and so on. It
 * also stores the not yet ready operations and resolves conflicts.
 *
 * @author yves
 *
 */
public class ResMainThread extends Thread {
    private static final Logger log = Logger.getLogger(ResMainThread.class.getName());

    
    private boolean suspended;
    private final BlockingQueue<OpWrapper> packetQueue;
    private final FileController fileController;
    private final OutputQueue netOut;
    private History history;
    private boolean halted;
    private LinkedList<OpWrapper> notReadyOps = new LinkedList<OpWrapper>();

    public enum OpRel {

        PROCEDES,
        RIGHTORDER,
        REVERSEORDER,
        DEPENDS,
        DESTROYED,
        MASKED
    };

    public enum OpRelRealConflict {

        REALCONFLICT
    };
    private Graph<OpWrapper, OpRel> graph = new Graph<OpWrapper, OpRel>();
    private Graph<OpWrapper, OpRelRealConflict> realConflictsGraph = new Graph<OpWrapper, OpRelRealConflict>();
    private ConfTest ct = new ConfTest();

    public ResMainThread(BlockingQueue<OpWrapper> packetQueue, FileController fc) {
        super("ResMainThread");
        this.packetQueue = packetQueue;
        this.fileController = fc;
        this.netOut = new OutputQueue("Concurrency" + fc.getId());
        /*try {
            /*Application.getApplication().getNetwork().registerOutputQueue(
                    netOut);*
        } catch (NetworkException e) {
            log.fatal("Could not register input or output queues.", e);
            throw new RuntimeException(e);
        }*/

        suspended = false;
        halted = false;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                OpWrapper op = packetQueue.dequeue();
                /*
                 * switch (oppack.getType()) { case OpObjectGen.OpPack.GLOBAL :
                 * globalhandle((OpObjectGen.GlobalOperation)oppack); break;
                 * case OpObjectGen.OpPack.LOCAL :
                 * localhandle((OpObjectGen.LocalOperation)oppack); break; case
                 * OpObjectGen.OpPack.UNDOGLOBAL :
                 * undoglobalhandle((OpObjectGen.UndoGlobalOperation)oppack);
                 * break; case OpObjectGen.OpPack.UNDOLOCAL :
                 * undolocalhandle((OpObjectGen.UndoLocalOperation)oppack);
                 * break;
                 }
                 */
                analyzePacket(op);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        synchronized (this) {
            suspended = true;
            this.interrupt();
        }
    }

    public void goOn() {
        if (!halted) {
            synchronized (this) {
                suspended = false;
                this.notify();
            }
        } else {
            throw new CCRuntimeException(
                    "The main thread was halted. You can't goOn().");
        }
    }

    private void analyzePacket(OpWrapper op) {
        if (op.isLocal()) {
            globalHandler(op);
            netOut.enqueue((Serializable) op.clone());

        } else {
            if (!history.isCausallyReady(op)) {
                notReadyOps.add(op);
            } else {
                globalHandler(op);
                boolean bb = true;
                while (bb) {
                    bb = false;
                    for (int i = 0; i < notReadyOps.size(); i++) {
                        if (history.isCausallyReady(notReadyOps.get(i))) {
                            globalHandler(notReadyOps.get(i));
                            notReadyOps.remove(i);
                            bb = true;
                            break;
                        }
                    }
                }
            }
        }

    }

    private void globalHandler(OpWrapper op) {
/*
        ArrayList<OpWrapper> ops = history.getOps();
        System.out.println("new op = " + op.getOperation() + "\n");
        graph.addNode(op);
        realConflictsGraph.addNode(op);
        for (int i = 0; i < ops.size(); i++) {
            OpWrapper ow = ops.get(i);
            if (concurrent(ow, op)) {
                if (realConflict(ow, op)) {

                    if (ow.getFromSite() < op.getFromSite()) {
                        realConflictsGraph.addEdge(op, ow, OpRelRealConflict.REALCONFLICT);
                    } else {
                        realConflictsGraph.addEdge(ow, op, OpRelRealConflict.REALCONFLICT);
                    }
                } else if (resolvableRightOrderConflict(ow, op)) {
                    graph.addEdge(ow, op, OpRel.RIGHTORDER);
                } else if (resolvableReverseOrderConflict(ow, op)) {
                    graph.addEdge(op, ow, OpRel.REVERSEORDER);
                }
            } else if (dependsOn(op, ow)) {
                graph.addEdge(ow, op, OpRel.DEPENDS);
            } else if (destroyedBy(ow, op)) {
                graph.addEdge(ow, op, OpRel.DESTROYED);
            } else if (maskedBy(ow, op)) {
                graph.addEdge(ow, op, OpRel.MASKED);
            } else if (procedes(ow, op)) {
                graph.addEdge(ow, op, OpRel.PROCEDES);
            }
        }
        propagateCancelOps(op);

        try {
            ArrayList<OpWrapper> hh = topologicalSort(graph, ops);
            int x;
            for (x = 0; x < ops.size(); x++) {
                if ((ops.get(x) != hh.get(x)) || ops.get(x).getOperation().isDirty()) {
                    break;
                }
            }

            for (int i = ops.size() - 1; i >= x; i--) /*
             * if (!ops.get(i).getOperation().isCanceled() ||
             ops.get(i).getOperation().isDirty());
             */ /*{
                ops.get(i).getOperation().unapply(fileController);
            }
            for (int i = x; i < hh.size(); i++) {
                hh.get(i).getOperation().apply(fileController);
            }

            history.setOps(hh);

//			history.appendToHistory(op);
//			op.apply(fileController);
        } catch (UnableToApplyException e) {
            e.printStackTrace();
//		} catch (CCException e) {
//			e.printStackTrace();
        }*/
    }

    private void localHandler(OpWrapper op) {
        try {
            netOut.enqueue((Serializable) op.clone());
            op.getOperation().apply(fileController);
            op.setApplied(true);
        } catch (UnableToApplyException e) {
            e.printStackTrace();
            // TODO Add log message.
        }
    }

    public void halt() {
        halted = true;
        interrupt();
    }

    /*
     * returns false if the topological sorting is not possible (i.e. the graph
     * contains cycles
     */
    @SuppressWarnings("unused")
    static private ArrayList<OpWrapper> topologicalSort(Graph<OpWrapper, OpRel> graph,
            ArrayList<OpWrapper> currentHistory) {
        ArrayList<OpWrapper> nodes, result;
        nodes = graph.getNodes();
        result = new ArrayList<OpWrapper>();

        HashMap<OpWrapper, Long> noEdges = new HashMap<OpWrapper, Long>();
        for (int i = 0; i < nodes.size(); i++) {
            noEdges.put(nodes.get(i), graph.getInDegree(nodes.get(i)));
        }

        boolean bb = true;
        for (int k = 0; k < nodes.size(); k++) {
            int x = -1;
            if (bb && k < currentHistory.size() && noEdges.get(currentHistory.get(k)) == 0
                    && !currentHistory.get(k).getOperation().isDirty()) {
                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i) == currentHistory.get(k)) {
                        x = i;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < nodes.size(); i++) {
                    if (noEdges.get(nodes.get(i)) == 0) {
                        if (x == -1) {
                            x = i;
                        } else {
                            if (nodes.get(i).getOperation().getId().compareTo(
                                    nodes.get(x).getOperation().getId()) < 0) {
                                x = i;
                            }
                        }
                    }
                }
                if (x == -1) {
                    return null;
                }
            }

            result.add(nodes.get(x));
            noEdges.put(nodes.get(x), new Long(-1));

            for (Graph<OpWrapper, OpRel>.EdgeEntry ee : graph.getOutNeighbors(nodes.get(x))) {
                OpWrapper ow = ee.getNode();
                long w = noEdges.get(ow) - 1;
                noEdges.put(ow, w);
            }
        }

        return result;
    }

    @SuppressWarnings("unused")
    static private boolean procedes(OpWrapper o1, OpWrapper o2) {
        long v1[], v2[];
        v1 = o1.getStateVector();
        v2 = o2.getStateVector();
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] > v2[i]) {
                return false;
            }
        }

        return true;
    }

    static private boolean concurrent(OpWrapper o1, OpWrapper o2) {
        return !(procedes(o1, o2) || procedes(o2, o1));
    }

/*
    private boolean realConflict(OpWrapper o1, OpWrapper o2) {
        return ct.testObjects(o1.getOperation(), o2.getOperation()) == ct.REALCONFLICT
                || ct.testObjects(o2.getOperation(), o1.getOperation()) == ct.REALCONFLICT;
    }


    private boolean resolvableRightOrderConflict(OpWrapper o1, OpWrapper o2) {
        return ct.testObjects(o1.getOperation(), o2.getOperation()) == ct.RIGHTORDER
                || ct.testObjects(o2.getOperation(), o1.getOperation()) == ct.REVERSEORDER;
    }

    
    private boolean resolvableReverseOrderConflict(OpWrapper o1, OpWrapper o2) {
        return ct.testObjects(o1.getOperation(), o2.getOperation()) == ct.REVERSEORDER
                || ct.testObjects(o1.getOperation(), o2.getOperation()) == ct.RIGHTORDER;
    }
*/
    @SuppressWarnings("unused")
    private boolean maskedBy(OpWrapper ow1, OpWrapper ow2) {
        Operation o1 = ow1.getOperation(), o2 = ow2.getOperation();


        return false;
    }

    @SuppressWarnings("unused")
    private boolean destroyedBy(OpWrapper ow1, OpWrapper ow2) {
       /* Operation o1 = ow1.getOperation(), o2 = ow2.getOperation();
        if (o2 instanceof DeleteOperation || o2 instanceof UngroupOperation) {
            return (o2.getId() == o1.getId());
        }
*/
        return false;
    }

    @SuppressWarnings("unused")
    private boolean dependsOn(OpWrapper ow1, OpWrapper ow2) {
        /*Operation o1 = ow1.getOperation(), o2 = ow2.getOperation();
        if (o2 instanceof CreateOperation || o2 instanceof GroupOperation) {
            if (o1 instanceof GroupOperation) {
                GroupOperation g = (GroupOperation) o1;
                return g.isInList(o2.getId());

            } else {
                return o1.getId() == o2.getId();
            }
        }
        if (o2 instanceof UngroupOperation && o1 instanceof GroupOperation) {
            UngroupOperation uo = (UngroupOperation) o2;
            GroupOperation go = (GroupOperation) o1;
            for (ID l : go.getInList()) {
                if (uo.childOfID(l)) {
                    return true;
                }
            }
        }

        if (o2 instanceof NewPageOperation) {
            throw new UnsupportedOperationException("Ooops");
            /*if (o1.getPageId().equals(o2.getId())) {
                return true;
            }*/
       /* }*/
        return false;
    }

    /*
     * private void propagateRealConflict (OpWrapper op, boolean reject) { if
     * (reject) op.getOperation().reject(); else op.getOperation().accept();
     *
     * ArrayList<OpWrapper> realConOps = graph.getOutNeighbors(op,
     * OpRel.REALCONFLICT); if (!reject && op.getOperation().isCanceled()) { for
     * (OpWrapper o : realConOps) { propagateRealConflict (o, true);
     * ArrayList<OpWrapper> depOps = graph.getOutNeighbors(o, OpRel.DEPENDS);
     * for (OpWrapper oo : depOps) { if
     * (graph.getInNeighbors(oo,OpRel.DEPENDS).size() == 0)
     * propagateRealConflict (oo, true); }
     *
     * }
     * }
     * else if (reject && !op.getOperation().isCanceled()) { for (OpWrapper o :
     * realConOps) { propagateRealConflict (o, !reject); graph.removeEdge(op,
     * o); graph.addEdge(o, op, OpRel.REALCONFLICT); ArrayList<OpWrapper> depOps
     * = graph.getOutNeighbors(o, OpRel.DEPENDS); for (OpWrapper oo : depOps)
     * propagateRealConflict (oo, !reject); } } }
     */
    private void propagateCancelOps(OpWrapper newOp) {
        boolean changed;
        LinkedList<OpWrapper> nodes = new LinkedList<OpWrapper>();
        nodes.addFirst(newOp);
        newOp.getOperation().reject();
        while (!nodes.isEmpty()) {
            changed = false;
            OpWrapper o = nodes.removeFirst();
            if (!o.getOperation().isCanceled()) {
                boolean bb = true;
                // is there any canceled operation on which "o" depends?
                ArrayList<OpWrapper> depConOps = graph.getInNeighbors(o, OpRel.DEPENDS);
                for (OpWrapper o2 : depConOps) {
                    if (o2.getOperation().isCanceled()) {
                        o.getOperation().reject();
                        bb = false;
                        break;
                    }
                }

                if (bb) {
                    //has any real conflicting active operation?
                    ArrayList<OpWrapper> relConOps = realConflictsGraph.getInNeighbors(o, OpRelRealConflict.REALCONFLICT);
                    for (OpWrapper o2 : relConOps) {
                        if (!o2.getOperation().isCanceled()) {
                            o.getOperation().reject();
                            bb = false;
                            break;
                        }
                    }
                }
                if (!bb) //operation rejected
                {
                    changed = true;
                }

            } else //o is canceled
            {

                boolean ok = false;
                ArrayList<OpWrapper> depConOps = graph.getInNeighbors(o, OpRel.DEPENDS);
                for (OpWrapper o2 : depConOps) {
                    if (o2.getOperation().isCanceled()) {
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    ArrayList<OpWrapper> relConOps = realConflictsGraph.getInNeighbors(o, OpRelRealConflict.REALCONFLICT);
                    for (OpWrapper o2 : relConOps) {
                        if (!o2.getOperation().isCanceled()) {
                            ok = true;
                            break;
                        }
                    }
                }
                if (!ok) {
                    changed = true;
                    o.getOperation().accept();

                }
            }

            if (changed) {
                for (OpWrapper op : graph.getOutNeighbors(o, OpRel.DEPENDS)) {
                    nodes.addLast(op);
                }
                for (OpWrapper op : realConflictsGraph.getOutNeighbors(o, OpRelRealConflict.REALCONFLICT)) {
                    nodes.addLast(op);
                }
            }

        }
    }

    private void propagateCancelOps() {
        boolean exitLoop = false;
        while (!exitLoop) {
            exitLoop = true;
            ArrayList<OpWrapper> ops = graph.getNodes();
            for (OpWrapper o : ops) {
                if (!o.getOperation().isCanceled()) {
                    boolean bb = true;
                    // is there any canceled operation on which "o" depends?
                    ArrayList<OpWrapper> depConOps = graph.getInNeighbors(o, OpRel.DEPENDS);
                    for (OpWrapper o2 : depConOps) {
                        if (o2.getOperation().isCanceled()) {
                            o.getOperation().reject();
                            exitLoop = false;
                            bb = false;
                            break;
                        }
                    }

                    if (bb) {
                        //has any real conflicting active operation?
                        ArrayList<OpWrapper> relConOps = realConflictsGraph.getInNeighbors(o, OpRelRealConflict.REALCONFLICT);
                        for (OpWrapper o2 : relConOps) {
                            if (!o2.getOperation().isCanceled()) {
                                o.getOperation().reject();
                                exitLoop = false;
                                break;
                            }
                        }
                    }

                } else //o is canceled
                {

                    boolean ok = false;
                    ArrayList<OpWrapper> depConOps = graph.getInNeighbors(o, OpRel.DEPENDS);
                    for (OpWrapper o2 : depConOps) {
                        if (o2.getOperation().isCanceled()) {
                            ok = true;
                            break;
                        }
                    }
                    if (!ok) {
                        ArrayList<OpWrapper> relConOps = realConflictsGraph.getInNeighbors(o, OpRelRealConflict.REALCONFLICT);
                        for (OpWrapper o2 : relConOps) {
                            if (!o2.getOperation().isCanceled()) {
                                ok = true;
                                break;
                            }
                        }
                    }
                    if (!ok) {
                        o.getOperation().accept();
                        exitLoop = false;
                        break;
                    }
                }
            }
        }
    }
}
