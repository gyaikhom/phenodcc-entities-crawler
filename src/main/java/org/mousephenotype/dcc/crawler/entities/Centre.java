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
@Table(name = "centre", catalog = "phenodcc_tracker", schema = "",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"short_name"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Centre.findAll", query = "SELECT c FROM Centre c"),
    @NamedQuery(name = "Centre.findAllActive", query = "SELECT c FROM Centre c WHERE c.isActive = true"),
    @NamedQuery(name = "Centre.findById", query = "SELECT c FROM Centre c WHERE c.id = :id"),
    @NamedQuery(name = "Centre.findByShortName", query = "SELECT c FROM Centre c WHERE c.shortName = :shortName"),
    @NamedQuery(name = "Centre.findByFullName", query = "SELECT c FROM Centre c WHERE c.fullName = :fullName"),
    @NamedQuery(name = "Centre.findByEmail", query = "SELECT c FROM Centre c WHERE c.email = :email"),
    @NamedQuery(name = "Centre.findByPhone", query = "SELECT c FROM Centre c WHERE c.phone = :phone"),
    @NamedQuery(name = "Centre.findByAddress", query = "SELECT c FROM Centre c WHERE c.address = :address"),
    @NamedQuery(name = "Centre.findByIsActive", query = "SELECT c FROM Centre c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "Centre.findByCreated", query = "SELECT c FROM Centre c WHERE c.created = :created"),
    @NamedQuery(name = "Centre.findByLastUpdate", query = "SELECT c FROM Centre c WHERE c.lastUpdate = :lastUpdate")})
public class Centre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Short id;
    @Basic(optional = false)
    @Column(name = "short_name", nullable = false, length = 32)
    private String shortName;
    @Basic(optional = true)
    @Column(name = "imits_name", nullable = true, length = 32)
    private String imitsName;
    @Basic(optional = false)
    @Column(name = "full_name", nullable = false, length = 128)
    private String fullName;
    @Basic(optional = false)
    @Column(nullable = false, length = 128)
    private String email;
    @Column(length = 14)
    private String phone;
    @Column(length = 256)
    private String address;
    @Basic(optional = false)
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "centreId")
    private Collection<FileSource> fileSourceCollection;
    @OneToMany(mappedBy = "centreId")
    private Collection<ZipFile> zipFileCollection;

    public Centre() {
    }

    public Centre(String shortName, String fullName, String address, boolean isActive, String email) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.address = address;
        this.isActive = isActive;
        this.email = email;
        this.created = new Date();
    }

    public Centre(String shortName, String fullName, String address, boolean isActive, String email, String phone) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.address = address;
        this.isActive = isActive;
        this.email = email;
        this.phone = phone;
        this.created = new Date();
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

    public String getImitsName() {
        return imitsName;
    }

    public void setImitsName(String imitsName) {
        this.imitsName = imitsName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
    public Collection<FileSource> getFileSourceCollection() {
        return fileSourceCollection;
    }

    public void setFileSourceCollection(Collection<FileSource> fileSourceCollection) {
        this.fileSourceCollection = fileSourceCollection;
    }

    @XmlTransient
    public Collection<ZipFile> getZipFileCollection() {
        return zipFileCollection;
    }

    public void setZipFileCollection(Collection<ZipFile> zipFileCollection) {
        this.zipFileCollection = zipFileCollection;
    }
}
