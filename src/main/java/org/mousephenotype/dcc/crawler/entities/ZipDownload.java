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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gagarine Yaikhom <g.yaikhom@har.mrc.ac.uk>
 */
@Entity
@Table(name = "zip_download", catalog = "phenodcc_tracker", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZipDownload.findAll", query = "SELECT z FROM ZipDownload z"),
    @NamedQuery(name = "ZipDownload.findById", query = "SELECT z FROM ZipDownload z WHERE z.id = :id"),
    @NamedQuery(name = "ZipDownload.findByRequest", query = "SELECT z FROM ZipDownload z WHERE z.request = :request"),
    @NamedQuery(name = "ZipDownload.findByReceived", query = "SELECT z FROM ZipDownload z WHERE z.received = :received"),
    @NamedQuery(name = "ZipDownload.findByDownloadedSizeBytes", query = "SELECT z FROM ZipDownload z WHERE z.downloadedSizeBytes = :downloadedSizeBytes"),
    @NamedQuery(name = "ZipDownload.findByLastUpdate", query = "SELECT z FROM ZipDownload z WHERE z.lastUpdate = :lastUpdate"),
    @NamedQuery(name = "ZipDownload.findByZipAction", query = "SELECT z FROM ZipDownload z JOIN z.zfId zf WHERE (z.phaseId = :phaseId AND z.statusId = :statusId AND zf.zaId = :zaId)")
})
public class ZipDownload implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date request;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date received;
    @Basic(optional = false)
    @Column(name = "downloaded_size_bytes", nullable = false)
    private long downloadedSizeBytes;
    @Basic(optional = false)
    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private AStatus statusId;
    @JoinColumn(name = "phase_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Phase phaseId;
    @JoinColumn(name = "zf_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private FileSourceHasZip zfId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zipId")
    private Collection<ZipLog> zipLogCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zipId")
    private Collection<XmlFile> xmlFileCollection;

    public ZipDownload() {
    }

    public ZipDownload(FileSourceHasZip zfId, Date request, Date received, Phase phaseId, AStatus statusId) {
        this.zfId = zfId;
        this.request = request;
        this.received = received;
        this.phaseId = phaseId;
        this.statusId = statusId;
        this.downloadedSizeBytes = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRequest() {
        return request;
    }

    public void setRequest(Date request) {
        this.request = request;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public long getDownloadedSizeBytes() {
        return downloadedSizeBytes;
    }

    public void setDownloadedSizeBytes(long downloadedSizeBytes) {
        this.downloadedSizeBytes = downloadedSizeBytes;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public AStatus getStatusId() {
        return statusId;
    }

    public void setStatusId(AStatus statusId) {
        this.statusId = statusId;
    }

    public Phase getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Phase phaseId) {
        this.phaseId = phaseId;
    }

    public FileSourceHasZip getZfId() {
        return zfId;
    }

    public void setZfId(FileSourceHasZip zfId) {
        this.zfId = zfId;
    }

    @XmlTransient
    public Collection<ZipLog> getZipLogCollection() {
        return zipLogCollection;
    }

    public void setZipLogCollection(Collection<ZipLog> zipLogCollection) {
        this.zipLogCollection = zipLogCollection;
    }

    @XmlTransient
    public Collection<XmlFile> getXmlFileCollection() {
        return xmlFileCollection;
    }

    public void setXmlFileCollection(Collection<XmlFile> xmlFileCollection) {
        this.xmlFileCollection = xmlFileCollection;
    }
}
