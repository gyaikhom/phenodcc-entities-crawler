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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gagarine Yaikhom <g.yaikhom@har.mrc.ac.uk>
 */
@Entity
@Table(name = "zip_log", catalog = "phenodcc_tracker", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZipLog.findAll", query = "SELECT z FROM ZipLog z"),
    @NamedQuery(name = "ZipLog.findById", query = "SELECT z FROM ZipLog z WHERE z.id = :id"),
    @NamedQuery(name = "ZipLog.findByLastUpdate", query = "SELECT z FROM ZipLog z WHERE z.lastUpdate = :lastUpdate")})
public class ZipLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Lob
    @Column(length = 65535)
    private String message;
    @Basic(optional = false)
    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @JoinColumn(name = "exception_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private AnException exceptionId;
    @JoinColumn(name = "zip_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ZipDownload zipId;

    public ZipLog() {
    }

    public ZipLog(ZipDownload zipId, AnException exceptionId, String message) {
        this.message = message;
        this.exceptionId = exceptionId;
        this.zipId = zipId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public AnException getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(AnException exceptionId) {
        this.exceptionId = exceptionId;
    }

    public ZipDownload getZipId() {
        return zipId;
    }

    public void setZipId(ZipDownload zipId) {
        this.zipId = zipId;
    }
}
