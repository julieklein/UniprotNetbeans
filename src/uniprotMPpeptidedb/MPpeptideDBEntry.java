/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uniprotMPpeptidedb;

/**
 *
 * @author julieklein
 */
public class MPpeptideDBEntry {
    ProteinDBEntry protein;
    private String mpdescription;
    private String mpsequence;
    Integer mpP1;
    Integer mpP1prime;

    public Integer getMpP1() {
        return mpP1;
    }

    public void setMpP1(Integer mpP1) {
        this.mpP1 = mpP1;
    }

    public Integer getMpP1prime() {
        return mpP1prime;
    }

    public void setMpP1prime(Integer mpP1prime) {
        this.mpP1prime = mpP1prime;
    }

    public String getMpdescription() {
        return mpdescription;
    }

    public void setMpdescription(String mpdescription) {
        this.mpdescription = mpdescription;
    }

    public String getMpsequence() {
        return mpsequence;
    }

    public void setMpsequence(String mpsequence) {
        this.mpsequence = mpsequence;
    }

    public ProteinDBEntry getProtein() {
        return protein;
    }

    public void setProtein(ProteinDBEntry protein) {
        this.protein = protein;
    }

    @Override
    public String toString() {
        return "MPpeptideDBEntry{" + "protein=" + protein + ", mpdescription=" + mpdescription + ", mpsequence=" + mpsequence + ", mpP1=" + mpP1 + ", mpP1prime=" + mpP1prime + '}';
    }
    
    
    
}
