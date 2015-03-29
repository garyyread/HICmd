package hicmd.resultprocessor;

/**
 * Class represents data from a HI-Maude hmc command
 *
 * @author Gary
 */
public class ModelCheckResult {

    /**
     *
     */
    public String rewrites;

    /**
     *
     */
    public String realTime;

    /**
     *
     */
    public String numericalMethod;

    /**
     *
     */
    public String stepsize;

    /**
     *
     */
    public String boolResult;

    public String toString() {
        //Create and add JPanel to display textual result
        String result = "Rewrites: " + rewrites + "\n"
                + "CPU real time: " + realTime + "\n"
                + "Numerical Method: " + numericalMethod + "\n"
                + "Stepsize: " + stepsize + "\n";

        result += "Result: " + boolResult;

        return result;
    }
}