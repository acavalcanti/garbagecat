/**********************************************************************************************************************
 * garbagecat                                                                                                         *
 *                                                                                                                    *
 * Copyright (c) 2008-2016 Red Hat, Inc.                                                                              *
 *                                                                                                                    * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse *
 * Public License v1.0 which accompanies this distribution, and is available at                                       *
 * http://www.eclipse.org/legal/epl-v10.html.                                                                         *
 *                                                                                                                    *
 * Contributors:                                                                                                      *
 *    Red Hat, Inc. - initial API and implementation                                                                  *
 *********************************************************************************************************************/
package org.eclipselabs.garbagecat.domain.jdk;

import org.eclipselabs.garbagecat.util.jdk.JdkUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TestSerialNewEvent extends TestCase {

    public void testIsBlocking() {
        String logLine = "7.798: [GC 7.798: [DefNew: 37172K->3631K(39296K), 0.0209300 secs] "
                + "41677K->10314K(126720K), 0.0210210 secs]";
        Assert.assertTrue(JdkUtil.LogEventType.SERIAL_NEW.toString() + " not indentified as blocking.",
                JdkUtil.isBlocking(JdkUtil.identifyEventType(logLine)));
    }

    public void testLogLine() {
        String logLine = "7.798: [GC 7.798: [DefNew: 37172K->3631K(39296K), 0.0209300 secs] "
                + "41677K->10314K(126720K), 0.0210210 secs]";
        Assert.assertTrue("Log line not recognized as " + JdkUtil.LogEventType.SERIAL_NEW.toString() + ".",
                SerialNewEvent.match(logLine));
        SerialNewEvent event = new SerialNewEvent(logLine);
        Assert.assertEquals("Time stamp not parsed correctly.", 7798, event.getTimestamp());
        Assert.assertEquals("Young begin size not parsed correctly.", 37172, event.getYoungOccupancyInit());
        Assert.assertEquals("Young end size not parsed correctly.", 3631, event.getYoungOccupancyEnd());
        Assert.assertEquals("Young available size not parsed correctly.", 39296, event.getYoungSpace());
        Assert.assertEquals("Old begin size not parsed correctly.", 4505, event.getOldOccupancyInit());
        Assert.assertEquals("Old end size not parsed correctly.", 6683, event.getOldOccupancyEnd());
        Assert.assertEquals("Old allocation size not parsed correctly.", 87424, event.getOldSpace());
        Assert.assertEquals("Duration not parsed correctly.", 21, event.getDuration());
    }

    public void testLogLineWhitespaceAtEnd() {
        String logLine = "7.798: [GC 7.798: [DefNew: 37172K->3631K(39296K), 0.0209300 secs] "
                + "41677K->10314K(126720K), 0.0210210 secs] ";
        Assert.assertTrue("Log line not recognized as " + JdkUtil.LogEventType.SERIAL_NEW.toString() + ".",
                SerialNewEvent.match(logLine));
    }

    public void testLogLineNoSpaceAfterGC() {
        String logLine = "4.296: [GC4.296: [DefNew: 68160K->8512K(76672K), 0.0528470 secs] "
                + "68160K->11664K(1325760K), 0.0530640 secs] [Times: user=0.04 sys=0.00, real=0.05 secs]";
        Assert.assertTrue("Log line not recognized as " + JdkUtil.LogEventType.SERIAL_NEW.toString() + ".",
                SerialNewEvent.match(logLine));
        SerialNewEvent event = new SerialNewEvent(logLine);
        Assert.assertEquals("Time stamp not parsed correctly.", 4296, event.getTimestamp());
        Assert.assertEquals("Young begin size not parsed correctly.", 68160, event.getYoungOccupancyInit());
        Assert.assertEquals("Young end size not parsed correctly.", 8512, event.getYoungOccupancyEnd());
        Assert.assertEquals("Young available size not parsed correctly.", 76672, event.getYoungSpace());
        Assert.assertEquals("Old begin size not parsed correctly.", 68160 - 68160, event.getOldOccupancyInit());
        Assert.assertEquals("Old end size not parsed correctly.", 11664 - 8512, event.getOldOccupancyEnd());
        Assert.assertEquals("Old allocation size not parsed correctly.", 1325760 - 76672, event.getOldSpace());
        Assert.assertEquals("Duration not parsed correctly.", 53, event.getDuration());
    }

    public void testLogLineDatestamp() {
        String logLine = "2016-11-22T09:07:01.358+0100: 1,319: [GC2016-11-22T09:07:01.359+0100: 1,320: [DefNew: "
                + "68160K->4425K(76672K), 0,0354890 secs] 68160K->4425K(3137216K), 0,0360580 secs] "
                + "[Times: user=0,04 sys=0,00, real=0,03 secs]";
        Assert.assertTrue("Log line not recognized as " + JdkUtil.LogEventType.SERIAL_NEW.toString() + ".",
                SerialNewEvent.match(logLine));
        SerialNewEvent event = new SerialNewEvent(logLine);
        Assert.assertEquals("Time stamp not parsed correctly.", 1319, event.getTimestamp());
        Assert.assertEquals("Young begin size not parsed correctly.", 68160, event.getYoungOccupancyInit());
        Assert.assertEquals("Young end size not parsed correctly.", 4425, event.getYoungOccupancyEnd());
        Assert.assertEquals("Young available size not parsed correctly.", 76672, event.getYoungSpace());
        Assert.assertEquals("Old begin size not parsed correctly.", 68160 - 68160, event.getOldOccupancyInit());
        Assert.assertEquals("Old end size not parsed correctly.", 4425 - 4425, event.getOldOccupancyEnd());
        Assert.assertEquals("Old allocation size not parsed correctly.", 3137216 - 76672, event.getOldSpace());
        Assert.assertEquals("Duration not parsed correctly.", 36, event.getDuration());
    }
}
