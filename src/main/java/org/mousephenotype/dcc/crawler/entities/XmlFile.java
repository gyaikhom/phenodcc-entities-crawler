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
@Table(name = "xml_file", catalog = "phenodcc_tracker", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XmlFile.findAll", query = "SELECT x FROM XmlFile x"),
    @NamedQuery(name = "XmlFile.findById", query = "SELECT x FROM XmlFile x WHERE x.id = :id"),
    @NamedQuery(name = "XmlFile.findByFname", query = "SELECT x FROM XmlFile x WHERE x.fname = :fname"),
    @NamedQuery(name = "XmlFile.findByZipFname", query = "SELECT x FROM XmlFile x WHERE (x.zipId = :zipId AND x.fname = :fname)"),
    @NamedQuery(name = "XmlFile.findByCreated", query = "SELECT x FROM XmlFile x WHERE x.created = :created"),
    @NamedQuery(name = "XmlFile.findBySizeBytes", query = "SELECT x FROM XmlFile x WHERE x.sizeBytes = :sizeBytes"),
    @NamedQuery(name = "XmlFile.findByContentMd5", query = "SELECT x FROM XmlFile x WHERE x.contentMd5 = :contentMd5"),
    @NamedQuery(name = "XmlFile.findByZipDownload", query = "SELECT x FROM XmlFile x WHERE (x.zipId = :zipId AND x.fname LIKE :pattern) ORDER BY x.created ASC"),
    @NamedQuery(name = "XmlFile.findByPhaseStatusTypeAscCreated", query = "SELECT x FROM XmlFile x WHERE (x.phaseId = :phaseId AND x.statusId = :statusId AND x.fname LIKE :pattern) ORDER BY x.zipId.zfId.zaId.zipId.created ASC, x.zipId.zfId.zaId.zipId.inc ASC, x.created ASC, x.inc"),
    @NamedQuery(name = "XmlFile.findByLastUpdate", query = "SELECT x FROM XmlFile x WHERE x.lastUpdate = :lastUpdate")})
public class XmlFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(nullable = false, length = 128)
    private String fname;
    @Basic(optional = true)
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date created;
    @Basic(optional = true)
    @Column(nullable = true)
    private Long inc;
    @Basic(optional = true)
    @Column(name = "size_bytes", nullable = true)
    private long sizeBytes;
    @Basic(optional = true)
    @Column(name = "content_md5", nullable = true, length = 32)
    private String contentMd5;
    @Basic(optional = false)
    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xmlId")
    private Collection<XmlLog> xmlLogCollection;
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private AStatus statusId;
    @JoinColumn(name = "phase_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Phase phaseId;
    @JoinColumn(name = "zip_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ZipDownload zipId;
    @JoinColumn(name = "centre_id", referencedColumnName = "id")
    @ManyToOne
    private Centre centreId;

    public XmlFile() {
    }

    public XmlFile(ZipDownload zipId, String fname,
            Phase phaseId, AStatus statusId){
        this.zipId = zipId;
        this.fname = fname;
        this.phaseId = phaseId;
        this.statusId = statusId;
    }

    public XmlFile(ZipDownload zipId, String fname,
            Phase phaseId, AStatus statusId,
            Centre centreId, Date created, Long inc, long sizeBytes){
        this.zipId = zipId;
        this.fname = fname;
        this.centreId = centreId;
        this.created = created;
        this.inc = inc;
        this.phaseId = phaseId;
        this.statusId = statusId;
        this.sizeBytes = sizeBytes;
    }

    public XmlFile(ZipDownload zipId, String fname,
            Centre centreId, Date created, Long inc,
            Phase phaseId, AStatus statusId,
            long sizeBytes, String contentMd5) {
        this.zipId = zipId;
        this.fname = fname;
        this.centreId = centreId;
        this.created = created;
        this.inc = inc;
        this.phaseId = phaseId;
        this.statusId = statusId;
        this.sizeBytes = sizeBytes;
        this.contentMd5 = contentMd5;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getInc() {
        return inc;
    }

    public void setInc(Long inc) {
        this.inc = inc;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public String getContentMd5() {
        return contentMd5;
    }

    public void setContentMd5(String contentMd5) {
        this.contentMd5 = contentMd5;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @XmlTransient
    public Collection<XmlLog> getXmlLogCollection() {
        return xmlLogCollection;
    }

    public void setXmlLogCollection(Collection<XmlLog> xmlLogCollection) {
        this.xmlLogCollection = xmlLogCollection;
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

    public ZipDownload getZipId() {
        return zipId;
    }

    public void setZipId(ZipDownload zipId) {
        this.zipId = zipId;
    }

    public Centre getCentreId() {
        return centreId;
    }

    public void setCentreId(Centre centreId) {
        this.centreId = centreId;
    }
}
