package org.joshy.gfx.perftests;

import org.joshy.gfx.Core;
import org.joshy.gfx.event.Callback;
import org.joshy.gfx.node.control.Button;
import org.joshy.gfx.node.layout.FlexBox;
import org.joshy.gfx.node.layout.VFlexBox;
import org.joshy.gfx.stage.Stage;
import org.joshy.gfx.util.u;

/**
 * Performance test 1. this test measures how long it takes
 * to lay out a complex UI.
 */
public class Test1 {
    private static boolean DEBUG = false;

    public static void main(String ... args) throws Exception {
        Core.setTesting(true);
        Core.init();

        final FlexBox box = new VFlexBox().setBoxAlign(VFlexBox.Align.Stretch);
        for(int i=0; i<100; i++) {
            box.add(new Button("button " + i));
        }
        if(DEBUG) {
            Stage stage = Stage.createStage();
            stage.setContent(box);
            return;
        }

        box.doSkins();
        long total = 0;
        int count = 0;
        for(int i=0; i<50; i++) {
            long t = time(new Callback() {
                public void call(Object o) throws Exception {
                    box.doPrefLayout();
                    box.setWidth(500);
                    box.setHeight(500);
                    box.doLayout();
                }
            });
            //u.p("time = " + t);
            if(i < 2) continue;
            count++;
            total += t;
        }

        long avg = total / count;
        u.p("average layout time: " + avg);

        //create gui
        //invoke top level layout manually
        //measure before and after
    }

    private static long time(Callback callback) {
        long start = System.currentTimeMillis();
        try {
            callback.call(null);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        long end = System.currentTimeMillis();
        return end-start;
    }
}
