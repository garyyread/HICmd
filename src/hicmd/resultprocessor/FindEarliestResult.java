package hicmd.resultprocessor;

/**
 * Class represents data from a HI-Maude hfind earliest command
 *
 * @author Gary
 */
public class FindEarliestResult {

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
    public String dataShown;

    /**
     *
     */
    public String[] source;

    /**
     *
     */
    public String inTime;

    /**
     *
     */
    public String[][] result;

    @Override
    public String toString() {
        //Create and add JPanel to display textual result
        String res = "Rewrites: " + rewrites + "\n"
                + "CPU real time: " + realTime + "\n"
                + "Numerical Method: " + numericalMethod + "\n"
                + "Stepsize: " + stepsize + "\n"
                + "In time: " + inTime + "\n"
                + "Data shown: " + dataShown + "\n"
                + "Source: \n";
        for (String s : source) {
            res += "\t" + s + "\n";
        }

        res += "Result: ";
        res += "[" + "Time" + ", " + "Stepsize";
        for (String s : source) {
            res += ", " + s;
        }
        res += "]" + "\n";

        for (String[] row : result) {
            res += "\t";

            for (String d : row) {
                res += d + "\t";
            }

            res += "\n";
        }

        return res;
    }
}
