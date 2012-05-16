/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uniprotMPpeptidedb;

import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uniprotproteasedb.*;

/**
 *
 * @author julieklein
 */
public class GetUniprotPeptideDB {

    public GetUniprotPeptideDB() {
        PrintStream csvWriter = null;
        try {
            //CREATE THE BIG RESULT TABLE
            LinkedList<MPpeptideDBEntry> alldata = new LinkedList<MPpeptideDBEntry>();
            //RETRIEVE XML
            //String UniprotURL = "file:///Users/julieklein/Desktop/ProteasiX/UniprotXML/xml2.xml";
            //String UniprotURL = "file:///Users/julieklein/Desktop/ProteasiX/UniprotXML/issuewithsequence.xml";
            //String UniprotURL = "file:///Users/julieklein/Desktop/ProteasiX/UniprotXML/issuewithtransit.xml";
           //String UniprotURL = "http://www.uniprot.org/uniprot/?query=col1A1+AND+reviewed%3Ayes&format=xml";
            String UniprotURL = "http://www.uniprot.org/uniprot/P42785.xml";
            //String UniprotURL = "http://www.uniprot.org/uniprot/P02452.xml";
            ParseUniprotPep parser = new ParseUniprotPep();
            Document xml = parser.getXML(UniprotURL);
            xml.getXmlVersion();
            System.out.println(xml.getXmlVersion());
            System.out.println(xml.toString());
            String xmlstring = parser.getXMLasstring(UniprotURL);
            //System.out.println(xmlstring);
            //RETRIEVE ENTRIES THAT ARE CHAIN OR PROPEPTIDE INSIDE XML
            XPathUniprotMP XPather = new XPathUniprotMP();
            String xpathQuery = "/uniprot/entry[./feature[@type='propeptide']|./feature[@type='chain']|./feature[@type='transit peptide']|./feature[@type='peptide']|./feature[@type='signal peptide']]";
            //GET ENTRIES THAT ARE CHAIN OR PROPEPTIDE NODELIST
            NodeList getNodeListbyXPath = XPather.getNodeListByXPath(xpathQuery, xml);
            //System.out.println("Size of NodeList: "+getNodeListbyXPath.getLength());
            //RETRIEVE GENE NAMES IN SELECTED ENTRIES
            XPathNodeUniprot XPathNoder = new XPathNodeUniprot();
            String xpathQueryNode = "./gene/name[@type][1]/text()";
            //RETRIEVE LIST OF ACCESSION IN SELECTED ENTRIES        
            XPathNodeUniprot XPathNoder1 = new XPathNodeUniprot();
            String xpathQueryNode1 = "./accession/text()";
            //RETRIEVE TAXONOMY  
            XPathNodeUniprot XPathNoder5 = new XPathNodeUniprot();
            String xpathQueryNode5 = "./organism/dbReference/@id";
            //RETRIEVE FIRST ACCESSION TO CREATE URL
            XPathNodeUniprot XPathNoder6 = new XPathNodeUniprot();
            String xpathQueryNode6 = "./accession[1]/text()";
            //RETRIEVE FULL AA SEQUENCE
            XPathNodeUniprot XPathNoder7 = new XPathNodeUniprot();
            String xpathQueryNode7 = "./sequence/text()";
            //INTERLUDE: RETRIEVE MP PEPTIDES FEATURES IN SELECTED ENTRIES 
            XPathNodeUniprot XPathNoder2 = new XPathNodeUniprot();
            String xpathQueryNode2 = "./feature[@type='propeptide']|./feature[@type='chain']|./feature[@type='transit peptide']|./feature[@type='peptide']|./feature[@type='signal peptide']";
            //RETRIEVE ENZYME DESCRIPTION IN SELECTED ENZYME FEATURES
            XPathNodeUniprot XPathNoder20 = new XPathNodeUniprot();
            String xpathQueryNode20 = "./@type";
            XPathNodeUniprot XPathNoder21 = new XPathNodeUniprot();
            String xpathQueryNode21 = "./@description";
            //RETRIEVE P1 AA NUMBER IN SELECTED ENZYME FEATURES
            XPathNodeUniprot XPathNoder30 = new XPathNodeUniprot();
            String xpathQueryNode30 = "./location/begin/@status|./location/begin/@position";
            XPathNodeUniprot XPathNoder3 = new XPathNodeUniprot();
            String xpathQueryNode3 = "./location/begin/@position";
            //RETRIEVE P1PRIME AA NUMBER IN SELECTED ENZYME FEATURES
            XPathNodeUniprot XPathNoder40 = new XPathNodeUniprot();
            String xpathQueryNode40 = "./location/end/@status|./location/end/@position";
            XPathNodeUniprot XPathNoder4 = new XPathNodeUniprot();
            String xpathQueryNode4 = "./location/end/@position";
            Loop l1 = new Loop();
            //FOR EACH SELECTED ENTRIES:
            for (int i = 0; i < getNodeListbyXPath.getLength(); i++) {


                //CREATE A PROTEIN ENTRY THAT WILL COLLECT ALL RELEVANT INFORMATION
                ProteinDBEntry pdb = new ProteinDBEntry();


                //1 INFO PDB: GET GENE NAME
                NodeList getNodeListByXPathNoder = XPathNoder.getNodeListByXPath(xpathQueryNode, getNodeListbyXPath.item(i));
                //2 INFO PDB: GET LIST OF ACCESSION
                NodeList getNodeListByXPathNoder1 = XPathNoder1.getNodeListByXPath(xpathQueryNode1, getNodeListbyXPath.item(i));
                //3 INFO PDB: GET TAXONOMY
                NodeList getNodeListByXPathNoder5 = XPathNoder5.getNodeListByXPath(xpathQueryNode5, getNodeListbyXPath.item(i));
                //4 INFO PDB: GET URL
                NodeList getNodeListByXPathNoder6 = XPathNoder6.getNodeListByXPath(xpathQueryNode6, getNodeListbyXPath.item(i));
                //5 INFO PDB: GET FULL SEQUENCE
                NodeList getNodeListbyXPathNoder7 = XPathNoder7.getNodeListByXPath(xpathQueryNode7, getNodeListbyXPath.item(i));


                //INTERLUDE: GET ENZYME FEATURE
                NodeList getNodeListByXPathNoder2 = XPathNoder2.getNodeListByXPath(xpathQueryNode2, getNodeListbyXPath.item(i));


                //1 INFO PDB: PRINT AND SAVE GENE NAME    
                LinkedList<String> stringfromNodelist = l1.getStringfromNodelist(getNodeListByXPathNoder);
                String string = stringfromNodelist.getFirst();
                System.out.println(string);
                pdb.setProteinname(string);

                //2 INFO PDB: PRINT AND SAVE LIST OF ACCESSION
                LinkedList<String> stringfromNodelist1 = l1.getStringfromNodelist(getNodeListByXPathNoder1);
                System.out.println(stringfromNodelist1);
                pdb.setProteinacessionlist(stringfromNodelist1);

                //3 INFO PDB: PRINT AND SAVE TAXONOMY
                LinkedList<String> stringfromNodelist5 = l1.getStringfromNodelist(getNodeListByXPathNoder5);
                String string5 = stringfromNodelist5.getFirst();
                System.out.println(string5);
                pdb.setProteintaxonomy(string5);

                //4 INFO PDB: PRINT AND SAVE URL
                LinkedList<String> stringfromNodelist6 = l1.getStringfromNodelist(getNodeListByXPathNoder6);
                String string6 = stringfromNodelist6.getFirst();
                System.out.println("http://www.uniprot.org/uniprot/" + string6);
                pdb.setProteinurl("http://www.uniprot.org/uniprot/" + string6);

                //5 INFO PDB: PRINT AND SAVE FULL SEQUENCE
                LinkedList<String> stringfromNodelist7 = l1.getStringfromNodelist(getNodeListbyXPathNoder7);
                String string7 = stringfromNodelist7.getFirst();
                string7 = string7.replaceAll("\n", "");
                pdb.setProteinsequence(string7);
                //int length = stringx.length();
                //System.out.println(stringx);
                //System.out.println(length);

                //INTERLUDE: FOR EACH MP FEATURE OF SELECTED ENTRIES
                for (int j = 0; j < getNodeListByXPathNoder2.getLength(); j++) {

                    //CREATE A MP PEPTIDE ENTRY THAT WILL COLLECT ALL RELEVANT INFORMATION

                    MPpeptideDBEntry mpdb = new MPpeptideDBEntry();

                    Node n = getNodeListByXPathNoder2.item(j);


                    //1 INFO MPDB: GET MP PEPTIDE DESCRIPTION
                    NodeList getNodeListByXPathNoder20 = XPathNoder20.getNodeListByXPath(xpathQueryNode20, n);
                    //1' INFO MPDB: GET MP PEPTIDE DESCRIPTION
                    NodeList getNodeListByXPathNoder21 = XPathNoder21.getNodeListByXPath(xpathQueryNode21, n);
                    //2 INFO MPDB: GET P1 AA
                    NodeList getNodeListbyXPathNoder30 = XPathNoder30.getNodeListByXPath(xpathQueryNode30, n);
                    //2' INFO MPDB: GET P1 AA
                    NodeList getNodeListbyXPathNoder3 = XPathNoder3.getNodeListByXPath(xpathQueryNode3, n);
                    //3 INFO MPDB: GET P1PRIME AA
                    NodeList getNodeListbyXPathNoder40 = XPathNoder40.getNodeListByXPath(xpathQueryNode40, n);
                    //3' INFO MPDB: GET P1PRIME AA
                    NodeList getNodeListbyXPathNoder4 = XPathNoder4.getNodeListByXPath(xpathQueryNode4, n);


                    //1 INFO MPDB: PRINT AND SAVE DESCRIPTION     
                    LinkedList<String> stringfromNodelist20 = l1.getStringfromNodelist(getNodeListByXPathNoder20);
                    String string20 = stringfromNodelist20.getFirst();
                    if (string20.equalsIgnoreCase("signal peptide")) {
                        String stringx = string20.substring(0, 1).toUpperCase() + string20.substring(1);
                        mpdb.setMpdescription(stringx);
                        mpdb.setProtein(pdb);
                        System.out.println(stringx);
                    } else {
                        if (string20.equalsIgnoreCase("transit peptide")) {
                            LinkedList<String> stringfromNodelist21 = l1.getStringfromNodelist(getNodeListByXPathNoder21);
                            String string21 = stringfromNodelist21.getFirst();
                            String stringy = string21 + " transit peptide";
                            mpdb.setMpdescription(stringy);
                            mpdb.setProtein(pdb);
                            System.out.println(stringy);
                        } else {
                            LinkedList<String> stringfromNodelist21 = l1.getStringfromNodelist(getNodeListByXPathNoder21);
                            if (stringfromNodelist21.isEmpty()) {
                                String stringz = string20.substring(0, 1).toUpperCase() + string20.substring(1);
                                mpdb.setMpdescription(stringz);
                                mpdb.setProtein(pdb);
                                System.out.println(stringz);
                            } else {
                                String string21 = stringfromNodelist21.getFirst();
                                mpdb.setMpdescription(string21);
                                mpdb.setProtein(pdb);
                                System.out.println(string21);
                            }
                        }
                    }


                    //2 INFO MPDB: PRINT AND SAVE P1 AA INTEGER
                    LinkedList<String> stringfromNodelist30 = l1.getStringfromNodelist(getNodeListbyXPathNoder30);
                    String P1 = null;
                    String string3 = stringfromNodelist30.getFirst();


                    if (string3.equalsIgnoreCase("unknown")) {
                        continue;

                    } else {

                        //3 INFO MPDB: PRINT AND SAVE P1PRIME AA INTEGER      
                        LinkedList<String> stringfromNodelist40 = l1.getStringfromNodelist(getNodeListbyXPathNoder40);
                        String P1prime = null;
                        String string4 = stringfromNodelist40.getFirst();

                        if (string4.equalsIgnoreCase("unknown")) {
                            continue;
                        } else {
                            LinkedList<String> stringfromNodelist3 = l1.getStringfromNodelist(getNodeListbyXPathNoder3);
                            String stringx = stringfromNodelist3.getFirst();
                            P1 = stringx;
                            System.out.println(stringx);
                            int intP1 = Integer.parseInt(P1);
                            mpdb.setMpP1(intP1);

                            LinkedList<String> stringfromNodelist4 = l1.getStringfromNodelist(getNodeListbyXPathNoder4);
                            String stringy = stringfromNodelist4.getFirst();
                            P1prime = stringy;
                            System.out.println(stringy);
                            int intP1prime = Integer.parseInt(P1prime);
                            mpdb.setMpP1prime(intP1prime);



                            //4 INFO MPDB: PRINT AND SAVE MP PEPTIDE SEQUENCE
                            String subst = string7.substring(intP1 - 1, intP1prime);
                            System.out.println(subst);
                            mpdb.setMpsequence(subst);

                            alldata.add(mpdb);
                        }
                    }


                    //ADD ALL DATA TO BIG FILE


                }

            }
            System.out.println("-----------------");
            csvWriter = new PrintStream("MPDB.csv");
            csvWriter.print("Protein Name");
            csvWriter.print(",");
            //            csvWriter.print("Substrate list of Accession");
            //            csvWriter.print(",");
            csvWriter.print("Protein Sequence");
            csvWriter.print(",");
            csvWriter.print("Protein taxonomy");
            csvWriter.print(",");
            csvWriter.print("MP Peptide description");
            csvWriter.print(",");
            csvWriter.print("MP Peptide start");
            csvWriter.print(",");
            csvWriter.print("MP Peptide end");
            csvWriter.print(",");
            csvWriter.print("MP Peptide sequence");
            csvWriter.print(",");
            csvWriter.print("url");
            csvWriter.print("\n");
            for (MPpeptideDBEntry MPpeptideDBEntry : alldata) {
                System.out.println(MPpeptideDBEntry);
                csvWriter.print(MPpeptideDBEntry.protein.getProteinname());
                csvWriter.print(",");
                //            csvWriter.print(MPpeptideDBEntry.protein.getProteinacessionlist());
                //            csvWriter.print(",");
                csvWriter.print(MPpeptideDBEntry.protein.getProteinsequence());
                csvWriter.print(",");
                csvWriter.print(MPpeptideDBEntry.protein.getProteintaxonomy());
                csvWriter.print(",");
                csvWriter.print(MPpeptideDBEntry.getMpdescription());
                csvWriter.print(",");
                csvWriter.print(MPpeptideDBEntry.getMpP1());
                csvWriter.print(",");
                csvWriter.print(MPpeptideDBEntry.getMpP1prime());
                csvWriter.print(",");
                csvWriter.print(MPpeptideDBEntry.getMpsequence());
                csvWriter.print(",");
                csvWriter.print(MPpeptideDBEntry.protein.getProteinurl());
                csvWriter.print("\n");




            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetUniprotPeptideDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            csvWriter.close();
        }
    }

    /**
     * @param args the command line arguments
     *
     */
    public static void main(String[] args) {
        GetUniprotPeptideDB JulieUniprot = new GetUniprotPeptideDB();
        // TODO code application logic here
    }
}