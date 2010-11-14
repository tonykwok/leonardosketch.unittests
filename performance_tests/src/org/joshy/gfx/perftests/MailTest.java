package org.joshy.gfx.perftests;

import org.joshy.gfx.Core;
import org.joshy.gfx.draw.FlatColor;
import org.joshy.gfx.event.Callback;
import org.joshy.gfx.node.Node;
import org.joshy.gfx.node.Parent;
import org.joshy.gfx.node.control.*;
import org.joshy.gfx.node.layout.*;
import org.joshy.gfx.stage.Stage;
import org.joshy.gfx.util.ArrayListModel;
import org.joshy.gfx.util.u;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: Nov 13, 2010
 * Time: 7:26:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class MailTest {
    private static boolean DEBUG = false;
    
    public static void main(String ... args) throws Exception {
        PrintWriter out = new PrintWriter(new FileOutputStream(new File(args[0]),true));
        Core.setTesting(true);
        Core.init();
        final FlexBox box = buildGUI();
        if(DEBUG) {
            Stage stage = Stage.createStage();
            stage.setContent(box);
            return;
        }
        doSkins(box);

        long total = 0;
        int count = 0;
        for(int i=0; i<50; i++) {
            long t = time(new Callback() {
                public void call(Object o) {
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

        out.println(MailTest.class.getCanonicalName()
                +", "+avg
                +", mail gui layout test");
        out.close();
    }

    private static void doSkins(Control control) {
        control.doSkins();
        if(control instanceof Parent) {
            Parent parent = (Parent) control;
            for(Node n : parent.children()) {
                if(n instanceof Control) {
                    doSkins((Control) n);
                }
            }
        }
    }

    private static FlexBox buildGUI() {
        ArrayListModel<String> mailboxModel = new ArrayListModel<String>();
        mailboxModel.add("Inbox");
        mailboxModel.add("Drafts");
        mailboxModel.add("Sent");
        mailboxModel.add("Trash");
        mailboxModel.add("Friends");
        mailboxModel.add("Work");
        mailboxModel.add("Mailing Lists");


        FlexBox content = new VFlexBox()
            .setBoxAlign(VFlexBox.Align.Stretch)
            .add(
                    new HFlexBox()
                        .setBoxAlign(VFlexBox.Align.Top)
                        .add(new Button("").setId("get_mail"))
                        .add(new Spacer(), 1)
                        .add(new Button("").setId("delete"))
                        .add(new Button("").setId("junk"))
                        .add(new Spacer(), 1)
                        .add(new Button("").setId("reply"))
                        .add(new Button("").setId("reply_all"))
                        .add(new Button("").setId("forward"))
                        .add(new Spacer(), 1)
                        .add(new Button("").setId("new_mail"))
                        .add(new Spacer(), 1)
                        .add(new Button("").setId("note"))
                        .add(new Button("").setId("todo"))
                        .add(new Spacer(),1)
                        .add(new Textbox("Search").setPrefWidth(150))
                        .setId("toolbar")
                 , 0)
            .add(new HFlexBox()
                .setBoxAlign(HFlexBox.Align.Stretch)
                .add(new VFlexBox()
                        .setBoxAlign(FlexBox.Align.Stretch)
                        .add(new ScrollPane(new ListView().setModel(mailboxModel))
                                .setHorizontalVisiblePolicy(ScrollPane.VisiblePolicy.Never)
                                .setVerticalVisiblePolicy(ScrollPane.VisiblePolicy.WhenNeeded)
                                ,1)
                        .add(new HFlexBox()
                            .setBoxAlign(HFlexBox.Align.Stretch)
                            .add(new Button("").setId("plus"))
                            .add(new Button("").setId("activity"))
                            .add(new Button("").setId("action")),0)
                        .setPrefWidth(200)
                    ,0)

                .add(new SplitPane(true)
                    .setFirst(new ScrollPane(new TableView())
                            .setHorizontalVisiblePolicy(ScrollPane.VisiblePolicy.Never))
                    .setSecond(new Panel()
                        .setFill(FlatColor.BLUE)
                        .setWidth(100)
                        .setHeight(100))
                    .setPosition(200)
                ,1)
            ,1)
                ;
        return content;
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
