/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uniprotMPpeptidedb;

import java.util.LinkedList;

/**
 *
 * @author julieklein
 */
public class ProteinDBEntry {
    LinkedList<String> proteinacessionlist;
    String proteinname;
    String proteinsequence;
    String proteintaxonomy;
    String proteinurl;

    @Override
    public String toString() {
        return "ProteinDBEntry{" + "proteinacessionlist=" + proteinacessionlist + ", proteinname=" + proteinname + ", proteinsequence=" + proteinsequence + ", proteintaxonomy=" + proteintaxonomy + ", proteinurl=" + proteinurl + '}';
    }
    
    

    public LinkedList<String> getProteinacessionlist() {
        return proteinacessionlist;
    }

    public void setProteinacessionlist(LinkedList<String> proteinacessionlist) {
        this.proteinacessionlist = proteinacessionlist;
    }

    public String getProteinname() {
        return proteinname;
    }

    public void setProteinname(String proteinname) {
        this.proteinname = proteinname;
    }

    public String getProteinsequence() {
        return proteinsequence;
    }

    public void setProteinsequence(String proteinsequence) {
        this.proteinsequence = proteinsequence;
    }

    public String getProteintaxonomy() {
        return proteintaxonomy;
    }

    public void setProteintaxonomy(String proteintaxonomy) {
        this.proteintaxonomy = proteintaxonomy;
    }

    public String getProteinurl() {
        return proteinurl;
    }

    public void setProteinurl(String proteinurl) {
        this.proteinurl = proteinurl;
    }

   
    
    
}
