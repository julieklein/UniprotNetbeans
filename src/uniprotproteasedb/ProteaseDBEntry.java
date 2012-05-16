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
public class ProteaseDBEntry {
    private String enzymename;
    private String enzymeaccession;
    private String enzymebrenda;
    private String enzymesymbol;

   
    
    public String getEnzymeaccession() {
        return enzymeaccession;
    }

    public void setEnzymeaccession(String enzymeaccession) {
        this.enzymeaccession = enzymeaccession;
    }

    public String getEnzymebrenda() {
        return enzymebrenda;
    }

    public void setEnzymebrenda(String enzymebrenda) {
        this.enzymebrenda = enzymebrenda;
    }

    public String getEnzymesymbol() {
        return enzymesymbol;
    }

    public void setEnzymesymbol(String enzymesymbol) {
        this.enzymesymbol = enzymesymbol;
    }
    

    public String getEnzymename() {
        return enzymename;
    }

    public void setEnzymename(String enzymename) {
        this.enzymename = enzymename;
    }

    @Override
    public String toString() {
        return "ProteaseDBEntry{" + "enzymename=" + enzymename + '}';
    }

    
    
    
    
    
}
