# Cowen Challenge
-------
This is an application created in Java with the Swing GUI.

## How to use
--------
1. Compile and run application
2. Click "Load Positions" to upload a .txt file in the format that was provided. This is the original positions file and loads it on the top section.
3. Click "Load Updated Prices" to upload a .xls file in the format that was provided. This is the new positions file and loads it in the middle section.
4. After those two files are uploaded, click "Generate New Positions" to generate the new positions in the bottom section. This will be in the same format as the .txt file. Changes only apply to Security Type that are Options and only the price is updated..
5. Click "Save As" to save the new positions as a new .txt file. 
-----
## Known Issues
* The row amount and column amount has been hard coded for this exercise to make sure the data has been normalized. There is a case where the POI library count more rows than there are and columns than there are possibly due to an error in the excel sheet.