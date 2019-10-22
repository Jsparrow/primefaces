/* 
 * The MIT License
 *
 * Copyright (c) 2009-2019 PrimeTek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.primefaces.documentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponentBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to scan a component and generated the Markdown information 
 * for the AJAX behavior events the component supports.
 * <p>
 * Execute by passing component class to the command line argument.
 * Example: org.primefaces.component.inputtext.InputText
 */
public class GenerateAjaxBehaviorDoc {

    
    private static final Logger logger = LoggerFactory.getLogger(GenerateAjaxBehaviorDoc.class);

	public static void main(String[] args) {
        try {
            for (String className : args) {
                Class<?> clazz = Class.forName(className);
                generateMarkdown((UIComponentBase)clazz.newInstance());
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void generateMarkdown(UIComponentBase comp) {
        String defaultEvent = comp.getDefaultEventName();
        List<String> events = new ArrayList<>(comp.getEventNames());
        Collections.sort(events);
        String eventString = events.toString();
        eventString =  eventString.substring(eventString.indexOf("[")+1, eventString.indexOf("]"));
        logger.info("## Ajax Behavior Events");
        System.out.println();
        logger.info("The following AJAX behavior events are available for this component. If no event is specific the default event is called.  ");
        logger.info("  ");
        logger.info(new StringBuilder().append("**Default Event:** ").append(defaultEvent).append("  ").toString());
        logger.info(new StringBuilder().append("**Available Events:** ").append(eventString).append("  ").toString());
        System.out.println();
        logger.info("```xhtml");
        logger.info(String.format("<p:ajax event=\"%s\" listener=\"#{bean.handle%s}\" update=\"msgs\" />", defaultEvent,defaultEvent));
        logger.info("```");
    }
}
