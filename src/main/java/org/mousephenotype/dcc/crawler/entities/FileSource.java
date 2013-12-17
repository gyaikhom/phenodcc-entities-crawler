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
@Table(name = "file_source", catalog = "phenodcc_tracker", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FileSource.findAll", query = "SELECT f FROM FileSource f"),
    @NamedQuery(name = "FileSource.findById", query = "SELECT f FROM FileSource f WHERE f.id = :id"),
    @NamedQuery(name = "FileSource.findByHostname", query = "SELECT f FROM FileSource f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FileSource.findByUsername", query = "SELECT f FROM FileSource f WHERE f.username = :username"),
    @NamedQuery(name = "FileSource.findByAccesskey", query = "SELECT f FROM FileSource f WHERE f.accesskey = :accesskey"),
    @NamedQuery(name = "FileSource.findByBasePath", query = "SELECT f FROM FileSource f WHERE f.basePath = :basePath"),
    @NamedQuery(name = "FileSource.findByCreated", query = "SELECT f FROM FileSource f WHERE f.created = :created"),
    @NamedQuery(name = "FileSource.findByLastUpdate", query = "SELECT f FROM FileSource f WHERE f.lastUpdate = :lastUpdate"),
    @NamedQuery(name = "FileSource.findByCentreState", query = "SELECT f FROM FileSource f WHERE (f.centreId = :centreId AND f.stateId = :stateId)")})
public class FileSource implements Serializable {
    // status of the ftp source

    public static final String AVAILABLE = "available";
    public static final String MAINTENANCE = "maintenance";
    public static final String REMOVED = "removed";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Short id;
    @Basic(optional = false)
    @Column(nullable = false, length = 512)
    private String hostname;
    @Basic(optional = false)
    @Column(nullable = false, length = 128)
    private String username;
    @Basic(optional = false)
    @Column(nullable = false, length = 256)
    private String accesskey;
    @Column(name = "base_path", length = 256)
    private String basePath;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fileSourceId")
    private Collection<FileSourceHasZip> fileSourceHasZipCollection;
    @JoinColumn(name = "state_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ResourceState stateId;
    @JoinColumn(name = "protocol_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private SourceProtocol protocolId;
    @JoinColumn(name = "centre_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Centre centreId;

    public FileSource() {
    }

    public FileSource(Short id) {
        this.id = id;
    }

    public FileSource(Short id, String hostname, String username, String accesskey, Date created, Date lastUpdate) {
        this.id = id;
        this.hostname = hostname;
        this.username = username;
        this.accesskey = accesskey;
        this.created = created;
        this.lastUpdate = lastUpdate;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getBasePath() {
        if (basePath == null) {
            return "";
        } else {
            return basePath;
        }
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @XmlTransient
    public Collection<FileSourceHasZip> getFileSourceHasZipCollection() {
        return fileSourceHasZipCollection;
    }

    public void setFileSourceHasZipCollection(Collection<FileSourceHasZip> fileSourceHasZipCollection) {
        this.fileSourceHasZipCollection = fileSourceHasZipCollection;
    }

    public ResourceState getStateId() {
        return stateId;
    }

    public void setStateId(ResourceState stateId) {
        this.stateId = stateId;
    }

    public SourceProtocol getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(SourceProtocol protocolId) {
        this.protocolId = protocolId;
    }

    public Centre getCentreId() {
        return centreId;
    }

    public void setCentreId(Centre centreId) {
        this.centreId = centreId;
    }
}
