package com.joshondesign.apidifftool;

import com.joshondesign.xml.Doc;
import com.joshondesign.xml.Elem;
import com.joshondesign.xml.XMLParser;
import com.joshondesign.xml.XMLWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: Oct 26, 2010
 * Time: 7:21:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private static final String QUALIFIED_NAME = "qualifiedName";

    public static void main(String ... args) throws Exception {
        p("diffing apis");
        File fileA = new File("../out/r0.api.xml");
        File fileB = new File("../out/r1.api.xml");

        File output = new File("../out/out.xml");
        XMLWriter out = new XMLWriter(output);

        Doc adoc = XMLParser.parse(fileA);
        Doc bdoc = XMLParser.parse(fileB);

        List<Elem> aclasses = new ArrayList<Elem>();
        List<Elem> bclasses = new ArrayList<Elem>();



        for(Elem cls : adoc.xpath("/javadoc/package/class")) {
            aclasses.add(cls);
        }
        for(Elem cls : bdoc.xpath("/javadoc/package/class")) {
            bclasses.add(cls);
        }


        out.header();
        out.start("changes");
        out.start("classes");

        for(Elem cls : bclasses) {
            if(!contains(QUALIFIED_NAME,cls,aclasses)) {
                p("added class: " + cls.attr(QUALIFIED_NAME));
                out.start("add","qualifiedName",cls.attr(QUALIFIED_NAME)).end();
            }
        }
        for(Elem cls : aclasses) {
            if(!contains(QUALIFIED_NAME, cls,bclasses)) {
                p("removed class: " + cls.attr("qualifiedName"));
                out.start("remove","qualifiedName",cls.attr(QUALIFIED_NAME)).end();
            }
        }
        out.end();//classes


        out.start("methods");
        for(Elem bcls : bclasses) {
            if(contains(QUALIFIED_NAME, bcls,aclasses)) {
                Elem acls = get(QUALIFIED_NAME,bcls,aclasses);
                //p("comparing: " + bcls.attr("qualifiedName"));
                List<Elem> ameths = new ArrayList<Elem>();
                List<Elem> bmeths = new ArrayList<Elem>();
                for(Elem meth : acls.xpath("method")) {
                    ameths.add(meth);
                }
                for(Elem meth : bcls.xpath("method")) {
                    bmeths.add(meth);
                }

                for(Elem meth : bmeths) {
                    if(!contains("name", meth, ameths)) {
                        p("added method: " + meth.attr("name") + " to class " + bcls.attr(QUALIFIED_NAME));
                        out.start("add","name",meth.attr("name"),"class",bcls.attr(QUALIFIED_NAME)).end();
                    }
                }
                for(Elem meth : ameths) {
                    if(!contains("name", meth, bmeths)) {
                        p("removed method: " + meth.attr("name") + " from class " + bcls.attr(QUALIFIED_NAME));
                        out.start("remove","name",meth.attr("name"),"class",bcls.attr(QUALIFIED_NAME)).end();
                    }
                }
            }
        }
        out.end();//methods

        out.end();
        out.close();
        
    }

    private static Elem get(String attr, Elem bcls, List<Elem> aclasses) {
        for(Elem acls : aclasses) {
            if(acls.attr(attr).equals(bcls.attr(attr))) {
                return acls;
            }
        }
        return null;
    }

    private static boolean contains(String attr, Elem bcls, List<Elem> aclasses) {
        for(Elem acls : aclasses) {
            if(acls.attr(attr).equals(bcls.attr(attr))) {
                //p("found: " + bcls.attr("qualifiedName"));
                return true;
            }
        }
        return false;
    }

    private static void p(String s) {
        System.out.println(s);
    }
}
