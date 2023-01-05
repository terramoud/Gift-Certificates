package com.epam.esm.domain.entity;

import java.io.Serializable;

/**
 * Basic entity for every model's entity
 *
 * @author Oleksadr Koreshev
 * @since 1.0
 */
public abstract class Entity implements Serializable {
    private static final long serialVersionUID = -1491456600908233143L;

    protected Long id;

    protected String name;

    /**
     * Returns the unique identifier or {@code 0L}
     *
     * @return the unique identifier or {@code 0L}
     */
    public Long getId() {
        return id;
    }

    /**
     * Replaces the current identifier of entity by specified identifier.
     *
     * @param id the unique identifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the unique name
     *
     * @return the unique name
     */
    public String getName() {
        return name;
    }

    /**
     * Replaces the current name of entity by specified name.
     *
     * @param name the unique name
     */
    public void setName(String name) {
        this.name = name;
    }
}
