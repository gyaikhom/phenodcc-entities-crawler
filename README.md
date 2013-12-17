# Crawler and Tracker Entities

This project implements the entities used by the PhenoDCC
**crawler**. The crawler is a multi-threaded Java application that is
responsible for visiting FTP/sFTP servers hosted by various
phenotyping centres, and retrieving new phenotype data for collection
and processing at the Data Coordination Centre at [MRC
Harwell](http://har.mrc.ac.uk/).

## Process and database schema

This project also specifies the database schema (see
`src/main/resources/phenodcc_tracker.sql`) that the PhenoDCC uses for
tracking processing status of phenotyping data collected from various
research centres around the world. The tracker system assumes a *pull*
oriented design, where the research centres export data from their
Laboratory Information Management System (LIMS) into the
[IMPC](http://www.mousephenotype.org/)-standardised XML format
specified in [1] and [2], which is then stored locally in one of the
file servers hosted by the centre.

The crawler at the PhenoDCC then visits all of the active ftp servers
that are currently hosted by all of the participating research centres
and adds new data to the phenotype database. Furthermore, before the
data is made available to the general public, and to other
researchers, the PhenoDCC validates the raw XML data and then carry
out quality control checks on the phenotype information, before
the data is finally uploaded to the Cental Data Archive (CDA) at
[EMBL-EBI](http://www.ebi.ac.uk/).

To update the data with new phenotyping results, the PhenoDCC periodically
crawls the file servers of all of the participating centres to check for
newly added data files. If there are unprocessed files, they are first
downloaded to the PhenoDCC local storage and then processed. The processing
happens in phases, and only those data files which pass all of the phases
without any error, or failure, are added to the Pre-QC database. If there
are issues with any of the data files, the tracker system provides the
centre, which uploaded with data files, with mechanisms to resolve these issues.

[1]: http://www.mousephenotype.org/sites/mousephenotype.org/files/files/specimen_definition.pdf
[2]: http://www.mousephenotype.org/sites/mousephenotype.org/files/files/procedure_definition.pdf
