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
import javax.persistence.Lob;
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
@Table(name = "resource_state", catalog = "phenodcc_tracker", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResourceState.findAll", query = "SELECT r FROM ResourceState r"),
    @NamedQuery(name = "ResourceState.findById", query = "SELECT r FROM ResourceState r WHERE r.id = :id"),
    @NamedQuery(name = "ResourceState.findByShortName", query = "SELECT r FROM ResourceState r WHERE r.shortName = :shortName"),
    @NamedQuery(name = "ResourceState.findByLastUpdate", query = "SELECT r FROM ResourceState r WHERE r.lastUpdate = :lastUpdate")})
public class ResourceState implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Short id;
    @Basic(optional = false)
    @Column(name = "short_name", nullable = false, length = 32)
    private String shortName;
    @Lob
    @Column(length = 65535)
    private String description;
    @Basic(optional = false)
    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stateId")
    private Collection<FileSource> fileSourceCollection;

    public ResourceState() {
    }

    public ResourceState(String shortName, String description) {
        this.shortName = shortName;
        this.description = description;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @XmlTransient
    public Collection<FileSource> getFileSourceCollection() {
        return fileSourceCollection;
    }

    public void setFileSourceCollection(Collection<FileSource> fileSourceCollection) {
        this.fileSourceCollection = fileSourceCollection;
    }
}
