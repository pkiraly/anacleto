/*
 * Created on Jan 30, 2005
 *
 */
package com.anacleto.view;

/**
 * @author robi
 * 
 */
public class IndexFieldView {

    private String name;

    private String value;

    private boolean indexed;

    private boolean stored;

    private boolean tokenized;

    private boolean termVectorStored;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the indexed.
     */
    public boolean isIndexed() {
        return indexed;
    }

    /**
     * @param indexed
     *            The indexed to set.
     */
    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    /**
     * @return Returns the stored.
     */
    public boolean isStored() {
        return stored;
    }

    /**
     * @param stored
     *            The stored to set.
     */
    public void setStored(boolean stored) {
        this.stored = stored;
    }

    /**
     * @return Returns the termVectorStored.
     */
    public boolean isTermVectorStored() {
        return termVectorStored;
    }

    /**
     * @param termVectorStored
     *            The termVectorStored to set.
     */
    public void setTermVectorStored(boolean termVectorStored) {
        this.termVectorStored = termVectorStored;
    }

    /**
     * @return Returns the tokenized.
     */
    public boolean isTokenized() {
        return tokenized;
    }

    /**
     * @param tokenized
     *            The tokenized to set.
     */
    public void setTokenized(boolean tokenized) {
        this.tokenized = tokenized;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}