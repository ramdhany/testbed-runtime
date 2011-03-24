/**********************************************************************************************************************
 * Copyright (c) 2010, Institute of Telematics, University of Luebeck                                                 *
 * All rights reserved.                                                                                               *
 *                                                                                                                    *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the   *
 * following conditions are met:                                                                                      *
 *                                                                                                                    *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following *
 *   disclaimer.                                                                                                      *
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the        *
 *   following disclaimer in the documentation and/or other materials provided with the distribution.                 *
 * - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or        *
 *   promote products derived from this software without specific prior written permission.                           *
 *                                                                                                                    *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, *
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE      *
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,         *
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE *
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF    *
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY   *
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.                                *
 **********************************************************************************************************************/

package de.uniluebeck.itm.tr.rs.persistence.inmemory.test;

import de.uniluebeck.itm.tr.rs.persistence.RSPersistence;
import de.uniluebeck.itm.tr.rs.persistence.inmemory.InMemoryRSPersistence;
import de.uniluebeck.itm.tr.rs.singleurnprefix.SingleUrnPrefixRS;
import de.uniluebeck.itm.tr.snaa.cmdline.server.SNAAServer;
import de.uniluebeck.itm.tr.util.PropertiesUtils;
import de.uniluebeck.itm.tr.util.SecureIdGenerator;
import de.uniluebeck.itm.tr.util.UrlUtils;
import eu.wisebed.testbed.api.rs.v1.*;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.BindException;
import java.util.*;

import static org.junit.Assert.*;

public class SingleUrnPrefixInmemoryTest {

    private final SecureIdGenerator secureIdGenerator = new SecureIdGenerator();

    private RSPersistence rsPersistence;

    private SingleUrnPrefixRS rs;

    private Map<Integer, ConfidentialReservationData> reservationDataMap =
            new HashMap<Integer, ConfidentialReservationData>();

    private Map<Integer, SecretReservationKey> reservationKeyMap = new HashMap<Integer, SecretReservationKey>();

    private List<SecretAuthenticationKey> secretAuthenticationKeyList = null;

    /**
     * The point in time that all reservations of this unit test will start from.
     */
    private static DateTime reservationStartingTime = new DateTime().plusHours(1);

    /**
     * The point in time that all reservation of this unit test will end on.
     */
    private static DateTime reservationEndingTime = reservationStartingTime.plusMinutes(30);

    private static final String TESTBED_PREFIX = "urn:unittest:testbed1:";

    private static final int RESERVATION_COUNT = 5;

    private static class IntervalData {

        public DateTime from;

        public DateTime until;

        public Integer expectedReservationCount;

        public String description;

        public IntervalData(final DateTime from, final DateTime until, final Integer expectedReservationCount,
                            final String description) {
            this.from = from;
            this.until = until;
            this.expectedReservationCount = expectedReservationCount;
            this.description = description;
        }
    }

    /**
     * Map that contains a mapping between tuples of {@link org.joda.time.DateTime} instances (start, end) that stand for
     * intervals and an {@link Integer} value indicating how many reservations there should be in the interval. Used in
     * {@link SingleUrnPrefixInmemoryTest#getConfidentialReservations()} and {@link SingleUrnPrefixInmemoryTest#getConfidentialReservations()}.
     */
    private static final List<IntervalData> intervals = new ArrayList<IntervalData>();

    private static final DatatypeFactory datatypeFactory;

    private static int snaaPort;

