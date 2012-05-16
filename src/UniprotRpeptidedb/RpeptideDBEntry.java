/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UniprotRpeptidedb;

import uniprotMPpeptidedb.*;

/**
 *
 * @author julieklein
 */
public class RpeptideDBEntry {
    ProteinDBEntryR protein;
    private String rdescription;
    private String rsequence;
    Integer rP1;
    Integer rP1prime;

    public Integer getrP1() {
        return rP1;
    }

    public void setrP1(Integer rP1) {
        this.rP1 = rP1;
    }

    public Integer getrP1prime() {
        return rP1prime;
    }

    public void setrP1prime(Integer rP1prime) {
        this.rP1prime = rP1prime;
    }

    public String getrdescription() {
        return rdescription;
    }

    public void setrdescription(String rdescription) {
        this.rdescription = rdescription;
    }

    public String getrsequence() {
        return rsequence;
    }

    public void setrsequence(String rsequence) {
        this.rsequence = rsequence;
    }

    public ProteinDBEntryR getProtein() {
        return protein;
    }

    public void setProtein(ProteinDBEntryR protein) {
        this.protein = protein;
    }

    @Override
    public String toString() {
        return "RpeptideDBEntry{" + "protein=" + protein + ", Rdescription=" + rdescription + ", Rsequence=" + rsequence + ", RP1=" + rP1 + ", RP1prime=" + rP1prime + '}';
    }
    
    
    
}
