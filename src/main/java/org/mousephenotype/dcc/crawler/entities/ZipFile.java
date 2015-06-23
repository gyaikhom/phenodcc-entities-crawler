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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gagarine Yaikhom <g.yaikhom@har.mrc.ac.uk>
 */
@Entity
@Table(name = "zip_file", catalog = "phenodcc_tracker", schema = "",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"file_name"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZipFile.findAll", query = "SELECT z FROM ZipFile z"),
    @NamedQuery(name = "ZipFile.findById", query = "SELECT z FROM ZipFile z WHERE z.id = :id"),
    @NamedQuery(name = "ZipFile.findByFileName", query = "SELECT z FROM ZipFile z WHERE z.fileName = :fileName"),
    @NamedQuery(name = "ZipFile.findByCreated", query = "SELECT z FROM ZipFile z WHERE z.created = :created"),
    @NamedQuery(name = "ZipFile.findBySizeBytes", query = "SELECT z FROM ZipFile z WHERE z.sizeBytes = :sizeBytes"),
    @NamedQuery(name = "ZipFile.findByContentMd5", query = "SELECT z FROM ZipFile z WHERE z.contentMd5 = :contentMd5"),
    @NamedQuery(name = "ZipFile.findByLastUpdate", query = "SELECT z FROM ZipFile z WHERE z.lastUpdate = :lastUpdate")})
public class ZipFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "file_name", nullable = false, length = 128)
    private String fileName;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zipId")
    private Collection<ZipAction> zipActionCollection;
    @JoinColumn(name = "centre_id", referencedColumnName = "id")
    @ManyToOne
    private Centre centreId;

    public ZipFile() {
    }

    public ZipFile(String fileName) {
        this.fileName = fileName;
    }
    
    public ZipFile(
            String fileName,
            Centre centreId,
            Date created,
            Long inc,
            int sizeBytes) {
        this.fileName = fileName;
        this.centreId = centreId;
        this.created = created;
        this.inc = inc;
        this.sizeBytes = sizeBytes;
    }

    public ZipFile(
            String fileName,
            Centre centreId,
            Date created,
            Long inc,
            int sizeBytes,
            String contentMd5) {
        this.fileName = fileName;
        this.centreId = centreId;
        this.created = created;
        this.inc = inc;
        this.sizeBytes = sizeBytes;
        this.contentMd5 = contentMd5;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
    public Collection<ZipAction> getZipActionCollection() {
        return zipActionCollection;
    }

    public void setZipActionCollection(Collection<ZipAction> zipActionCollection) {
        this.zipActionCollection = zipActionCollection;
    }

    public Centre getCentreId() {
        return centreId;
    }

    public void setCentreId(Centre centreId) {
        this.centreId = centreId;
    }
}
