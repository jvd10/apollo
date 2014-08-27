package edu.pitt.apollo.timeseriesvisualizer.types;

/**
 *
 * Author: Nick Millett
 Email: nick.millett@gmail.com
 Date: Feb 27, 2014
 Time: 3:35:57 PM
 Class: TimeSeriesCurveTypeEnum
 IDE: NetBeans 6.9.1
 */
public enum TimeSeriesCurveTypeEnum {

    SUSCEPTIBLE("susceptible"),
    LATENT("latent"),
    INFECTIOUS("infectious"),
    RECOVERED("recovered"),
    NEWLY_LATENT("newly latent"),
	NEWLY_DECEASED("newly deceased"),
	PROPHYLACTICS_GIVEN("prophylactics given");
    
    public static final TimeSeriesCurveTypeEnum[] CURVE_TYPES_FOR_PREVALENCE_CHART = {SUSCEPTIBLE, LATENT, INFECTIOUS, RECOVERED};
    public static final TimeSeriesCurveTypeEnum[] CURVE_TYPES_FOR_INCIDENCE_CHART = {NEWLY_LATENT};
	public static final TimeSeriesCurveTypeEnum[] CURVE_TYPES_FOR_NEWLY_DECEASED_CHART = {NEWLY_DECEASED};
	public static final TimeSeriesCurveTypeEnum[] CURVE_TYPES_FOR_TREATMENT_COUNTS_CHART = {PROPHYLACTICS_GIVEN};
	
    private final String value;

    private TimeSeriesCurveTypeEnum(String s) {
        value = s;
    }

    public String getValue() {
        return value;
    }
}
