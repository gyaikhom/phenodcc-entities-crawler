/*******************************************************************************
 *                       The PhenoDCC Tracker Database                         *
 *                                                                             *
 * DESCRIPTION:                                                                *
 * This specifies the database that the PhenoDCC uses for tracking processing  *
 * status of phenotyping data collected at various research centres around     *
 * the world. The tracker system assumes a `pull' oriented design, where the   *
 * research centres export data from their LIMS into the IMPC-standardised XML *
 * format, which is then stored locally in one of the file servers hosted by   *
 * the centre. Then, the crawler at the PhenoDCC visits all of the active ftp       *
 * servers that are currently hosted by the centres and adds new data to the   *
 * phenotype database. Before the data is made available to the general public,*
 * and to other researchers, the PhenoDCC validates the raw XML data and then  *
 * carry out quality control checks on the phenotype information, before the   *
 * data is uploaded to the Cental Data Archive (CDA) database at EBI.          *
 *                                                                             *
 * To update the data with new phenotyping results, the PhenoDCC periodically  *
 * crawls the file servers of all of the participating centres to check for    *
 * newly added data files. If there are unprocessed files, they are first      *
 * downloaded to the PhenoDCC local storage and then processed. The processing *
 * happens in phases, and only those data files which pass all of the phases   *
 * without any error, or failure, are added to the Pre-QC database. If there   *
 * are issues with any of the data files, the tracker system provides the      *
 * centre, which uploaded with data files, with mechanisms to resolve issues.  *
 *                                                                             *
 * Copyright (c) 2012, The Medical Research Council Harwell                    *
 * Written by: G. Yaikhom (g.yaikhom@har.mrc.ac.uk)                            *
 *                                                                             * 
 ******************************************************************************/

drop database if exists phenodcc_tracker;
create database phenodcc_tracker;
grant all on phenodcc_tracker.* to 'dccadmin'@'localhost';
flush privileges;
use phenodcc_tracker;

/* All of the phenotype data are collected by experimenters at various research centres. */
drop table if exists centre;
create table centre (
       id smallint unsigned not null auto_increment,
       short_name varchar(32) not null, /* short name for centre (ILAR code) */
       imits_name varchar(32), /* iMits centre code */
       full_name varchar(128) not null, /* centre name in full */
       email varchar(128) not null, /* contact email id */
       phone varchar(14), /* must include country and area codes */
       address varchar(256), /* centre address (we haven't bothered to normalise the address column) */
       is_active boolean not null default true, /* is the centre active */
       created datetime not null, /* when was the centre added */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (short_name), /* queries might use this more often than Id */
       primary key (id),
       unique (short_name)
) engine = innodb;

/* File sources host data files using standard file transfer protocols. The crawler supports a selection of communication protocols listed here. */
drop table if exists source_protocol;
create table source_protocol (
       id smallint unsigned not null auto_increment,
       short_name varchar(8) not null, /* the name of the communication protocol */
       description text, /* a brief description of what the source provides */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (short_name),
       primary key (id)
) engine = innodb;

/* File sources have state. They are either available, under maintenance, removed, or are in some other state. */
drop table if exists resource_state;
create table resource_state (
       id smallint unsigned not null auto_increment,
       short_name varchar(32) not null, /* the unique short name for the state */
       description text, /* a brief description of what the state means */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (short_name),
       primary key (id)
) engine = innodb;

/* A centre could host multiple file servers that provide the raw data as zipped XML documents. If a centre is deleted, all of its file sources must also be deleted. It is preferable, however, to update the centre status 'is_active' field instead of actually deleting the centre. This helps us preserve referential integrity. */
drop table if exists file_source;
create table file_source (
       id smallint unsigned not null auto_increment,
       centre_id smallint unsigned not null, /* each of the file data sources is associated with a centre */
       protocol_id smallint unsigned not null, /* the protocol used by the file server */
       hostname varchar(512) not null, /* the hostname of the file server */
       username varchar(128) not null, /* the username for accessing the resource */
       accesskey varchar(256) not null, /* either password, or authkey location */
       base_path varchar(256), /* base path which contains IMPC data */
       state_id smallint unsigned not null, /* the current state of the server */
       created datetime not null, /* when was the file source added */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (hostname),
       primary key (id),
       foreign key (centre_id) references centre(id) on update cascade on delete cascade,
       foreign key (protocol_id) references source_protocol(id) on update cascade on delete cascade,
       foreign key (state_id) references resource_state(id) on update cascade on delete cascade
) engine = innodb;