    static {

        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        String description;

        description =
                "query interval overlaps, ranging from the exact starting point until the exact ending point in time";
        intervals.add(new IntervalData(reservationStartingTime, reservationEndingTime, RESERVATION_COUNT, description));

        description = "query interval does not overlap, since it lies before reservation interval";
        intervals.add(new IntervalData(reservationStartingTime.minusMillis(20000),
                reservationStartingTime.minusMillis(1), 0, description
        )
        );

        description = "query interval does not overlap, since it lies after reservation interval";
        intervals.add(new IntervalData(reservationEndingTime.plusMillis(1), reservationEndingTime.plusMillis(20000), 0,
                description
        )
        );

        description = "query interval overlaps on the end of the reservation interval";
        intervals.add(new IntervalData(reservationEndingTime.minusMillis(1), reservationEndingTime.plusMillis(20000),
                RESERVATION_COUNT, description
        )
        );

        description = "query interval overlaps on the start of the reservation interval";
        intervals
                .add(new IntervalData(reservationStartingTime.minusMillis(20000), reservationStartingTime.plusMillis(1),
                        RESERVATION_COUNT, description
                )
                );

        description = "query interval overlaps on the exact millisecond on reservation start";
        intervals.add(new IntervalData(reservationStartingTime.minusMillis(20000), reservationStartingTime, 0,
                description
        )
        );

        description = "query interval overlaps on the exact millisecond on reservation end";
        intervals.add(new IntervalData(reservationEndingTime, reservationEndingTime.plusMillis(20000), 0, description));

        description =
                "query interval fully overlaps, ranging from a point after reservation start until before reservation end";
        intervals.add(new IntervalData(reservationStartingTime.plusMillis(5), reservationEndingTime.minusMillis(5),
                RESERVATION_COUNT, description
        )
        );

        description =
                "query interval fully overlaps, ranging from a point before reservation start until after reservation interval";
        intervals.add(new IntervalData(reservationStartingTime.minusMillis(5), reservationEndingTime.plusMillis(5),
                RESERVATION_COUNT, description
        )
        );

        // start SNAA endpoint
        snaaPort = UrlUtils.getRandomUnprivilegedPort();
        final HashMap<String, String> snaaConfig = new HashMap<String, String>() {{

            // choose an arbitrary port between 1024 and 65535
            put("config.port", "" + snaaPort);
            put("config.snaas", "dummy1");

            put("dummy1.type", "dummy");
            put("dummy1.urnprefix", TESTBED_PREFIX);
            put("dummy1.path", "/snaa/dummy1");

        }};
        boolean started = false;

        while (!started) {
            try {
                SNAAServer.startFromProperties(PropertiesUtils.copyToProperties(snaaConfig));
            } catch (Exception e) {
                if (e.getCause() instanceof BindException) {
                    snaaPort = UrlUtils.getRandomUnprivilegedPort();
                    snaaConfig.put("config.port", "" + snaaPort);
                } else {
                    throw new RuntimeException(e);
                }
            }
            started = true;
        }
        
    }

    @Before
    public void setUp() throws Exception {

        rsPersistence = new InMemoryRSPersistence();
        String snaaEndpointUrl = "http://localhost:" + snaaPort + "/snaa/dummy1";
        rs = new SingleUrnPrefixRS(TESTBED_PREFIX, snaaEndpointUrl, null, rsPersistence);

        // create SecretAuthenticationKey for use with dummy SNAA (no pwd check)
        secretAuthenticationKeyList = new LinkedList<SecretAuthenticationKey>();
        SecretAuthenticationKey key = new SecretAuthenticationKey();
        key.setUsername("username");
        key.setSecretAuthenticationKey(secureIdGenerator.getNextId());
        key.setUrnPrefix(TESTBED_PREFIX);
        secretAuthenticationKeyList.add(key);

        // create 10 reservations of 10 different nodes (node 1 to 10)
        for (int i = 0; i < RESERVATION_COUNT; i++) {

            // create ConfidentialReservationData
            ConfidentialReservationData confiData = new ConfidentialReservationData();
            Data data = new Data();
            data.setUrnPrefix(TESTBED_PREFIX);
            data.setUsername("username");
            confiData.getData().add(data);

            confiData.setFrom(datatypeFactory.newXMLGregorianCalendar(
                    reservationStartingTime.toGregorianCalendar()
            )
            );
            confiData.setTo(datatypeFactory.newXMLGregorianCalendar(
                    reservationEndingTime.toGregorianCalendar()
            )
            );
            confiData.setUserData("username");
            confiData.getNodeURNs().add(TESTBED_PREFIX + (i + 1));

            // remember the node in a map that will be used when reserving nodes
            reservationDataMap.put(i, confiData);

        }

    }

    @After
    public void tearDown() throws Exception {
        secretAuthenticationKeyList = null;
        reservationDataMap = null;
    }

    @Test
    public void test() throws Throwable {
        makeReservations();
        getReservationBeforeDeletion();
        deleteReservationBeforeDeletion();
        getReservationAfterDeletion();
        deleteReservationAfterDeletion();
    }

    /**
     * Makes {@link de.uniluebeck.itm.tr.rs.persistence.inmemory.test.SingleUrnPrefixInmemoryTest#RESERVATION_COUNT}
     * reservations, each for different node URNs, starting at {@link de.uniluebeck.itm.tr.rs.persistence.inmemory.test.SingleUrnPrefixInmemoryTest#reservationStartingTime}
     * and stopping at {@link de.uniluebeck.itm.tr.rs.persistence.inmemory.test.SingleUrnPrefixInmemoryTest#reservationEndingTime}.
     *
     * @throws Exception
     */
    private void makeReservations() throws Exception {
        for (int i = 0; i < reservationDataMap.size(); i++) {
            reservationKeyMap.put(i, rs.makeReservation(
                    secretAuthenticationKeyList, reservationDataMap.get(i)
            ).get(0)
            );
        }
    }

