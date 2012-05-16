/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uniprotproteasedb;

/**
 *
 * @author julieklein
 */
public class CleavageSiteDBEntry {
    ProteaseDBEntry protease;  
    SubstrateDBEntry substrate;
    private int P1Number;
    private int P1primeNumber;
    private char P1Name;
    private char P1primeName;

    public char getP1Name() {
        return P1Name;
    }

    public void setP1Name(char P1Name) {
        this.P1Name = P1Name;
    }

    public int getP1Number() {
        return P1Number;
    }

    public void setP1Number(int P1Number) {
        this.P1Number = P1Number;
    }

    public char getP1primeName() {
        return P1primeName;
    }

    public void setP1primeName(char P1primeName) {
        this.P1primeName = P1primeName;
    }

    public int getP1primeNumber() {
        return P1primeNumber;
    }

    public void setP1primeNumber(int P1primeNumber) {
        this.P1primeNumber = P1primeNumber;
    }

    public ProteaseDBEntry getProtease() {
        return protease;
    }

    public void setProtease(ProteaseDBEntry protease) {
        this.protease = protease;
    }

    public SubstrateDBEntry getSubstrate() {
        return substrate;
    }

    public void setSubstrate(SubstrateDBEntry substrate) {
        this.substrate = substrate;
    }
    
    
            
    
}
