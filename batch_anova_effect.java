package com.example.helloworld;
import java.io.*;
import java.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.inference.OneWayAnova;
import com.opencsv.*;
public class batch_anova_effect
{
    public static ArrayList<String[]> read_file_nist(File myFile, String subString, int featuresPerBatch, double anovaCuttoff) throws IOException {
        String line = "";
        String splitBy = ",";
        BufferedReader file = new BufferedReader(new FileReader(myFile));
        ArrayList<String> header = new ArrayList<>();
        ArrayList<String[]> newFile = new ArrayList<>();
        int count = 0;
        while((line = file.readLine()) != null)
        {
            if(count == 0)
            {
                String[] headerS = line.split(splitBy);
                header.addAll(Arrays.asList(headerS));
                header.add("pValue");
                String[] headerStr = new String[header.size()];
                headerStr = header.toArray(headerStr);
                newFile.add(headerStr);
                count += 1;
            }
            else
            {
                String[] body = line.split(splitBy);
                ArrayList<String> bodyArray= (ArrayList<String>) loopRow(body);
                ArrayList<double[]> indices = (ArrayList<double[]>) findIndices(header, bodyArray, subString, featuresPerBatch);
                if(!(indices.size() < 2))
                {
                    double anovaValue = anovaTest(indices);
                    if(anovaValue >= anovaCuttoff)
                    {
                        bodyArray.add(String.valueOf(anovaValue));
                        String[] bodyArrayStr = new String[bodyArray.size()];
                        bodyArrayStr = bodyArray.toArray(bodyArrayStr);
                        newFile.add(bodyArrayStr);
                    }
                }
                count += 1;
            }
        }
    return newFile;
    }
    private static List<String> loopRow(String[] splitRows)
    {
        List<String> line = new ArrayList<>();
        line.addAll(Arrays.asList(splitRows));
        return line;
    }
    private static List<double[]> findIndices(List<String> header, List<String> body, String subString, int featuresPerBatch)
    {
        List<double[]> indexBody = new ArrayList<>();
        double[] dArray = {};
        int batch = 0;
        for (int x = 0; x < header.size(); x++)
        {
            if (header.get(x).contains(subString) && !header.get(x).equals("") && !body.get(x).equals(""))
            {
                if(batch < featuresPerBatch)
                {
                    if(body.get(x).equals(""))
                    {
                        indexBody.clear();
                        break;
                    }
                    else
                    {
                        dArray = ArrayUtils.add(dArray,Double.parseDouble(body.get(x)));
                    }
                    batch += 1;
                }
                else
                {
                    indexBody.add(dArray);
                    dArray = new double[]{};
                    batch = 0;

                }
            }

        }
        return indexBody;
    }
    private static double anovaTest(List<double[]> indexBody)
    {
        OneWayAnova anova_test = new OneWayAnova();
        return anova_test.anovaPValue(indexBody);
    }
    public static void newBetweenBatchBlank(String filePath, ArrayList<String[]> cleanedArray) throws IOException
    {
        File newFile = new File(filePath);
        FileWriter outputFile = new FileWriter(newFile);
        CSVWriter writer = new CSVWriter(outputFile);
        writer.writeAll(cleanedArray);

        writer.close();
    }

    public static void main(String[] args) throws Exception
    {
        //1.User can input the number of NIST features are present per batch to compare in ANOVA
        //2. User can input the P-Value cuttoff that dictates whether a sample
        int featuresPerBatch = 2;
        double anovaCuttoff = 0.02;
        String path = "/Users/cconwa10/Desktop/";
        ArrayList<String[]> cleanedFile = read_file_nist(new File(path + "final_peaks_new.csv"), "NIST", featuresPerBatch, anovaCuttoff);
        newBetweenBatchBlank(path + "final_peaks_cleaned.csv", cleanedFile);
    }
}
