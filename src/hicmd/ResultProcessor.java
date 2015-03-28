package hicmd;

import hicmd.resultprocessor.FindEarliestResult;
import hicmd.resultprocessor.HybridRewriteResult;
import hicmd.resultprocessor.HybridSearchResult;
import hicmd.resultprocessor.ModelCheckResult;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class should be passed raw hi-maude output data and in return this class
 * produces arrays of output which contain readable output. Output is processed
 * using a combination of String replace an split, which looks for specific
 * words and is made to work with version 1.0 and may not work with future
 * versions.
 *
 * @author Gary
 */
public class ResultProcessor {

    private String raw;
    private ArrayList<HybridRewriteResult> hybridRewriteResults;
    private ArrayList<HybridSearchResult> hybridSearchResults;
    private ArrayList<FindEarliestResult> findEarliestResults;
    private ArrayList<ModelCheckResult> modelCheckResults;

    public ResultProcessor(String result) throws IOException {
        //Prosperity
        raw = result;

        //Remove specific white space's
        result = result.replace('\n', ' ').replace('\t', ' ').replaceAll("( )+", " ");

        //Sort output by types and process
        hybridRewriteResults = new ArrayList<>();
        hybridSearchResults = new ArrayList<>();
        findEarliestResults = new ArrayList<>();
        modelCheckResults = new ArrayList<>();
        String[] working = result.split("rewrites:");
        for (int i = 0; i < working.length; i++) {
            if (working[i].contains("Hybrid rewrite")) {
                hybridRewriteResults.add(hybridRewrite("rewrites:" + working[i]));

            } else if (working[i].contains("Find earliest")) {
                findEarliestResults.add(findEarliest("rewrites:" + working[i]));

            } else if (working[i].contains("Hybrid search")) {
                hybridSearchResults.add(hybridSearch("rewrites:" + working[i]));

            } else if (working[i].contains("Model check")) {
                modelCheckResults.add(modelCheck("rewrites:" + working[i]));

            }
        }
    }

    public ArrayList<HybridRewriteResult> getHybridRewriteResults() {
        return hybridRewriteResults;
    }

    public ArrayList<HybridSearchResult> getHybridSearchResults() {
        return hybridSearchResults;
    }

    public ArrayList<FindEarliestResult> getFindEarliestResults() {
        return findEarliestResults;
    }

    public ArrayList<ModelCheckResult> getModelCheckResults() {
        return modelCheckResults;
    }

    private HybridSearchResult hybridSearch(String raw) {
        HybridSearchResult res = new HybridSearchResult();
        res.rewrites = getRewrites(raw);
        res.realTime = getRealTime(raw);
        res.numericalMethod = getNumericalMethod(raw);
        res.dataShown = getDataShown(raw);
        res.solutions = getSolutions(raw);
        res.source = getSource(raw);

        return res;
    }

    private ModelCheckResult modelCheck(String raw) {
        ModelCheckResult res = new ModelCheckResult();
        res.rewrites = getRewrites(raw);
        res.realTime = getRealTime(raw);
        res.numericalMethod = getNumericalMethod(raw);
        res.stepsize = getStepSize(raw);
        res.boolResult = getBoolResult(raw);

        return res;
    }

    private FindEarliestResult findEarliest(String raw) {
        FindEarliestResult res = new FindEarliestResult();
        res.rewrites = getRewrites(raw);
        res.realTime = getRealTime(raw);
        res.numericalMethod = getNumericalMethod(raw);
        res.stepsize = getStepSize(raw);
        res.dataShown = getDataShown(raw);
        res.result = getResult(raw);
        res.source = getSource(raw);
        res.inTime = getInTime(raw);

        return res;
    }