/* We must now begin tracking the processing status of the zip files as they are being processed for addition to the PhenoDCC databases. This includes the XML documents contained inside these zip files. All of the zip files and XML documents undergo various processing phases.

It is a requirement that entries to this table must preserve the temporal ordering of the phases. In other words, the phase that follows another in the processing pipeline must have an identifier with higher ordinal value. This is a requirement because the PhenoDCC tracking system relies on the phase identifier for making several decisions. We could have added a separate `priority' or `temporal' column, however, following this ordering convention makes such a field redundant, and unnecessary. */
drop table if exists phase;
create table phase (
       id smallint unsigned not null auto_increment, /* this will be used as the processing phase code */
       short_name varchar(64) not null, /* the short name of the processing phase */
       description text, /* a brief description of what happens in this processing phase */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (short_name),
       primary key (id),
       unique (short_name)
) engine = innodb;

/* Every processing phase is associated with it a status, which is either 'pending', 'running', 'done' (successful completion), 'cancelled', or 'failed' (termination due to processing errors). Again, the temporal ordering of the status must be maintained. In other words, the status for a phase cannot change arbitrarily; they must follow: pending, running, done, cancelled, or failed. Note that the PhenoDCC tracker system relies on the failure status having the highest priority. */
drop table if exists a_status;
create table a_status (
       id smallint unsigned not null auto_increment, /* this will be used as the progress status code */
       short_name varchar(64) not null, /* the short name we will use for the status */
       description text, /* a brief description of the status */
       rgba char(8) not null, /* Red, Green, Blue and Alpha channels for the status colour */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (short_name),
       primary key (id),
       unique (short_name)
) engine = innodb;

/* All of the zip files adhere to a specific PhenoDCC data-file naming convention. Note that files created by one centre may be hosted at various file data sources hosted at other centres. If a centre is deleted, we must not delete the zip files generated by that centre. This is because other centres might be hosting the zip file to improve availability through redundancy. It is preferable, however, to update the centre status 'is_active' field instead of actually deleting the centre, thus preserving referential integrity. */
drop table if exists zip_file;
create table zip_file (
       id bigint unsigned not null auto_increment,
       file_name varchar(128) not null, /* the name of the zip file using IMPC file naming convention */
       centre_id smallint unsigned, /* which centre produced the data (this is encoded in the file name) */
       created date, /* when was the data file created (this is encoded in the file name) */
       inc bigint unsigned, /* file increment number (this is encoded in the file name) */
       size_bytes bigint unsigned, /* size of the file in bytes (for monitoring performance) */
       content_md5 char(32), /* this file check-sum must match the one available at the file servers (curently not used) */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (file_name), /* because of the upload naming convention, file names are likely to be unique */
       primary key (id),
       unique (file_name),
       foreign key (centre_id) references centre(id) on update cascade on delete set null
) engine = innodb;

/* Each file downloaded from a resource host must be processed in a particular manner. The following list all of the ways of processing a file. */
drop table if exists processing_type;
create table processing_type (
       id smallint unsigned not null auto_increment,
       short_name varchar(32) not null, /* the unique name for the processing type */
       description text, /* a brief description of what the process does to a file */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (short_name),
       primary key (id)
) engine = innodb;

/* Since the file server directory determines the type of action on the file, we must record this requested action while crawling the file servers. A zip file, therefore, is always associated with an action, independent of where it was downloaded from. By convention, the file crawlers will only crawl the following three directories: `add', `edit', and `delete'. Each of these directories must be at the root of the file source. Any subdirectories inside these three directories will be ignored: only zip files will be tracked. Furthermore, only zip files with names that conform to the PhenoDCC naming convention will be downloaded to the staging area. These download decisions are recorded in the (phase, status) fields. */
drop table if exists zip_action;
create table zip_action (
       id bigint unsigned not null auto_increment,
       zip_id bigint unsigned not null, /* the zip file to be processed */
       todo_id smallint unsigned not null, /* how to process the zip file */
       phase_id smallint unsigned not null, /* the current processing phase of the zip file */
       status_id smallint unsigned not null, /* the current status of the processing phase */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       primary key (id),
       unique (zip_id, todo_id), /* there should be only one combination */
       foreign key (zip_id) references zip_file(id) on update cascade on delete cascade,
       foreign key (todo_id) references processing_type(id) on update cascade on delete restrict,
       foreign key (phase_id) references phase(id) on update cascade on delete restrict,
       foreign key (status_id) references a_status(id) on update cascade on delete restrict
) engine = innodb;

