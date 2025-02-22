/**
 * The MIT License
 *
 * Copyright (c) 2009-2019 PrimeTek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.primefaces.model.timeline;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class TimelineEvent<T> implements Serializable {

    private static final long serialVersionUID = 20130316L;

    /**
     * a unique id for this event.
     */
    private String id;

    /**
     * any custom data object (required to show content of the event)
     */
    private T data;

    /**
     * event's start date (required)
     */
    private Date startDate;

    /**
     * event's end date (optional)
     */
    private Date endDate;

    /**
     * is this event editable? (optional). if null, see the timeline's attribute "editable"
     */
    private Boolean editable;

    /**
     * is this event time editable? (optional). Overrides editable. if null, see the timeline's attribute "editableTime"
     */
    private Boolean editableTime;

    /**
     * is this event group editable? (optional). Overrides editable. if null, see the timeline's attribute "editable"
     */
    private Boolean editableGroup;

    /**
     * is this event removable? (optional). Overrides editable. if null, see the timeline's attribute "editable"
     */
    private Boolean editableRemove;

    /**
     * group this event belongs to (optional). this can be either the group's content or group's position in the list of all groups
     */
    private String group;

    /**
     * A title that is displayed when holding the mouse on the item. The title can be a string containing plain text or HTML (optional).
     */
    private String title;

    /**
     * any custom style class for this event in UI (optional)
     */
    private String styleClass;

    public TimelineEvent() {
        this.id = UUID.randomUUID().toString();
    }

    public TimelineEvent(T data, Date startDate) {
        this(data, startDate, null, null, null, null);
    }

    public TimelineEvent(T data, Date startDate, Boolean editable) {
        this(data, startDate, null, editable, null, null);
    }

    public TimelineEvent(T data, Date startDate, Boolean editable, String group) {
        this(data, startDate, null, editable, group, null);
    }

    public TimelineEvent(T data, Date startDate, Boolean editable, String group, String styleClass) {
        this(data, startDate, null, editable, group, styleClass);
    }

    public TimelineEvent(T data, Date startDate, Date endDate) {
        this(data, startDate, endDate, null, null, null);
    }

    public TimelineEvent(T data, Date startDate, Date endDate, Boolean editable) {
        this(data, startDate, endDate, editable, null, null);
    }

    public TimelineEvent(T data, Date startDate, Date endDate, Boolean editable, String group) {
        this(data, startDate, endDate, editable, group, null);
    }

    public TimelineEvent(T data, Date startDate, Date endDate, Boolean editable, String group, String styleClass) {
        this(UUID.randomUUID().toString(), data, startDate, endDate, editable, group, styleClass);
    }

    public TimelineEvent(String id, T data, Date startDate, Date endDate, Boolean editable, String group, String styleClass) {
        checkStartDate(startDate);
        this.id = id;
        this.data = data;
        this.startDate = startDate;
        this.endDate = endDate;
        this.editable = editable;
        this.editableTime = editable;
        this.editableGroup = editable;
        this.editableRemove = editable;
        this.group = group;
        this.styleClass = styleClass;
    }

    public TimelineEvent(TimelineEvent<T> event) {
        this.id = event.id;
        this.data = event.data;
        this.startDate = (Date) event.startDate.clone();
        this.endDate = event.endDate != null ? (Date) event.endDate.clone() : null;
        this.editable = event.editable;
        this.editableTime = event.editableTime;
        this.editableGroup = event.editableGroup;
        this.editableRemove = event.editableRemove;
        this.group = event.group;
        this.title = event.title;
        this.styleClass = event.styleClass;
    }

    public String getId() {
        return id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        checkStartDate(startDate);
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean isEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
        this.editableTime = editable;
        this.editableGroup = editable;
        this.editableRemove = editable;
    }

    public Boolean isEditableTime() {
        return editableTime;
    }

    public void setEditableTime(Boolean editableTime) {
        this.editableTime = editableTime;
    }

    public Boolean isEditableGroup() {
        return editableGroup;
    }

    public void setEditableGroup(Boolean editableGroup) {
        this.editableGroup = editableGroup;
    }

    public Boolean isEditableRemove() {
        return editableRemove;
    }

    public void setEditableRemove(Boolean editableRemove) {
        this.editableRemove = editableRemove;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static <T> Builder<T> builder() {
        return new Builder<T>();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimelineEvent<?> other = (TimelineEvent<?>) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "TimelineEvent{"
                + "id=" + id
                + ", data=" + data
                + ", startDate=" + startDate
                + ", endDate=" + endDate
                + ", editable=" + editable
                + ", group='" + group + '\''
                + ", styleClass='" + styleClass + '\''
                + '}';
    }

    private void checkStartDate(Date startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Event start date can not be null!");
        }
    }

    public static final class Builder<T> {
        private final TimelineEvent<T> event;

        public Builder() {
            event = new TimelineEvent<>();
        }

        public Builder<T> id(String id) {
            event.id = id;
            return this;
        }

        public Builder<T> data(T data) {
            event.setData(data);
            return this;
        }

        public Builder<T> startDate(Date startDate) {
            event.setStartDate(startDate);
            return this;
        }

        public Builder<T> endDate(Date endDate) {
            event.setEndDate(endDate);
            return this;
        }

        public Builder<T> editable(Boolean editable) {
            event.setEditable(editable);
            return this;
        }

        public Builder<T> group(String group) {
            event.setGroup(group);
            return this;
        }

        public Builder<T> styleClass(String styleClass) {
            event.setStyleClass(styleClass);
            return this;
        }

        public Builder<T> title(String title) {
            event.setTitle(title);
            return this;
        }

        public Builder<T> editableTime(Boolean editableTime) {
            event.setEditableTime(editableTime);
            return this;
        }

        public Builder<T> editableGroup(Boolean editableGroup) {
            event.setEditableGroup(editableGroup);
            return this;
        }

        public Builder<T> editableRemove(Boolean editableRemove) {
            event.setEditableRemove(editableRemove);
            return this;
        }

        public TimelineEvent<T> build() {
            return event;
        }
    }
}
