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
 * - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote*
 *   products derived from this software without specific prior written permission.                                   *
 *                                                                                                                    *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, *
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE      *
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,         *
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE *
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF    *
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY   *
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.                                *
 **********************************************************************************************************************/

package de.uniluebeck.itm.gtr.wsngui.rs;

import de.uniluebeck.itm.gtr.wsngui.sessionmanagement.SessionManagementView;
import eu.wisebed.testbed.api.rs.RSServiceHelper;
import eu.wisebed.testbed.api.rs.v1.ConfidentialReservationData;
import eu.wisebed.testbed.api.rs.v1.RS;
import eu.wisebed.testbed.api.rs.v1.SecretAuthenticationKey;
import eu.wisebed.testbed.api.rs.v1.SecretReservationKey;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.xml.datatype.DatatypeFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class RSClientController {

    private static final Logger log = LoggerFactory.getLogger(RSClientController.class);

    private RSClientView view;

    private SessionManagementView sessionManagementView;

	private Map<String, String> properties;

	private ActionListener makeReservationButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            try {

                String endpointUrl = view.getEndpointUrlTextField().getText();
                RS rsService = RSServiceHelper.getRSService(endpointUrl);
                List<SecretAuthenticationKey> authenticationData = parseAuthenticationData();
                ConfidentialReservationData reservation = parseConfidentialReservationData();
                List<SecretReservationKey> secretReservationKeyList = rsService.makeReservation(authenticationData, reservation);
                displaySecretReservationKeysResult(secretReservationKeyList);

            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, e1.getMessage());
            }


        }

    };

    private ActionListener copySecretReservationKeysButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            sessionManagementView.getSecretReservationKeysTextArea().setText(view.getSecretReservationKeysTextArea().getText());
        }
    };
    
    private RSClientModel model;

    private void displaySecretReservationKeysResult(List<SecretReservationKey> secretReservationKeyList) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < secretReservationKeyList.size(); i++) {
            builder.append(secretReservationKeyList.get(i).getUrnPrefix());
            builder.append(",");
            builder.append(secretReservationKeyList.get(i).getSecretReservationKey());
            if (i < secretReservationKeyList.size() - 1) {
                builder.append("\n");
            }
        }
        view.getSecretReservationKeysTextArea().setText(builder.toString());
    }

    private ConfidentialReservationData parseConfidentialReservationData() {


        try {

            ConfidentialReservationData data = new ConfidentialReservationData();
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            DateTime fromDate = new DateTime(view.getFromDateTextField().getValue());
            DateTime toDate = new DateTime(view.getToDateTextField().getValue());
            data.setFrom(datatypeFactory.newXMLGregorianCalendar(fromDate.toGregorianCalendar()));
            data.setTo(datatypeFactory.newXMLGregorianCalendar(toDate.toGregorianCalendar()));
            data.setUserData("");
            data.getNodeURNs().addAll(parseNodeUrns());

            return data;

        } catch (Exception e) {
            log.warn("" + e, e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return null;

    }

    private List<String> parseNodeUrns() {
        return Arrays.asList(view.getNodeUrnsTextField().getText().split(","));
    }

    private List<SecretAuthenticationKey> parseAuthenticationData() {

        List<SecretAuthenticationKey> keys = new ArrayList<SecretAuthenticationKey>();
        String text = view.getSecretAuthenticationKeysTextArea().getText();

        for (String line : text.split("\n")) {

            String[] values = line.split(",");

            if (values.length == 3) {
                SecretAuthenticationKey key = new SecretAuthenticationKey();
                key.setUrnPrefix(values[0].trim());
                key.setUsername(values[1].trim());
                key.setSecretAuthenticationKey(values[2].trim());
                keys.add(key);
            }
        }

        return keys;
    }

    public RSClientController(final RSClientView view, final RSClientModel model,
							  SessionManagementView sessionManagementView, final Map<String, String> properties) {

        this.view = view;
        this.model = model;
        this.sessionManagementView = sessionManagementView;
		this.properties = properties;

		try {
            view.getEndpointUrlTextField().setText("http://" + InetAddress.getLocalHost().getHostName() + ":8889/rs");
        } catch (UnknownHostException e) {
            log.error("" + e, e);
        }

        DateTime now = new DateTime();
        DateTime then = now.plusMinutes(30);

        this.view.getFromDateTextField().setValue(now.toDate());
        this.view.getSecretAuthenticationKeysTextArea().setText("urn:wisebed:uzl1:,testbeduzl1,abc12345");
        this.view.getToDateTextField().setValue(then.toDate());
        this.view.getNodeUrnsTextField().setText("urn:wisebed:uzl1:0x1b57,urn:wisebed:uzl1:0x99af,urn:wisebed:uzl1:0x753d");

        this.view.getMakeReservationButton().addActionListener(makeReservationButtonActionListener);
        this.view.getCopySecretReservationKeysButton().addActionListener(copySecretReservationKeysButtonActionListener);

    }

}
