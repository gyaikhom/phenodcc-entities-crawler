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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gagarine Yaikhom <g.yaikhom@har.mrc.ac.uk>
 */
@Entity
@Table(name = "crawling_session", catalog = "phenodcc_tracker", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CrawlingSession.findAll", query = "SELECT c FROM CrawlingSession c"),
    @NamedQuery(name = "CrawlingSession.findById", query = "SELECT c FROM CrawlingSession c WHERE c.id = :id"),
    @NamedQuery(name = "CrawlingSession.findByStartTime", query = "SELECT c FROM CrawlingSession c WHERE c.startTime = :startTime"),
    @NamedQuery(name = "CrawlingSession.findByFinishTime", query = "SELECT c FROM CrawlingSession c WHERE c.finishTime = :finishTime"),
    @NamedQuery(name = "CrawlingSession.findByStatus", query = "SELECT c FROM CrawlingSession c WHERE c.status = :status"),
    @NamedQuery(name = "CrawlingSession.findByCreated", query = "SELECT c FROM CrawlingSession c WHERE c.created = :created"),
    @NamedQuery(name = "CrawlingSession.findByLastUpdate", query = "SELECT c FROM CrawlingSession c WHERE c.lastUpdate = :lastUpdate")})
public class CrawlingSession implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Basic(optional = true)
    @Column(name = "finish_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishTime;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private short status;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionId")
    private Collection<SessionTask> sessionTasksCollection;

    public CrawlingSession() {
        startTime = new Date();
        created = startTime;
        finishTime = null; /* not yet finished */
        status = 0; /* assume that everything is fine */
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
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
    public Collection<SessionTask> getSessionTasksCollection() {
        return sessionTasksCollection;
    }

    public void setSessionTasksCollection(Collection<SessionTask> sessionTasksCollection) {
        this.sessionTasksCollection = sessionTasksCollection;
    }
}
