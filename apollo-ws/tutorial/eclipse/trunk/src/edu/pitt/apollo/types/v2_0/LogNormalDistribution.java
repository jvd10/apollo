
package edu.pitt.apollo.types.v2_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LogNormalDistribution complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LogNormalDistribution">
 *   &lt;complexContent>
 *     &lt;extension base="{http://types.apollo.pitt.edu/v2_0/}ContinuousParametricProbabilityDistribution">
 *       &lt;sequence>
 *         &lt;element name="mean" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="standardDeviation" type="{http://types.apollo.pitt.edu/v2_0/}NonNegativeDouble"/>
 *         &lt;element name="shiftRight" type="{http://types.apollo.pitt.edu/v2_0/}NonNegativeDouble"/>
 *         &lt;element name="cutTailAt" type="{http://types.apollo.pitt.edu/v2_0/}PositiveDouble"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogNormalDistribution", propOrder = {
    "mean",
    "standardDeviation",
    "shiftRight",
    "cutTailAt"
})
public class LogNormalDistribution
    extends ContinuousParametricProbabilityDistribution
{

    protected double mean;
    protected double standardDeviation;
    protected double shiftRight;
    protected double cutTailAt;

    /**
     * Gets the value of the mean property.
     * 
     */
    public double getMean() {
        return mean;
    }

    /**
     * Sets the value of the mean property.
     * 
     */
    public void setMean(double value) {
        this.mean = value;
    }

    /**
     * Gets the value of the standardDeviation property.
     * 
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * Sets the value of the standardDeviation property.
     * 
     */
    public void setStandardDeviation(double value) {
        this.standardDeviation = value;
    }

    /**
     * Gets the value of the shiftRight property.
     * 
     */
    public double getShiftRight() {
        return shiftRight;
    }

    /**
     * Sets the value of the shiftRight property.
     * 
     */
    public void setShiftRight(double value) {
        this.shiftRight = value;
    }

    /**
     * Gets the value of the cutTailAt property.
     * 
     */
    public double getCutTailAt() {
        return cutTailAt;
    }

    /**
     * Sets the value of the cutTailAt property.
     * 
     */
    public void setCutTailAt(double value) {
        this.cutTailAt = value;
    }

}
