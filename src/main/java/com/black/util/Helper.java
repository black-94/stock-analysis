package com.black.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Helper {
    public static String stack(Throwable e){
        StringWriter stringWriter=new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