/* A zip file may be hosted at multiple file sources. This includes the action since a zip file may be placed at two 'delete' directories on different file sources. We, therefore, maintain information about which zip files with action (a zip action) is currently being hosted by an file source. Using this table, the PhenoDCC tracker will be able to retrieve the zip action from multiple file sources, in case the preferred sources have failed to deliver the zip files. */
drop table if exists file_source_has_zip;
create table file_source_has_zip (
       id bigint unsigned not null auto_increment,
       za_id bigint unsigned not null, /* the zip file with action */
       file_source_id smallint unsigned not null, /* the file server hosting the zip file */
       num_retries tinyint unsigned not null, /* number of download attempts after a download error */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       primary key (id),
       unique (za_id, file_source_id), /* there should be only one combination */
       foreign key (za_id) references zip_action(id) on update cascade on delete cascade,
       foreign key (file_source_id) references file_source(id) on update cascade on delete cascade
) engine = innodb;

/* The PhenoDCC downloads a zipped file containing XML documents from an file source. A zipped data file may be sourced from multiple file servers hosted at various centres (including those that did not create the data file). This redundancy improves reliability, although there is a small performance penalty since the same data file may appear multiple times when listing directory contents during a file crawl. The zip file and file source combination is chosen based on the preference of the file sources. New records to the `zip_download' table are added only when higher preference download attempts have failed.

The progress of each download attempt should be recorded for troubleshooting. Hence, each download records its current phase and status. The phase and status of a zip_action record must reflect the latest zip_download record for that zip_action. However, if the zip_download status ended in (unzip, failed), the zip file is corrupt. Since this corruption did not occur during the file transfer, we must cancel processing this zip_action and flag its status as (unzip, corrupted). The centre is then expected to fix the corrupted file. For the moment, though, since MD5 checksum is not provided by the centres, we have removed this failure handling scenario (hence, no status named `corrupted'). */
drop table if exists zip_download;
create table zip_download (
       id bigint unsigned not null auto_increment,
       zf_id bigint unsigned not null, /* the zip file and file source combination for the download */
       request datetime not null, /* when did we send the download request (for monitoring performance) */
       received datetime not null, /* when did we actually receive the complete file (for monitoring performance) */
       downloaded_size_bytes bigint unsigned not null default 0, /* for monitoring download progress */
       phase_id smallint unsigned not null, /* the current transfer state of the zip file */
       status_id smallint unsigned not null, /* the current status of the processing */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       primary key (id),
       foreign key (zf_id) references file_source_has_zip(id) on update cascade on delete cascade,
       foreign key (phase_id) references phase(id) on update cascade on delete restrict,
       foreign key (status_id) references a_status(id) on update cascade on delete restrict
) engine = innodb;

/* All XML documents are communicated as zipped files. A zip file may contain multiple XML documents. If the parent zip file is deleted, all of the XML documents inside that zip file must also be deleted. You must not delete a status if there are XML documents still using that status. */
drop table if exists xml_file;
create table xml_file (
       id bigint unsigned not null auto_increment,
       zip_id bigint unsigned not null, /* to which downloaded zip file does this XML document belong to */
       fname varchar(128) not null, /* the name of the XML document using IMPC file naming convention */
       centre_id smallint unsigned, /* which centre produced the data (this is encoded in the file name) */
       created date, /* when was the data created (this is encoded in the file name) */
       inc bigint unsigned, /* file increment number (this is encoded in the file name) */
       size_bytes bigint unsigned, /* size of the file in bytes (for monitoring performance) */
       content_md5 char(32), /* this file check-sum must match the one available inside the zip file (currently not used) */
       phase_id smallint unsigned not null, /* the current processing phase of the XML document */
       status_id smallint unsigned not null, /* the current status of the XML document */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (fname), /* highly likely that we will search XML documents using their name */
       index (created), /* highly likely that the XML documents will be sorted using creation date */
       primary key (id),
       foreign key (centre_id) references centre(id) on update cascade on delete set null,
       foreign key (zip_id) references zip_download(id) on update cascade on delete cascade,
       foreign key (phase_id) references phase(id) on update cascade on delete restrict,
       foreign key (status_id) references a_status(id) on update cascade on delete restrict
) engine = innodb;

