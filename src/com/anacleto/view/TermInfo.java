package com.anacleto.view;

import org.apache.lucene.index.Term;

/**
 * @author robi
 * 
 */
public final class TermInfo {
    private Term term;

    private int docFreq;

    public TermInfo(Term t, int df) {
        term = t;
        docFreq = df;
    }

    /**
     * @return Returns the field.
     */
    public String getField() {
        return term.field();
    }

    /**
     * @return Returns the docFreq.
     */
    public int getDocFreq() {
        return docFreq;
    }

    /**
     * @param docFreq
     *            The docFreq to set.
     */
    public void setDocFreq(int docFreq) {
        this.docFreq = docFreq;
    }

    /**
     * @return Returns the term.
     */
    public Term getTerm() {
        return term;
    }

    /**
     * @param term
     *            The term to set.
     */
    public void setTerm(Term term) {
        this.term = term;
    }

    /**
     * @return Returns the text.
     */
    public String getText() {
        return term.text();
    }
}