//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.30 at 11:48:09 AM MESZ 
//


package de.uniluebeck.itm.tr.runtime.wsnapp.xml;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigInteger;


/**
 * <p>Java class for WsnDevice complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="WsnDevice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="urn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;choice>
 *           &lt;element name="serialinterface" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="autodetection-mac" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsnDevice", propOrder = {
		"urn",
		"id",
		"type",
		"serialinterface",
		"autodetectionMac"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
		comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class WsnDevice {

	@XmlElement(required = true)
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	protected String urn;

	@XmlElement(required = true)
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	protected String id;

	@XmlElement(required = true)
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	protected String type;

	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	protected String serialinterface;

	@XmlElement(name = "autodetection-mac")
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	protected String autodetectionMac;

	/**
	 * Gets the value of the urn property.
	 *
	 * @return possible object is {@link String }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public String getUrn() {
		return urn;
	}

	/**
	 * Sets the value of the urn property.
	 *
	 * @param value allowed object is {@link String }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public void setUrn(String value) {
		this.urn = value;
	}

	/**
	 * Gets the value of the id property.
	 *
	 * @return possible object is {@link BigInteger }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 *
	 * @param value allowed object is {@link BigInteger }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the type property.
	 *
	 * @return possible object is {@link String }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 *
	 * @param value allowed object is {@link String }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public void setType(String value) {
		this.type = value;
	}

	/**
	 * Gets the value of the serialinterface property.
	 *
	 * @return possible object is {@link String }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public String getSerialinterface() {
		return serialinterface;
	}

	/**
	 * Sets the value of the serialinterface property.
	 *
	 * @param value allowed object is {@link String }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public void setSerialinterface(String value) {
		this.serialinterface = value;
	}

	/**
	 * Gets the value of the autodetectionMac property.
	 *
	 * @return possible object is {@link String }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public String getAutodetectionMac() {
		return autodetectionMac;
	}

	/**
	 * Sets the value of the autodetectionMac property.
	 *
	 * @param value allowed object is {@link String }
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2010-06-30T11:48:09+02:00",
			comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
	public void setAutodetectionMac(String value) {
		this.autodetectionMac = value;
	}

}