/* We track the failure of a processing phase using exceptions. Based on the type of failures that we anticipate, we maintain a list of all the possible exceptions. These provide specific details concerning the status. At the beginning, this table is left empty. New exceptions are created by the PhenoDCC tracker database as required. */
drop table if exists an_exception;
create table an_exception (
       id smallint unsigned not null auto_increment, /* this will be used as the exception code */
       short_name varchar(64) not null, /* the short name for the exception */
       description text, /* a brief description of the exception */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (short_name), /* the exceptions are updated rarely, but are queried frequently */
       primary key (id),
       unique (short_name)
) engine = innodb;

/* Additional details concerning a cancellation or failure of a processing phase is recorded separately. Exceptions must not be deleted while some error logs are still using them. There can be no zip error log without the corresponding downloaded zip file. */
drop table if exists zip_log;
create table zip_log (
       id bigint unsigned not null auto_increment,
       zip_id bigint unsigned not null, /* to which downloaded zip file does this log information relevant */
       exception_id smallint unsigned not null, /* what was the exception that caused the termination */
       message text, /* a free-form message providing further details specific to the file */
       last_update timestamp not null default current_timestamp on update current_timestamp, /* for log sequencing */
       index (exception_id), /* highly likely to query processing errors using exception type */
       primary key (id),
       foreign key (zip_id) references zip_download(id) on update cascade on delete cascade,
       foreign key (exception_id) references an_exception(id) on update cascade on delete restrict
) engine = innodb;

/* For XML processing phases, we can provide more in-depth diagnostic information, such as line number and column number. Note here that the `exception_id' field should provide general information about the exception; whereas, the `message' field should only provide information that is specific to a specific XML document. This saves database space by avoiding unnecessary data redundancy. Exceptions must not be deleted while some error logs are still using them. There can be no XML error log without the corresponding XML document. */
drop table if exists xml_log;
create table xml_log (
       id bigint unsigned not null auto_increment,
       xml_id bigint unsigned not null, /* to which XML document does this log information relevant to */
       exception_id smallint unsigned not null, /* what was the exception that caused the termination */
       message text, /* a free-form message providing further details specific to the file */
       line int unsigned, /* optional line number */
       col int unsigned, /* optional column number on the line */
       last_update timestamp not null default current_timestamp on update current_timestamp, /* for log sequencing */
       index (exception_id), /* highly likely to query processing errors using exception type */
       primary key (id),
       foreign key (xml_id) references xml_file(id) on update cascade on delete cascade,
       foreign key (exception_id) references an_exception(id) on update cascade on delete restrict
) engine = innodb;

/* We shall now add tables that are used to track the history of the crawler executions. These tables will record information about when the crawler was executed, when it finished executions, the exit status, and details about the various stages that was executed.
Each execution of the crawler happens inside a crawling session. Only one session should be active at any time. The crawler partially ensures this by using the phenodcc.lock in the crawler dirctory. All of the crawling tasks are executed as part of a session. A crawling session is considered to have finished successfully if all of its tasks were successful. If there was a failure, the status should store the identifier of the task that failed. */
drop table if exists crawling_session;
create table crawling_session (
       id bigint unsigned not null auto_increment,
       start_time datetime not null, /* when crawling began */
       finish_time datetime, /* when did crawling finished (null if not finished) */
       status smallint not null, /* how did it end? 0 - success, > 1 if failure (cid of crawling task) */
       created datetime not null, /* when was this record created */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       primary key (id)
) engine = innodb;

/* All of the tasks inside a session has a session_task entry. This records the statistics for the task */
drop table if exists session_task;
create table session_task (
       id bigint unsigned not null auto_increment,
       session_id bigint unsigned not null, /* crawling session */
       phase_id smallint unsigned not null, /* task identifier */
       comment varchar(256), /* additional information about the task */
       start_time datetime not null, /* when the task began */
       finish_time datetime, /* when did the task finish (null if not finished) */
       status smallint not null, /* how did it end? 0 - success, 1 if failure */
       created datetime not null, /* when was this record created */
       last_update timestamp not null default current_timestamp on update current_timestamp,
       index (session_id),
       index (phase_id),
       primary key (id),
       foreign key (session_id) references crawling_session(id) on update cascade on delete cascade,
       foreign key (phase_id) references phase(id) on update cascade on delete restrict
) engine = innodb;

/* End of MySQL script */

