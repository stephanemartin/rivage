package fr.inria.rivage.engine.concurrency.resconcurrency;

final public class ConfTest {
/*
    private HashMap<Class, Integer> objHash;
    private HashMap<Class, Integer> operationHash;
    public int op1ID;
    public int op2ID;
    public Operation op1;
    public Operation op2;
    public int o1ID;
    public int o2ID;
    final public int RIGHTORDER = 3;
    final public int REVERSEORDER = 2;
    final public int REALCONFLICT = 1;
    final public int NOCONFLICT = 0;

    public ConfTest() {
        objHash = new HashMap<Class, Integer>();
        operationHash = new HashMap<Class, Integer>();
        objHash.put(geditor.elements.GLine.class, 0);
        objHash.put(geditor.elements.GGroup.class, 1);
        objHash.put(geditor.elements.GPolyLine.class, 2);
        objHash.put(geditor.elements.GEllipse.class, 3);
        objHash.put(geditor.elements.GRectangle.class, 4);
        operationHash.put(geditor.engine.operations.DeleteOperation.class, 0);
        operationHash.put(geditor.engine.operations.GroupOperation.class, 1);
        operationHash.put(geditor.engine.operations.PointMoveOperation.class, 2);
        operationHash.put(geditor.engine.operations.ChangeColorOperation.class, 3);
        operationHash.put(geditor.engine.operations.CreateOperation.class, 4);
        operationHash.put(geditor.engine.operations.RotateOperation.class, 5);
        operationHash.put(geditor.engine.operations.UngroupOperation.class, 6);
        operationHash.put(geditor.engine.operations.TranslateOperation.class, 7);
        operationHash.put(geditor.engine.operations.ScaleOperation.class, 8);
        operationHash.put(geditor.engine.operations.ChangeTextOperation.class, 9);
        operationHash.put(geditor.engine.operations.SetZPosOperation.class, 10);
        operationHash.put(geditor.engine.operations.MoveToLayerOperation.class, 11);
        operationHash.put(geditor.engine.operations.RemoveLayerOperation.class, 12);
        operationHash.put(geditor.engine.operations.NewPageOperation.class, 13);
        operationHash.put(geditor.engine.operations.RemovePageOperation.class, 14);
        operationHash.put(geditor.engine.operations.NewLayerOperation.class, 15);
        operationHash.put(geditor.engine.operations.SnapOperation.class, 16);
        operationHash.put(geditor.engine.operations.ChangeStrokeOperation.class, 17);
        operationHash.put(geditor.engine.operations.ChangeTextSizeOperation.class, 18);
    }

    private void convertOps() {
        op1ID = operationHash.get(op1.getClass());
        op2ID = operationHash.get(op2.getClass());
    }

    private int testdeleteungroup0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testgroupgroup0() {
        if (setNotEqual(inter(getOperationChildrenList(op1), getOperationChildrenList(op2)), null)) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangecolordelete0() {
        if (((op1.getId().equals(op2.getId()))
                || (childof(op1.getId(), op2.getId())))  {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangecolorchangecolor0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangestrokedelete0() {
        if (((op1.getId()) == (op2.getId())) || (childof(op1.getId(), op2.getId()))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangestrokechangestroke0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testtranslatedelete0() {
        if (((op1.getId()) == (op2.getId())) || (childof(op1.getId(), op2.getId()))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testtranslatetranslate0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testscaledelete0() {
        if (((op1.getId()) == (op2.getId())) || (childof(op1.getId(), op2.getId()))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testscalescale0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangetextdelete0() {
        if (((op1.getId()) == (op2.getId())) || (childof(op1.getId(), op2.getId()))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangetextchangetext0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangetextsizedelete0() {
        if (((op1.getId()) == (op2.getId())) || (childof(op1.getId(), op2.getId()))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangetextsizechangetextsize0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testsetzposdelete0() {
        if (((op1.getId()) == (op2.getId())) || (childof(op1.getId(), op2.getId()))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testsetzpossetzpos0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testdeletegroup0() {
        if (idInSet(op1.getId(), getOperationInList(op2))) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testdeletedelete0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testdeletedelete1() {
        if ((childof(op2.getId(), op1.getId())) && ((op1.getId()) != (op2.getId()))) {
            return REVERSEORDER;
        }
        return testdeletedelete0();
    }

    private int testungroupungroup0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testungroupchangecolor0() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testungroupchangestroke0() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testungrouptranslate0() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testungroupscale0() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testungroupsetzpos0() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangecolorchangecolor1() {
        if (((op1.getId()) != (op2.getId())) && (childof(op1.getId(), op2.getId()))) {
            return REVERSEORDER;
        }
        return testchangecolorchangecolor0();
    }

    private int testchangestrokechangestroke1() {
        if (((op1.getId()) != (op2.getId())) && (childof(op1.getId(), op2.getId()))) {
            return REVERSEORDER;
        }
        return testchangestrokechangestroke0();
    }

    private int testscaletranslate0() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testtranslatescale0() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testscalescale1() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        }
        return testscalescale0();
    }

    private int testrotaterotate0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testrotatedelete0() {
        if (((op1.getId()) == (op2.getId())) || (childof(op1.getId(), op2.getId()))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testrotatetranslate0() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testrotatescale0() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testrotaterotate1() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        }
        return testrotaterotate0();
    }

    private int testscalerotate0() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testrotatescale1() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        }
        return testrotatescale0();
    }

    private int testtranslaterotate0() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testrotatetranslate1() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        }
        return testrotatetranslate0();
    }

    private int testtranslatescale1() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        }
        return testtranslatescale0();
    }

    private int testpointmovepointmove0() {
        if (((op1.getId()) == (op2.getId())) && ((((PointMoveOperation) op1).getPointID()) == (((PointMoveOperation) op2).getPointID()))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testscalepointmove0() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testrotatepointmove0() {
        if (((op1.getId()) != (op2.getId())) && (childof(op2.getId(), op1.getId()))) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testscalepointmove1() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        }
        return testscalepointmove0();
    }

    private int testrotatepointmove1() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        }
        return testrotatepointmove0();
    }

    private int testpointmovedelete0() {
        if (((op1.getId()) == (op2.getId())) || (childof(op1.getId(), op2.getId()))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testungroupgroup0() {
        if (idInSet(op1.getId(), getOperationInList(op2))) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testmovetolayermovetolayer0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testdeletemovetolayer0() {
        if ((op1.getId()) == (op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testnewlayernewlayer0() {
        if ((op1.getId()) < (op2.getId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testungroupmovetolayer0() {
        if ((op1.getId()) == (op2.getId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testgroupmovetolayer0() {
        if (idInSet(op2.getId(), getOperationInList(op1))) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testtranslateremovelayer0() {
        if (childof(op1.getId(), op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testscaleremovelayer0() {
        if (childof(op1.getId(), op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testrotateremovelayer0() {
        if (childof(op1.getId(), op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangecolorremovelayer0() {
        if (childof(op1.getId(), op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangestrokeremovelayer0() {
        if (childof(op1.getId(), op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testgroupremovelayer0() {
        if (childof(op1.getId(), op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testungroupremovelayer0() {
        if (childof(op1.getId(), op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangetextremovelayer0() {
        if (childof(op1.getId(), op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testchangetextsizeremovelayer0() {
        if (childof(op1.getId(), op2.getId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagenewpage0() {
        if ((op1.getId()) == (((NextPageIdOperation) op2).getNextPageId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testnewpagenewpage0() {
        if (((op1.getId()) < (op2.getId())) && ((((NextPageIdOperation) op1).getNextPageId()) == (((NextPageIdOperation) op2).getNextPageId()))) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepageremovepage0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagedelete0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REVERSEORDER;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagegroup0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagepointmove0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagechangecolor0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagechangestroke0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagecreate0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagerotate0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepageungroup0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagetranslate0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagescale0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagechangetext0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagechangetextsize0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagesetzpos0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepagemovetolayer0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testremovepageremovelayer0() {
        if ((op1.getPageId()) == (op2.getPageId())) {
            return REALCONFLICT;
        } else {
            return NOCONFLICT;
        }
    }

    private int testcreatecreate0() {
        if (op1.getId().compareTo(op2.getId())<0) {
            return REVERSEORDER;
        }
            return NOCONFLICT;
        
    }

    private int testgroupgroup1() {
        if (op1.getId().compareTo(op2.getId()) < 0) {
            return REVERSEORDER;
        }
        return testgroupgroup0();
    }

    private int testcreategroup0() {
        if (op1.getId().compareTo(op2.getId()) < 0) {
            return REVERSEORDER;
        }
        return NOCONFLICT;
    }

    public int testObjects(Operation op1, Operation op2) {
        this.op1 = op1;
        this.op2 = op2;
        convertOps();
        switch (op1ID) {
            case 0:
                switch (op2ID) {
                    case 6:
                        return testdeleteungroup0();
                    case 1:
                        return testdeletegroup0();
                    case 0:
                        return testdeletedelete1();
                    case 11:
                        return testdeletemovetolayer0();
                }
                return NOCONFLICT;
            case 1:
                switch (op2ID) {
                    case 1:
                        return testgroupgroup1();
                    case 11:
                        return testgroupmovetolayer0();
                    case 12:
                        return testgroupremovelayer0();
                }
                return NOCONFLICT;
            case 3:
                switch (op2ID) {
                    case 0:
                        return testchangecolordelete0();
                    case 3:
                        return testchangecolorchangecolor1();
                    case 12:
                        return testchangecolorremovelayer0();
                }
                return NOCONFLICT;
            case 17:
                switch (op2ID) {
                    case 0:
                        return testchangestrokedelete0();
                    case 17:
                        return testchangestrokechangestroke1();
                    case 12:
                        return testchangestrokeremovelayer0();
                }
                return NOCONFLICT;
            case 7:
                switch (op2ID) {
                    case 0:
                        return testtranslatedelete0();
                    case 7:
                        return testtranslatetranslate0();
                    case 8:
                        return testtranslatescale1();
                    case 5:
                        return testtranslaterotate0();
                    case 12:
                        return testtranslateremovelayer0();
                }
                return NOCONFLICT;
            case 8:
                switch (op2ID) {
                    case 0:
                        return testscaledelete0();
                    case 8:
                        return testscalescale1();
                    case 7:
                        return testscaletranslate0();
                    case 5:
                        return testscalerotate0();
                    case 2:
                        return testscalepointmove1();
                    case 12:
                        return testscaleremovelayer0();
                }
                return NOCONFLICT;
            case 9:
                switch (op2ID) {
                    case 0:
                        return testchangetextdelete0();
                    case 9:
                        return testchangetextchangetext0();
                    case 12:
                        return testchangetextremovelayer0();
                }
                return NOCONFLICT;
            case 18:
                switch (op2ID) {
                    case 0:
                        return testchangetextsizedelete0();
                    case 18:
                        return testchangetextsizechangetextsize0();
                    case 12:
                        return testchangetextsizeremovelayer0();
                }
                return NOCONFLICT;
            case 10:
                switch (op2ID) {
                    case 0:
                        return testsetzposdelete0();
                    case 10:
                        return testsetzpossetzpos0();
                }
                return NOCONFLICT;
            case 6:
                switch (op2ID) {
                    case 6:
                        return testungroupungroup0();
                    case 3:
                        return testungroupchangecolor0();
                    case 17:
                        return testungroupchangestroke0();
                    case 7:
                        return testungrouptranslate0();
                    case 8:
                        return testungroupscale0();
                    case 10:
                        return testungroupsetzpos0();
                    case 1:
                        return testungroupgroup0();
                    case 11:
                        return testungroupmovetolayer0();
                    case 12:
                        return testungroupremovelayer0();
                }
                return NOCONFLICT;
            case 5:
                switch (op2ID) {
                    case 5:
                        return testrotaterotate1();
                    case 0:
                        return testrotatedelete0();
                    case 7:
                        return testrotatetranslate1();
                    case 8:
                        return testrotatescale1();
                    case 2:
                        return testrotatepointmove1();
                    case 12:
                        return testrotateremovelayer0();
                }
                return NOCONFLICT;
            case 2:
                switch (op2ID) {
                    case 2:
                        return testpointmovepointmove0();
                    case 0:
                        return testpointmovedelete0();
                }
                return NOCONFLICT;
            case 11:
                switch (op2ID) {
                    case 11:
                        return testmovetolayermovetolayer0();
                }
                return NOCONFLICT;
            case 15:
                switch (op2ID) {
                    case 15:
                        return testnewlayernewlayer0();
                }
                return NOCONFLICT;
            case 14:
                switch (op2ID) {
                    case 13:
                        return testremovepagenewpage0();
                    case 14:
                        return testremovepageremovepage0();
                    case 0:
                        return testremovepagedelete0();
                    case 1:
                        return testremovepagegroup0();
                    case 2:
                        return testremovepagepointmove0();
                    case 3:
                        return testremovepagechangecolor0();
                    case 17:
                        return testremovepagechangestroke0();
                    case 4:
                        return testremovepagecreate0();
                    case 5:
                        return testremovepagerotate0();
                    case 6:
                        return testremovepageungroup0();
                    case 7:
                        return testremovepagetranslate0();
                    case 8:
                        return testremovepagescale0();
                    case 9:
                        return testremovepagechangetext0();
                    case 18:
                        return testremovepagechangetextsize0();
                    case 10:
                        return testremovepagesetzpos0();
                    case 11:
                        return testremovepagemovetolayer0();
                    case 12:
                        return testremovepageremovelayer0();
                }
                return NOCONFLICT;
            case 13:
                switch (op2ID) {
                    case 13:
                        return testnewpagenewpage0();
                }
                return NOCONFLICT;
            case 4:
                switch (op2ID) {
                    case 4:
                        return testcreatecreate0();
                    case 1:
                        return testcreategroup0();
                }
                return NOCONFLICT;
        }
        return NOCONFLICT;
    }

    private boolean setEqual(HashSet<ID> set1, HashSet<ID> set2) {
        if (set1 == null) {
            set1 = new HashSet<ID>();
        }
        if (set2 == null) {
            set2 = new HashSet<ID>();
        }
        return set1.equals(set2);
    }

    private boolean setNotEqual(HashSet<ID> set1, HashSet<ID> set2) {
        return !setEqual(set1, set2);
    }

    private HashSet<ID> inter(HashSet<ID> set1, HashSet<ID> set2) {
        HashSet<ID> ret = new HashSet<ID>();
        ret.addAll(set1);
        ret.retainAll(set2);
        return ret;
    }

    private HashSet<ID> getOperationInList(Operation op12) {
        if (!(op12 instanceof InListOperation)) {
            throw new RuntimeException("The operation passed doesn't support the inList operation.");
        }
        HashSet<ID> s = new HashSet<ID>();
        s.addAll(((InListOperation) op12).getInList());
        return s;
    }

    private HashSet<ID> getOperationChildrenList(Operation op12) {
        if (!(op12 instanceof ChildrenListOperation)) {
            throw new RuntimeException("The operation passed doesn't support the childrenlist operation.");
        }
        HashSet<ID> s = new HashSet<ID>();
        s.addAll(((ChildrenListOperation) op12).getChildrenList());
        return s;
    }

    private boolean idInSet(ID id, HashSet<ID> operationInList) {
        return operationInList.contains(id);
    }

    private boolean childof(ID id1, ID id2) {
        if (id2.equals(op1.getId())) {
            return op1.childOfID(id1);
        } else if (id2.equals(op2.getId())) {
            return op2.childOfID(id1);
        }
        return false;
    }*/
}
