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
@Table(name = "xml_log", catalog = "phenodcc_tracker", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XmlLog.findAll", query = "SELECT x FROM XmlLog x"),
    @NamedQuery(name = "XmlLog.findById", query = "SELECT x FROM XmlLog x WHERE x.id = :id"),
    @NamedQuery(name = "XmlLog.findByLine", query = "SELECT x FROM XmlLog x WHERE x.line = :line"),
    @NamedQuery(name = "XmlLog.findByCol", query = "SELECT x FROM XmlLog x WHERE x.col = :col"),
    @NamedQuery(name = "XmlLog.findByLastUpdate", query = "SELECT x FROM XmlLog x WHERE x.lastUpdate = :lastUpdate")})
public class XmlLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Lob
    @Column(length = 65535)
    private String message;
    private Integer line;
    private Integer col;
    @Basic(optional = false)
    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @JoinColumn(name = "exception_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private AnException exceptionId;
    @JoinColumn(name = "xml_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private XmlFile xmlId;

    public XmlLog() {
    }

    public XmlLog(
            XmlFile xmlId,
            AnException exceptionId,
            String message) {
        this.xmlId = xmlId;
        this.exceptionId = exceptionId;
        this.message = message;
        this.line = 0;
        this.col = 0;
    }

    public XmlLog(
            XmlFile xmlId,
            AnException exceptionId,
            String message,
            Integer line) {
        this.xmlId = xmlId;
        this.exceptionId = exceptionId;
        this.message = message;
        this.line = line;
        this.col = 0;
    }

    public XmlLog(
            XmlFile xmlId,
            AnException exceptionId,
            String message,
            Integer line,
            Integer col) {
        this.xmlId = xmlId;
        this.exceptionId = exceptionId;
        this.message = message;
        this.line = line;
        this.col = col;
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

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
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

    public XmlFile getXmlId() {
        return xmlId;
    }

    public void setXmlId(XmlFile xmlId) {
        this.xmlId = xmlId;
    }
}
