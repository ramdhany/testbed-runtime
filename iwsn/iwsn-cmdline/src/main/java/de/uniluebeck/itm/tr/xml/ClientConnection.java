//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.14 at 01:47:45 PM MEZ 
//


package de.uniluebeck.itm.tr.xml;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClientConnection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClientConnection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://itm.uniluebeck.de/tr/xml}AddressAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClientConnection")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-11-14T01:47:45+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class ClientConnection {

    @XmlAttribute(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-11-14T01:47:45+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String type;
    @XmlAttribute(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-11-14T01:47:45+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String address;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-11-14T01:47:45+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-11-14T01:47:45+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-11-14T01:47:45+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-11-14T01:47:45+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setAddress(String value) {
        this.address = value;
    }

}
