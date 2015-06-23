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
@Table(name = "zip_action", catalog = "phenodcc_tracker", schema = "",
    uniqueConstraints = {
    @UniqueConstraint(columnNames = {"zip_id", "todo_id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZipAction.findAll", query = "SELECT z FROM ZipAction z"),
    @NamedQuery(name = "ZipAction.findById", query = "SELECT z FROM ZipAction z WHERE z.id = :id"),
    @NamedQuery(name = "ZipAction.findByTodo", query = "SELECT z FROM ZipAction z WHERE z.todoId = :todoId"),
    @NamedQuery(name = "ZipAction.findByLastUpdate", query = "SELECT z FROM ZipAction z WHERE z.lastUpdate = :lastUpdate"),
    @NamedQuery(name = "ZipAction.findByIdAction", query = "SELECT z FROM ZipAction z WHERE (z.id = :id AND z.todoId = :todoId)"),
    @NamedQuery(name = "ZipAction.findByZipAction", query = "SELECT z FROM ZipAction z WHERE (z.zipId = :zipId AND z.todoId = :todoId)"),
    @NamedQuery(name = "ZipAction.findByPhaseStatus", query = "SELECT z FROM ZipAction z WHERE (z.phaseId = :phaseId AND z.statusId = :statusId)"),
    @NamedQuery(name = "ZipAction.findByPhaseStatusAscCreated", query = "SELECT z FROM ZipAction z JOIN z.zipId zf WHERE (z.phaseId = :phaseId AND z.statusId = :statusId) ORDER BY zf.created ASC"),
    @NamedQuery(name = "ZipAction.countPhaseStatus", query = "SELECT COUNT(z) FROM ZipAction z WHERE (z.phaseId = :phaseId AND z.statusId = :statusId)")})
public class ZipAction implements Serializable {

    // type of action
    public static final String ADD_ACTION = "add";
    public static final String EDIT_ACTION = "edit";
    public static final String DELETE_ACTION = "delete";

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
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
    @JoinColumn(name = "todo_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ProcessingType todoId;
    @JoinColumn(name = "zip_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ZipFile zipId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zaId")
    private Collection<FileSourceHasZip> fileSourceHasZipCollection;

    public ZipAction() {
    }

    public ZipAction(
            ZipFile zipId,
            ProcessingType todoId,
            Phase phaseId,
            AStatus statusId) {
        this.zipId = zipId;
        this.todoId = todoId;
        this.phaseId = phaseId;
        this.statusId = statusId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ProcessingType getTodoId() {
        return todoId;
    }

    public void setTodoId(ProcessingType todoId) {
        this.todoId = todoId;
    }

    public ZipFile getZipId() {
        return zipId;
    }

    public void setZipId(ZipFile zipId) {
        this.zipId = zipId;
    }

    @XmlTransient
    public Collection<FileSourceHasZip> getFileSourceHasZipCollection() {
        return fileSourceHasZipCollection;
    }

    public void setFileSourceHasZipCollection(Collection<FileSourceHasZip> fileSourceHasZipCollection) {
        this.fileSourceHasZipCollection = fileSourceHasZipCollection;
    }
}
