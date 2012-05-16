package GetPMAP;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.LinkedList;/**
 *
 * @author julieklein
 */
public class CleavageSiteDBEntryPMAP {
    ProteaseDBEntryPMAP protease;
    String csurl;
    String cspmid;
    String cleavagesiteseaquence;

    public String getCleavagesiteseaquence() {
        return cleavagesiteseaquence;
    }

    public void setCleavagesiteseaquence(String cleavagesiteseaquence) {
        this.cleavagesiteseaquence = cleavagesiteseaquence;
    }
    @Override
    public String toString() {
        return "CleavageSiteDBEntryPMAP{" + "protease=" + protease + ", csurl=" + csurl + ", cspmid=" + cspmid + ", cserrormsg=" + cserrormsg + ", cserrorsequence=" + cscuration + ", substrate=" + substrate + ", P1=" + P1 + ", P1prime=" + P1prime + ", aaP1=" + aaP1 + ", aaP1prime=" + aaP1prime + '}';
    }

  
    String cserrormsg;
    String cscuration;

    public String getCserrormsg() {
        return cserrormsg;
    }

    public void setCserrormsg(String cserrormsg) {
        this.cserrormsg = cserrormsg;
    }

    public String getCscuration() {
        return cscuration;
    }

    public void setCscuration(String cscuration) {
        this.cscuration = cscuration;
    }

    public String getCspmid() {
        return cspmid;
    }

    public void setCspmid(String cspmid) {
        this.cspmid = cspmid;
    }

    public String getCsurl() {
        return csurl;
    }

    public void setCsurl(String csurl) {
        this.csurl = csurl;
    }
//    @Override
//    public String toString() {
//        return "CleavageSiteDBEntry{" + "protease=" + protease + ", substrate=" + substrate + ", P1=" + P1 + ", P1prime=" + P1prime + ", aaP1=" + aaP1 + ", aaP1prime=" + aaP1prime + '}';
//    }
    SubstrateDBEntryPMAP substrate;

   
    Integer P1;
    Integer P1prime;
    char aaP1;
    char aaP1prime;

    public Integer getP1() {
        return P1;
    }

    public void setP1(Integer P1) {
        this.P1 = P1;
    }

    public Integer getP1prime() {
        return P1prime;
    }

    public void setP1prime(Integer P1prime) {
        this.P1prime = P1prime;
    }

    public char getAaP1() {
        return aaP1;
    }

    public void setAaP1(char aaP1) {
        this.aaP1 = aaP1;
    }

    public char getAaP1prime() {
        return aaP1prime;
    }

    public void setAaP1prime(char aaP1prime) {
        this.aaP1prime = aaP1prime;
    }

    public ProteaseDBEntryPMAP getProtease() {
        return protease;
    }

    public void setProtease(ProteaseDBEntryPMAP protease) {
        this.protease = protease;
    }

    public SubstrateDBEntryPMAP getSubstrate() {
        return substrate;
    }

    public void setSubstrate(SubstrateDBEntryPMAP substrate) {
        this.substrate = substrate;
    }
    
    
            
    
}
