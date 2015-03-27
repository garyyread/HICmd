package hicmd.resultprocessor;

/**
 * Class represents data from a HI-Maude hsearch command
 *
 * @author Gary
 */
public class HybridSearchResult {

    public String rewrites;
    public String realTime;
    public String numericalMethod;
    public String dataShown;
    public String[] source;
    public String[][][] solutions;

    @Override
    public String toString() {
        int j = 1;
        String dataStr = "";
        for (String[][] solution : solutions) {
            dataStr += "Solution " + j + ": ";
            dataStr += "[" + "Time" + ", " + "Stepsize";
            for (String s : source) {
                dataStr += ", " + s;
            }
            dataStr += "]" + "\n";

            for (String[] row : solution) {
                dataStr += "\t";

                for (String d : row) {
                    dataStr += d + "\t";
                }

                dataStr += "\n";
            }
        }

        //Create and add JPanel to display textual result
        String result = "Rewrites: " + rewrites + "\n"
                + "CPU real time: " + realTime + "\n"
                + "Numerical Method: " + numericalMethod + "\n"
                + "Data shown: " + dataShown + "\n"
                + "Source: \n";

        for (String s : source) {
            result += "\t" + s + "\n";
        }

        result += dataStr;
        return result;
    }
}
