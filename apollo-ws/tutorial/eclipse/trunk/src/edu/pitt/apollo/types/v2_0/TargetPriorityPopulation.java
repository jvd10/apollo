
package edu.pitt.apollo.types.v2_0;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for TargetPriorityPopulation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TargetPriorityPopulation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="label" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="fractionOfTargetPopulationToPrioritize" type="{http://types.apollo.pitt.edu/v2_0/}Fraction"/>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;choice>
 *           &lt;element name="targetPopulationDefinition" type="{http://types.apollo.pitt.edu/v2_0/}PopulationStrataDefinition"/>
 *           &lt;element name="targetPopulationDefinitionEnum" type="{http://types.apollo.pitt.edu/v2_0/}TargetPopulationDefinitionEnum"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetPriorityPopulation", propOrder = {
    "label",
    "fractionOfTargetPopulationToPrioritize",
    "priority",
    "targetPopulationDefinition",
    "targetPopulationDefinitionEnum"
})
public class TargetPriorityPopulation {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String label;
    protected double fractionOfTargetPopulationToPrioritize;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger priority;
    protected PopulationStrataDefinition targetPopulationDefinition;
    protected TargetPopulationDefinitionEnum targetPopulationDefinitionEnum;

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the fractionOfTargetPopulationToPrioritize property.
     * 
     */
    public double getFractionOfTargetPopulationToPrioritize() {
        return fractionOfTargetPopulationToPrioritize;
    }

    /**
     * Sets the value of the fractionOfTargetPopulationToPrioritize property.
     * 
     */
    public void setFractionOfTargetPopulationToPrioritize(double value) {
        this.fractionOfTargetPopulationToPrioritize = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPriority(BigInteger value) {
        this.priority = value;
    }

    /**
     * Gets the value of the targetPopulationDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link PopulationStrataDefinition }
     *     
     */
    public PopulationStrataDefinition getTargetPopulationDefinition() {
        return targetPopulationDefinition;
    }

    /**
     * Sets the value of the targetPopulationDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link PopulationStrataDefinition }
     *     
     */
    public void setTargetPopulationDefinition(PopulationStrataDefinition value) {
        this.targetPopulationDefinition = value;
    }

    /**
     * Gets the value of the targetPopulationDefinitionEnum property.
     * 
     * @return
     *     possible object is
     *     {@link TargetPopulationDefinitionEnum }
     *     
     */
    public TargetPopulationDefinitionEnum getTargetPopulationDefinitionEnum() {
        return targetPopulationDefinitionEnum;
    }

    /**
     * Sets the value of the targetPopulationDefinitionEnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetPopulationDefinitionEnum }
     *     
     */
    public void setTargetPopulationDefinitionEnum(TargetPopulationDefinitionEnum value) {
        this.targetPopulationDefinitionEnum = value;
    }

}