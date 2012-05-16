package GetPMAP;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.LinkedList;

/**
 *
 * @author julieklein
 */
public class ProteaseDBEntryPMAP {

  private String enzymename;
  private String enzymetaxonomy;
  private String enzymesymbol;
  private String enzymeUniprot;
  private String enzymeBrenda;

    public String getEnzymeBrenda() {
        return enzymeBrenda;
    }

    public void setEnzymeBrenda(String enzymeBrenda) {
        this.enzymeBrenda = enzymeBrenda;
    }

    public String getEnzymeUniprot() {
        return enzymeUniprot;
    }

    public void setEnzymeUniprot(String enzymeUniprot) {
        this.enzymeUniprot = enzymeUniprot;
    }

    public String getEnzymesymbol() {
        return enzymesymbol;
    }

    public void setEnzymesymbol(String enzymesymbol) {
        this.enzymesymbol = enzymesymbol;
    }

    public String getEnzymetaxonomy() {
        return enzymetaxonomy;
    }

    public void setEnzymetaxonomy(String enzymetaxonomy) {
        this.enzymetaxonomy = enzymetaxonomy;
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
