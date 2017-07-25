Build a Java application that can load two files, one txt with NFS data and one Excel with updated price information, and creates a new txt file with the updated prices. 
The application needs 4 buttons:

1. Load positions file & and display - Loads the positions file and displays it. This is the .txt position file
2. Load updated price file & display - Loads the price file and display it. This is the XLS file with updated prices.
3. Generate new positions file  - generates a new position files and displays it
4. Save As - dialog box allowing user to save the CSV text file to local

We only want to update prices for Options the rest will have to stay the same.
The original price can be found in Column F of the attached spreadsheet, the updated price in Column J.
The security type can be found in Column I, we are only interested in type "Option".
To map the price correctly to the positions file use the attached PDF manual for the Risk Based Haircut System. On page 30 you will find the field specification. Necessary fields are "Put/Call", "Trading Symbol", "Expiration/Delivery Year", "Expiration/Delivery Month", "Expiration/Delivery Day", "Strike Dollar", "Strike Fraction", "Security Type" and "Price".

For example:
Row 283 in the Excel shows a new price of 2.1850 for CSCO130119C17.5 (old price 2.1700) which corresponds to line 256 in the text file with these field values:

[Put/Call] = C
[Trading Symbol] = CSCO
[Expiration/Delivery Year] = 2013
[Expiration/Delivery Month] = 01
[Expiration/Delivery Day] = 19
[Strike Dollar] = 00017
[Strike Fraction] = 5000
[Security Type] = O
[Price] = 000002170000

Only change for this line would have to be
[Price] = 000002185000

346 0020OPTIONS TRCCSCO  20130119000175000000002170SO000000001500F  

346 0020OPTIONS TRCVVUS  20120922000320000S000000001000000000050F
346 0020OPTIONS TRCVVUS  20120922000320000S000000001000000000050


346 0020PROP ACCT PCRUS  20121020000400000LO
000002950000
0000000020F