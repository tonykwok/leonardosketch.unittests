package org.joshy.gfx.perftests;

import org.joshy.gfx.Core;
import org.joshy.gfx.draw.GFX;
import org.joshy.gfx.event.Callback;
import org.joshy.gfx.node.control.Button;
import org.joshy.gfx.node.control.Control;
import org.joshy.gfx.node.Node;
import org.joshy.gfx.node.layout.FlexBox;
import org.joshy.gfx.node.layout.VFlexBox;
import org.joshy.gfx.stage.swing.SwingGFX;
import org.joshy.gfx.util.u;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Test2 {
    public static void main(String ... args) throws Exception {
        PrintWriter out = new PrintWriter(new FileOutputStream(new File(args[0]),true));
        Core.setTesting(true);
        Core.init();

        final FlexBox box = new VFlexBox().setBoxAlign(VFlexBox.Align.Stretch);
        for(int i=0; i<100; i++) {
            box.add(new Button("button " + i));
        }
//        final SwingStage stage = (SwingStage) Stage.createStage();
//        stage.setContent(box);
        //return;


        box.doSkins();
        for(Node n : box.children()) {
            if(n instanceof Control) {
                ((Control)n).doSkins();
            }
        }
        box.doPrefLayout();
        box.setWidth(500);
        box.setHeight(500);
        box.doLayout();

        long total = 0;
        int count = 0;
        final BufferedImage img = new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB);
        for(int i=0; i<50; i++) {
            long t = time(new Callback() {
                public void call(Object o) {
                    Graphics2D g2 = img.createGraphics();
                    GFX gfx = new SwingGFX(g2);
                    box.draw(gfx);
                    gfx.dispose();
                }
            });
            //u.p("time = " + t);
            if(i < 2) continue;
            count++;
            total += t;
        }

        long avg = total / count;
        u.p("average drawing time: " + avg);

        out.println(Test2.class.getCanonicalName()
                +", "+avg
                +", draw 100 buttons to a buffer");
        out.close();
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
