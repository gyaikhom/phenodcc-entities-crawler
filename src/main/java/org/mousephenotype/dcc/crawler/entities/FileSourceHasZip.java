/*
 * Copyright 2012 Medical Research Council Harwell.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mousephenotype.dcc.crawler.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gagarine Yaikhom <g.yaikhom@har.mrc.ac.uk>
 */
@Entity
@Table(name = "file_source_has_zip", catalog = "phenodcc_tracker", schema = "",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"za_id", "file_source_id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FileSourceHasZip.findAll", query = "SELECT f FROM FileSourceHasZip f"),
    @NamedQuery(name = "FileSourceHasZip.findById", query = "SELECT f FROM FileSourceHasZip f WHERE f.id = :id"),
    @NamedQuery(name = "FileSourceHasZip.findByNumRetries", query = "SELECT f FROM FileSourceHasZip f WHERE f.numRetries = :numRetries"),
    @NamedQuery(name = "FileSourceHasZip.findByLastUpdate", query = "SELECT f FROM FileSourceHasZip f WHERE f.lastUpdate = :lastUpdate"),
    @NamedQuery(name = "FileSourceHasZip.findByAction", query = "SELECT f FROM FileSourceHasZip f WHERE f.zaId = :zaId"),
    @NamedQuery(name = "FileSourceHasZip.findBySourceAction", query = "SELECT f FROM FileSourceHasZip f WHERE (f.fileSourceId = :fileSourceId AND f.zaId = :zaId)")
})
public class FileSourceHasZip implements Serializable {

    private static final long serialVersionUID = 1L;
    @Transient
    private short rating = 0; // for sorting ftp sources
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "num_retries", nullable = false)
    private short numRetries;
    @Basic(optional = false)
    @Column(name = "last_update", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zfId")
    private Collection<ZipDownload> zipDownloadCollection;
    @JoinColumn(name = "file_source_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private FileSource fileSourceId;
    @JoinColumn(name = "za_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ZipAction zaId;

    public FileSourceHasZip() {
    }

    public FileSourceHasZip(FileSource fileSourceId, ZipAction zaId) {
        this.fileSourceId = fileSourceId;
        this.zaId = zaId;
        this.numRetries = 0;
    }

    public FileSourceHasZip(FileSource fileSourceId, ZipAction zaId, short numRetries) {
        this.fileSourceId = fileSourceId;
        this.zaId = zaId;
        this.numRetries = numRetries;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getNumRetries() {
        return numRetries;
    }

    public void setNumRetries(short numRetries) {
        this.numRetries = numRetries;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @XmlTransient
    public Collection<ZipDownload> getZipDownloadCollection() {
        return zipDownloadCollection;
    }

    public void setZipDownloadCollection(Collection<ZipDownload> zipDownloadCollection) {
        this.zipDownloadCollection = zipDownloadCollection;
    }

    public FileSource getFileSourceId() {
        return fileSourceId;
    }

    public void setFileSourceId(FileSource fileSourceId) {
        this.fileSourceId = fileSourceId;
    }

    public ZipAction getZaId() {
        return zaId;
    }

    public void setZaId(ZipAction zaId) {
        this.zaId = zaId;
    }
}
