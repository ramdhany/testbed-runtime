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

package de.uniluebeck.itm.gtr.wsngui.wsn;

import de.uniluebeck.itm.tr.util.SecureIdGenerator;
import de.uniluebeck.itm.tr.util.UrlUtils;
import eu.wisebed.testbed.api.wsn.Constants;
import eu.wisebed.testbed.api.wsn.v22.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.List;


@WebService(
        serviceName = "WSNService",
        targetNamespace = Constants.NAMESPACE_WSN_SERVICE,
        portName = "WSNPort",
        endpointInterface = Constants.ENDPOINT_INTERFACE_WSN_SERVICE
)
public class WSNServiceImpl implements WSN {

    private static final Logger log = LoggerFactory.getLogger(WSNServiceImpl.class);

    private String endpointUrl;

    private Endpoint endpoint;

    private SecureIdGenerator generator = new SecureIdGenerator();

    public WSNServiceImpl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public void start() throws Exception {
        String bindAllInterfacesUrl = UrlUtils.convertHostToZeros(endpointUrl);

        log.debug("Starting WISEBED WSN dummy service...");
        log.debug("Endpoint URL: {}", endpointUrl);
        log.debug("Binding  URL: {}", bindAllInterfacesUrl);

        endpoint = Endpoint.publish(bindAllInterfacesUrl, this);

        log.info("Started WISEBED WSN dummy service on {}", bindAllInterfacesUrl);
    }

    public void stop() {

        if (endpoint != null) {
            endpoint.stop();
            log.info("Stopped WISEBED WSN dummy service on {}", endpointUrl);
        }

    }

    @Override
    public void addController(
            @WebParam(name = "controllerEndpointUrl", targetNamespace = "") String controllerEndpointUrl) {
        // nothing to do, this is a dummy ;-)
    }

    @Override
    public void removeController(
            @WebParam(name = "controllerEndpointUrl", targetNamespace = "") String controllerEndpointUrl) {
        // nothing to do, this is a dummy ;-)
    }

    @Override
    public String send(@WebParam(name = "nodeIds", targetNamespace = "") List<String> nodeIds, @WebParam(name = "message", targetNamespace = "") Message message) {
        log.info("WSNServiceImpl.send");
        return generator.getNextId();
    }

    @Override
    public String getVersion() {
        log.info("WSNServiceImpl.getVersion");
        return "2.1";
    }

    @Override
    public String areNodesAlive(@WebParam(name = "nodes", targetNamespace = "") List<String> nodes) {
        log.info("WSNServiceImpl.checkAreNodesAlive");
        return generator.getNextId();
    }

    @Override
    public String defineNetwork(@WebParam(name = "newNetwork", targetNamespace = "") String newNetwork) {
        log.info("WSNServiceImpl.defineNetwork");
        return generator.getNextId();
    }

    @Override
    public String describeCapabilities(@WebParam(name = "capability", targetNamespace = "") String capability) throws UnsupportedOperationException_Exception {
        log.info("WSNServiceImpl.describeCapabilities");
        return generator.getNextId();
    }

    @Override
    public String destroyVirtualLink(@WebParam(name = "sourceNode", targetNamespace = "") String sourceNode, @WebParam(name = "targetNode", targetNamespace = "") String targetNode) {
        log.info("WSNServiceImpl.destroyVirtualLink");
        return generator.getNextId();
    }

    @Override
    public String disableNode(@WebParam(name = "node", targetNamespace = "") String node) {
        log.info("WSNServiceImpl.disableNode");
        return generator.getNextId();
    }

    @Override
    public String disablePhysicalLink(@WebParam(name = "nodeA", targetNamespace = "") String nodeA, @WebParam(name = "nodeB", targetNamespace = "") String nodeB) {
        log.info("WSNServiceImpl.disablePhysicalLink");
        return generator.getNextId();
    }

    @Override
    public String enableNode(@WebParam(name = "node", targetNamespace = "") String node) {
        log.info("WSNServiceImpl.enableNode");
        return generator.getNextId();
    }

    @Override
    public String enablePhysicalLink(@WebParam(name = "nodeA", targetNamespace = "") String nodeA, @WebParam(name = "nodeB", targetNamespace = "") String nodeB) {
        log.info("WSNServiceImpl.enablePhysicalLink");
        return generator.getNextId();
    }

    @Override
    public String flashPrograms(@WebParam(name = "nodeIds", targetNamespace = "") List<String> nodeIds, @WebParam(name = "programIndices", targetNamespace = "") List<Integer> programIndices, @WebParam(name = "programs", targetNamespace = "") List<Program> programs) {
        log.info("WSNServiceImpl.flashPrograms");
        return generator.getNextId();
    }

    @Override
    public List<String> getFilters() {
        log.info("WSNServiceImpl.getFilters");
        return new ArrayList<String>();
    }

    @Override
    public List<String> getNeighbourhood(@WebParam(name = "node", targetNamespace = "") String node) throws UnknownNodeUrnException_Exception {
        log.info("WSNServiceImpl.getNeighbourhood");
        return new ArrayList<String>();
    }

    @Override
    public String getNetwork() {
        log.info("WSNServiceImpl.getNetwork");
        return "<network>dummy network description</network>";
    }

    @Override
    public String getPropertyValueOf(@WebParam(name = "node", targetNamespace = "") String node, @WebParam(name = "propertyName", targetNamespace = "") String propertyName) throws UnknownNodeUrnException_Exception {
        log.info("WSNServiceImpl.getPropertyValueOf");
        return generator.getNextId();
    }

    @Override
    public String resetNodes(@WebParam(name = "nodes", targetNamespace = "") List<String> nodes) {
        log.info("WSNServiceImpl.resetNodes");
        return generator.getNextId();
    }

    @Override
    public String setStartTime(@WebParam(name = "time", targetNamespace = "") XMLGregorianCalendar time) {
        log.info("WSNServiceImpl.setStartTime");
        return generator.getNextId();
    }

    @Override
    public String setVirtualLink(@WebParam(name = "sourceNode", targetNamespace = "") String sourceNode, @WebParam(name = "targetNode", targetNamespace = "") String targetNode, @WebParam(name = "remoteServiceInstance", targetNamespace = "") String remoteServiceInstance, @WebParam(name = "parameters", targetNamespace = "") List<String> parameters, @WebParam(name = "filters", targetNamespace = "") List<String> filters) {
        log.info("WSNServiceImpl.setVirtualLink");
        return generator.getNextId();
    }
}
