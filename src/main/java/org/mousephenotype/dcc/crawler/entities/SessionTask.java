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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gagarine Yaikhom <g.yaikhom@har.mrc.ac.uk>
 */
@Entity
@Table(name = "session_task", catalog = "phenodcc_tracker", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SessionTask.findAll", query = "SELECT s FROM SessionTask s"),
    @NamedQuery(name = "SessionTask.findById", query = "SELECT s FROM SessionTask s WHERE s.id = :id"),
    @NamedQuery(name = "SessionTask.findByStartTime", query = "SELECT s FROM SessionTask s WHERE s.startTime = :startTime"),
    @NamedQuery(name = "SessionTask.findByFinishTime", query = "SELECT s FROM SessionTask s WHERE s.finishTime = :finishTime"),
    @NamedQuery(name = "SessionTask.findByStatus", query = "SELECT s FROM SessionTask s WHERE s.status = :status"),
    @NamedQuery(name = "SessionTask.findByCreated", query = "SELECT s FROM SessionTask s WHERE s.created = :created"),
    @NamedQuery(name = "SessionTask.findByLastUpdate", query = "SELECT s FROM SessionTask s WHERE s.lastUpdate = :lastUpdate"),
    @NamedQuery(name = "SessionTask.findByPhaseId", query = "SELECT s FROM SessionTask s WHERE s.phaseId = :phaseId"),
    @NamedQuery(name = "SessionTask.findBySessionId", query = "SELECT s FROM SessionTask s WHERE s.sessionId = :sessionId")})
public class SessionTask implements Serializable {

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
    @Basic(optional = true)
    @Column(length = 256, nullable = true)
    private String comment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @JoinColumn(name = "phase_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Phase phaseId;
    @JoinColumn(name = "session_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CrawlingSession sessionId;

    public SessionTask() {
    }

    public SessionTask(Phase phaseId, CrawlingSession sessionId) {
        startTime = new Date();
        created = startTime;
        finishTime = null; /* task has not finished */
        status = 0; /* assume everthing is fine */
        this.phaseId = phaseId;
        this.sessionId = sessionId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Phase getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Phase phaseId) {
        this.phaseId = phaseId;
    }

    public CrawlingSession getSessionId() {
        return sessionId;
    }

    public void setSessionId(CrawlingSession sessionId) {
        this.sessionId = sessionId;
    }
}
