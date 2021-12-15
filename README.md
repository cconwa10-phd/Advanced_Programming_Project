# Advanced_Programming_Project
Description:
This program is meant to take aligned MS1 or MS2 feature tables and perform OneWayAnova on each sample (row) to determine if there is significant difference between the internal standards between each batch (features/columns). Those that are above the user cuttoff for P-Values with be retained and passed to a csv file, while those samples that did not pass the threshold will not be in the final output.  This allows the user to view those samples of significance.
Installation Instructions:
1. Clone the project from GitHub
Example:
1. Modify path of the Main function, path variable, for both the input file and output file. Take the final_peaks_new.csv file and use that as the input to the function.
2. You can set the P-Value cuttoff score (anovaCuttoff), for this example, 0.02 has been used
3. You can also set the number of internal standard features that are contained within each batch (featuresPerBatch), for this example, 2 has been used since there are 2 internal standard features per batch across 3 NIST internal batches.
4. Run the program, you should have a csv file output that matches the final_peaks_cleaned.csv within the repository
