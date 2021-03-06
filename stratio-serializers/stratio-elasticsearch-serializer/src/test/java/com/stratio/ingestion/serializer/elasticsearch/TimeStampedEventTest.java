/**
 * Copyright (C) 2014 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.ingestion.serializer.elasticsearch;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.Map;

import org.apache.flume.event.EventBuilder;
import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableMap;

@RunWith(JUnit4.class)
public class TimeStampedEventTest {

    @Test
    public void checkTimestamp() {
        long now = System.currentTimeMillis();
        for (Map<String, String> headers : Arrays.asList(
                ImmutableMap.of("@timestamp", Long.toString(now)),
                ImmutableMap.of("@timestamp", ISODateTimeFormat.dateTime().withZoneUTC().print(now))
                )) {
            final TimeStampedEvent timestampedEvent = new TimeStampedEvent(EventBuilder.withBody(new byte[0], headers));
            assertThat(timestampedEvent.getTimestamp()).isEqualTo(now);
            assertThat(timestampedEvent.getHeaders().get("@timestamp")).isEqualTo(ISODateTimeFormat.dateTime().withZoneUTC().print(now));
        }

        DateTimeUtils.setCurrentMillisFixed(now);
        for (Map<String, String> headers : Arrays.asList(
                ImmutableMap.<String,String>of(),
                ImmutableMap.of("timestamp", ""),
                ImmutableMap.of("@timestamp", "")
        )) {
            final TimeStampedEvent timestampedEvent = new TimeStampedEvent(EventBuilder.withBody(new byte[0], headers));
            assertThat(timestampedEvent.getTimestamp()).isEqualTo(now);
            assertThat(timestampedEvent.getHeaders().get("@timestamp")).isEqualTo(ISODateTimeFormat.dateTime().withZoneUTC().print(now));
        }
        DateTimeUtils.setCurrentMillisSystem();
    }

}
