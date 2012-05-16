/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uniprotproteasedb;

import java.util.LinkedList;

/**
 *
 * @author julieklein
 */
public class SubstrateDBEntry {
    String substrateacessionlist;
    String substratename;
    String substratesequence;
    String substratetaxonomy;
    String substrateurl;
    String substrateprotname;

    public String getSubstrateprotname() {
        return substrateprotname;
    }

    public void setSubstrateprotname(String substrateprotname) {
        this.substrateprotname = substrateprotname;
    }

    @Override
    public String toString() {
        return "SubstrateDBEntry{" + "substrateacessionlist=" + substrateacessionlist + ", substratename=" + substratename + ", substrateprotname=" + substrateprotname +", substratesequence=" + substratesequence + ", substratetaxonomy=" + substratetaxonomy + ", substrateurl=" + substrateurl + '}';
    }
    

  

    
    

    public String getSubstrateacessionlist() {
        return substrateacessionlist;
    }

    public void setSubstrateacessionlist(String substrateacessionlist) {
        this.substrateacessionlist = substrateacessionlist;
    }

    public String getSubstratename() {
        return substratename;
    }

    public void setSubstratename(String substratename) {
        this.substratename = substratename;
    }

    public String getSubstratesequence() {
        return substratesequence;
    }

    public void setSubstratesequence(String substratesequence) {
        this.substratesequence = substratesequence;
    }

    public String getSubstratetaxonomy() {
        return substratetaxonomy;
    }

    public void setSubstratetaxonomy(String substratetaxonomy) {
        this.substratetaxonomy = substratetaxonomy;
    }

    public String getSubstrateurl() {
        return substrateurl;
    }

    public void setSubstrateurl(String substrateurl) {
        this.substrateurl = substrateurl;
    }
   
    
    
}