    public void getReservationBeforeDeletion() throws RSExceptionException, ReservervationNotFoundExceptionException {
        for (int i = 0; i < reservationDataMap.size(); i++) {

            List<SecretReservationKey> tempKeyList = new LinkedList<SecretReservationKey>();
            tempKeyList.add(reservationKeyMap.get(i));

            ConfidentialReservationData rememberedCRD = reservationDataMap.get(i);
            ConfidentialReservationData receivedCRD = rs.getReservation(tempKeyList).get(0);

            assertEquals(rememberedCRD.getUserData(), receivedCRD.getUserData());
            assertEquals(rememberedCRD.getNodeURNs(), receivedCRD.getNodeURNs());
            assertEquals(rememberedCRD.getFrom(), receivedCRD.getFrom());
            assertEquals(rememberedCRD.getTo(), receivedCRD.getTo());

            //assertTrue(equals(reservationDataMap.get(i), singleUrnPrefixRS.getReservation(tempKeyList).get(0)));
        }
    }

    public void getReservationAfterDeletion() throws RSExceptionException {
        for (int i = 0; i < reservationDataMap.size(); i++) {
            List<SecretReservationKey> tempKeyList = new LinkedList<SecretReservationKey>();
            tempKeyList.add(reservationKeyMap.get(i));
            try {
                rs.getReservation(tempKeyList).get(0);
                fail("Should have raised an ReservervationNotFoundExceptionException");
            }
            catch (ReservervationNotFoundExceptionException e) {
            }
        }
    }

    /**
     * @throws RSExceptionException
     * @throws DatatypeConfigurationException
     */
    @Test
    public void testGetReservations() throws Exception {

        makeReservations();

        for (IntervalData id : intervals) {
            assertSame(rs.getReservations(createXmlCal(id.from), createXmlCal(id.until)).size(), id.expectedReservationCount);
        }

    }

    /**
     * Creates a {@link javax.xml.datatype.XMLGregorianCalendar} instance with the given date and time.
     *
     * @param dateTime the date and time the newly created instance shall be set to
     * @return a {@link javax.xml.datatype.XMLGregorianCalendar} instance with the given date and time
     */
    private XMLGregorianCalendar createXmlCal(DateTime dateTime) {
        return datatypeFactory.newXMLGregorianCalendar(dateTime.toGregorianCalendar());
    }

    public void deleteReservationBeforeDeletion()
            throws RSExceptionException, ReservervationNotFoundExceptionException {
        for (int i = 0; i < reservationDataMap.size(); i++) {
            List<SecretReservationKey> tempKeyList = new LinkedList<SecretReservationKey>();
            tempKeyList.add(reservationKeyMap.get(i));
            rs.deleteReservation(Collections.<SecretAuthenticationKey>emptyList(), tempKeyList);
        }
    }

    public void deleteReservationAfterDeletion() throws RSExceptionException {
        for (int i = 0; i < reservationDataMap.size(); i++) {
            List<SecretReservationKey> tempKeyList = new LinkedList<SecretReservationKey>();
            tempKeyList.add(reservationKeyMap.get(i));
            try {
                rs.deleteReservation(Collections.<SecretAuthenticationKey>emptyList(), tempKeyList);
                fail("Should have raised an ReservervationNotFoundExceptionException");
            }
            catch (ReservervationNotFoundExceptionException expected) {
            }
        }
    }

    /**
     * Checks if the reservations that have been made before are found when querying the RS with various time periods.
     *
     * @throws RSExceptionException
     * @throws DatatypeConfigurationException
     */
    @Test
    public void getConfidentialReservations() throws Exception {

        makeReservations();

        for (IntervalData id : intervals) {
            GetReservations period = createPeriod(id.from, id.until);
            int actualReservationCount = rs.getConfidentialReservations(secretAuthenticationKeyList, period).size();
            assertSame(actualReservationCount, id.expectedReservationCount);
        }

    }

    private GetReservations createPeriod(DateTime from, DateTime to) throws DatatypeConfigurationException {
        GetReservations period = new GetReservations();
        period.setFrom(datatypeFactory.newXMLGregorianCalendar(from.toGregorianCalendar()));
        period.setTo(datatypeFactory.newXMLGregorianCalendar(to.toGregorianCalendar()));
        return period;
    }
}