    private HybridRewriteResult hybridRewrite(String raw) {
        HybridRewriteResult res = new HybridRewriteResult();
        res.rewrites = getRewrites(raw);
        res.realTime = getRealTime(raw);
        res.numericalMethod = getNumericalMethod(raw);
        res.stepsize = getStepSize(raw);
        res.dataShown = getDataShown(raw);
        res.result = getResult(raw);
        res.source = getSource(raw);
        res.inTime = getInTime(raw);

        return res;
    }

    private String getRewrites(String raw) {
        String startStr = "rewrites: ";
        String endStr = " in";
        int start = raw.indexOf(startStr);
        int end = raw.indexOf(endStr);
        return raw.subSequence(start + startStr.length(), end).toString();
    }

    private String getRealTime(String raw) {
        String startStr = "cpu (";
        String endStr = "ms real";
        int start = raw.indexOf(startStr);
        int end = raw.indexOf(endStr);
        return raw.subSequence(start + startStr.length(), end).toString();
    }

    private String getBoolResult(String raw) {
        String startStr = "Result Bool : ";
        int start = raw.indexOf(startStr);
        int end = raw.length();
        if (raw.subSequence(start + startStr.length(), end).toString().contains("true")) {
            raw = "true";
        } else {
            raw = "false";
        }
        return raw;
    }

    private String getNumericalMethod(String raw) {
        return (raw.contains("euler")) ? "euler" : "Runge Kutta";
    }

    private String getStepSize(String raw) {
        String startStr = "stepsize ";
        String endStr = "discreteswitch";
        int start = raw.indexOf(startStr);
        int end = raw.indexOf(endStr);
        return raw.subSequence(start + startStr.length(), end).toString();
    }

    private String getDataShown(String raw) {
        String startStr = "dataShown : ";
        String endStr = ",result";
        int start = raw.indexOf(startStr);
        int end = raw.indexOf(endStr);
        try {
            raw = raw.subSequence(start + startStr.length(), end).toString();
        } catch (StringIndexOutOfBoundsException ex) {
            raw = "No Solution.";
        }
        return raw;
    }

    private String[][] getResult(String raw) {
        String[][] data;
        String startStr, endStr;
        if (raw.contains("\",source")) {
            startStr = ",result : \"";
            endStr = "\"";
        } else {
            startStr = ",result : \"";
            endStr = "\"";
        }
        
        int start = raw.indexOf(startStr);
        raw = raw.substring(start + startStr.length());
        
        int end = raw.indexOf(endStr);
        raw = raw.subSequence(0, end).toString();

        raw = raw.replace("\\n", "\n");
        raw = raw.replace(",", "\t");

        //Put result string into an array for processing later...
        String[] rows = raw.split("\n");
        data = new String[rows.length][];

        int i = 0;
        for (String row : rows) {
            data[i] = row.split("\t");
            i++;
        }
        
        return data;
    }

    private String[][][] getSolutions(String raw) {

        String[] rawSolutions = raw.split("Solution");
        String[][][] solutions = new String[rawSolutions.length - 1][][];

        int count = 0;
        for (String s : rawSolutions) {
            if (s.contains(",result : \"")) {
                solutions[count] = getResult(s);
                count++;
            }
        }

        return solutions;
    }

    private String[] getSource(String raw) {
        String[] res;
        String startStr, endStr;
        if (raw.contains("source :(")) {
            startStr = "source :(";
            endStr = "),";
        } else {
            startStr = "source : ";
            endStr = ",";
        }
        int start = raw.indexOf(startStr);
        raw = raw.substring(start + startStr.length());
        
        int end = raw.indexOf(endStr);
        try {
            raw = raw.subSequence(0, end).toString().trim();
            res = raw.split(" ");
        } catch (StringIndexOutOfBoundsException ex) {
            res = new String[] {"No Solution."};
        }
        return res;
    }

    private String getInTime(String raw) {
        String startStr = "),time : ";
        int start = raw.indexOf(startStr);
        raw = raw.substring(start + startStr.length());

        String endStr = " >";
        int end = raw.indexOf(endStr);
        return raw.subSequence(0, end).toString();
    }
}
