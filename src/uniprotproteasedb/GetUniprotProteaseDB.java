/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uniprotproteasedb;

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author julieklein
 */
public class GetUniprotProteaseDB {

    public GetUniprotProteaseDB() {
        PrintStream csvWriter = null;

        //CREATE THE BIG RESULT TABLE
        LinkedList<CleavageSiteDBEntry> alldata = new LinkedList<CleavageSiteDBEntry>();
        //RETRIEVE XML
        //String UniprotURL = "file:///Users/julieklein/Desktop/ProteasiX/UniprotXML/xml2.xml";
        //String UniprotURL = "http://www.uniprot.org/uniprot/?query=col1A1+AND+reviewed%3Ayes&format=xml";
        //String UniprotURL = "http://www.uniprot.org/uniprot/P02452.xml";
        String UniprotIDListURL = "http://www.uniprot.org/uniprot/?query=%22cleavage%3B%22+organism%3A9606+reviewed%3Ayes&format=list";
        int linecounter = 0;
        try {
            BufferedReader list = new BufferedReader(new InputStreamReader(new URL(UniprotIDListURL).openStream()));

            String line = null;
            while ((line = list.readLine()) != null) {
                linecounter++;
            }
            System.out.println(linecounter);

        } catch (IOException ex) {
            Logger.getLogger(GetUniprotProteaseDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        String UniprotURLdebut = "http://www.uniprot.org/uniprot/?query=%22cleavage%3B%22+organism%3A9606+reviewed%3Ayes&format=xml&limit=100&offset=";
        String UniprotURL = null;
        int offset = 0;
        while (offset < linecounter) {
            UniprotURL = UniprotURLdebut + offset;
            System.out.println(UniprotURL);



            ParseUniprotProt parser = new ParseUniprotProt();
            Document xml = parser.getXML(UniprotURL);
            xml.getXmlVersion();
            //System.out.println(xml.getXmlVersion());
            //System.out.println(xml.toString());
            String xmlstring = parser.getXMLasstring(UniprotURL);
            //System.out.println(xmlstring);
            //RETRIEVE ENTRIES THAT ARE "cleavage; by" INSIDE XML
            XPathUniprot XPather = new XPathUniprot();
            String xpathQuery = "/uniprot/entry[./feature[@type='site'][contains(@description, 'Cleavage')]]";
            //GET ENTRIES THAT ARE "cleavage; by" NODELIST
            NodeList getNodeListByXPath = XPather.getNodeListByXPath(xpathQuery, xml);
            //System.out.println("Size of NodeList: "+getNodeListByXPath.getLength());
            //RETRIEVE GENE NAMES IN SELECTED ENTRIES
            XPathNodeUniprot XPathNoder = new XPathNodeUniprot();
            String xpathQueryNode = "./gene/name[@type][1]/text()";
            //RETRIEVE PROTEIN NAMES IN SELECTED ENTRIES
            XPathNodeUniprot XPathNoder0 = new XPathNodeUniprot();
            String xpathQueryNode0 = "./protein/recommendedName/fullName/text()";
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
            //INTERLUDE: RETRIEVE ENZYME FEATURES IN SELECTED ENTRIES 
            XPathNodeUniprot XPathNoder2 = new XPathNodeUniprot();
            String xpathQueryNode2 = "./feature[@type='site'][contains(@description, 'Cleavage')]";
            //RETRIEVE ENZYME DESCRIPTION IN SELECTED ENZYME FEATURES
            XPathNodeUniprot XPathNoder20 = new XPathNodeUniprot();
            String xpathQueryNode20 = "./@description";
            //RETRIEVE P1 AA NUMBER IN SELECTED ENZYME FEATURES
            XPathNodeUniprot XPathNoder3 = new XPathNodeUniprot();
            String xpathQueryNode3 = "./location/begin/@position";
            XPathNodeUniprot XPathNoder30 = new XPathNodeUniprot();
            String xpathQueryNode30 = "./location//position/@position";

            //RETRIEVE P1PRIME AA NUMBER IN SELECTED ENZYME FEATURES
            XPathNodeUniprot XPathNoder4 = new XPathNodeUniprot();
            String xpathQueryNode4 = "./location/end/@position";
            Loop l1 = new Loop();
            //FOR EACH SELECTED ENTRIES:
            for (int i = 0; i < getNodeListByXPath.getLength(); i++) {

                //CREATE A SUBSTRATE ENTRY THAT WILL COLLECT ALL RELEVANT INFORMATION
                SubstrateDBEntry sdb = new SubstrateDBEntry();


                //0 INFO SDB: GET PROTEIN NAME
                NodeList getNodeListByXPathNoder0 = XPathNoder0.getNodeListByXPath(xpathQueryNode0, getNodeListByXPath.item(i));
                //1 INFO SDB: GET GENE NAME
                NodeList getNodeListByXPathNoder = XPathNoder.getNodeListByXPath(xpathQueryNode, getNodeListByXPath.item(i));
                //2 INFO SDB: GET LIST OF ACCESSION
                NodeList getNodeListByXPathNoder1 = XPathNoder1.getNodeListByXPath(xpathQueryNode1, getNodeListByXPath.item(i));
                //3 INFO SDB: GET TAXONOMY
                NodeList getNodeListByXPathNoder5 = XPathNoder5.getNodeListByXPath(xpathQueryNode5, getNodeListByXPath.item(i));
                //4 INFO SDB: GET URL
                NodeList getNodeListByXPathNoder6 = XPathNoder6.getNodeListByXPath(xpathQueryNode6, getNodeListByXPath.item(i));
                //5 INFO SDB: GET FULL SEQUENCE
                NodeList getNodeListbyXPathNoder7 = XPathNoder7.getNodeListByXPath(xpathQueryNode7, getNodeListByXPath.item(i));


                //INTERLUDE: GET ENZYME FEATURE
                NodeList getNodeListByXPathNoder2 = XPathNoder2.getNodeListByXPath(xpathQueryNode2, getNodeListByXPath.item(i));

                //4 INFO SDB: PRINT AND SAVE URL
                LinkedList<String> stringfromNodelist6 = l1.getStringfromNodelist(getNodeListByXPathNoder6);
                String string6 = stringfromNodelist6.getFirst();
                System.out.println("http://www.uniprot.org/uniprot/" + string6);
                sdb.setSubstrateurl("http://www.uniprot.org/uniprot/" + string6);

                //0 INFO SDB: PRINT AND SAVE PROTEIN NAME    
                LinkedList<String> stringfromNodelist0 = l1.getStringfromNodelist(getNodeListByXPathNoder0);
                String string0 = null;

                if (!stringfromNodelist0.isEmpty()) {
                    string0 = stringfromNodelist0.getFirst();
                    string0 = string0.replaceAll(",", "");
                    System.out.println(string0);
                    sdb.setSubstrateprotname(string0);
                }

                //1 INFO SDB: PRINT AND SAVE GENE NAME    
                LinkedList<String> stringfromNodelist = l1.getStringfromNodelist(getNodeListByXPathNoder);
                String string = null;
                if (!stringfromNodelist.isEmpty()) {
                    string = stringfromNodelist.getFirst();
                    System.out.println(string);
                    sdb.setSubstratename(string);
                }

                //2 INFO SDB: PRINT AND SAVE LIST OF ACCESSION
                LinkedList<String> stringfromNodelist1 = l1.getStringfromNodelist(getNodeListByXPathNoder1);
                String string1 = stringfromNodelist1.toString();
                string1 = string1.replaceAll(",", ";");

                System.out.println(string1);
                sdb.setSubstrateacessionlist(string1);

                //3 INFO SDB: PRINT AND SAVE TAXONOMY
                LinkedList<String> stringfromNodelist5 = l1.getStringfromNodelist(getNodeListByXPathNoder5);
                String string5 = stringfromNodelist5.getFirst();
                System.out.println(string5);
                sdb.setSubstratetaxonomy(string5);



                //5 INFO SDB: PRINT AND SAVE FULL SEQUENCE
                LinkedList<String> stringfromNodelist7 = l1.getStringfromNodelist(getNodeListbyXPathNoder7);
                String string7 = stringfromNodelist7.getFirst();
                string7 = string7.replaceAll("\n", "");
                sdb.setSubstratesequence(string7);
                //int length = stringx.length();
                //System.out.println(stringx);
                //System.out.println(length);


                //INTERLUDE: FOR EACH ENZYME FEATURE OF SELECTED ENTRIES
                for (int j = 0; j < getNodeListByXPathNoder2.getLength(); j++) {

                    //CREATE A CLEAVAGE SITE ENTRY THAT WILL COLLECT ALL RELEVANT INFORMATION
                    CleavageSiteDBEntry cs = new CleavageSiteDBEntry();

                    Node n = getNodeListByXPathNoder2.item(j);

                    //1 INFO CSDB: GET ENZYME DESCRIPTION
                    NodeList getNodeListByXPathNoder20 = XPathNoder20.getNodeListByXPath(xpathQueryNode20, n);
                    //2 INFO CSDB: GET P1 AA
                    NodeList getNodeListbyXPathNoder3 = XPathNoder3.getNodeListByXPath(xpathQueryNode3, n);
                    //3 INFO CSDB: GET P1PRIME AA
                    NodeList getNodeListbyXPathNoder4 = XPathNoder4.getNodeListByXPath(xpathQueryNode4, n);

                    //1 INFO CSDB: PRINT AND SAVE DESCRIPTION     
                    LinkedList<String> stringfromNodelist20 = l1.getStringfromNodelist(getNodeListByXPathNoder20);
                    String string20 = stringfromNodelist20.getFirst();
                    string20 = string20.replaceAll("Cleavage; by ", "");
                    string20 = string20.replaceAll("Cleavage, first; by ", "");
                    string20 = string20.replaceAll("Cleavage, second; by ", "");
                    string20 = string20.replaceAll(",", ";");
                    if (string20.equalsIgnoreCase("autolysis")) {
                        string20 = string20.replaceAll("autolysis", string);
                        //CREATE A PROTEASE ENTRY THAT WILL COLLECT PROTEASE NAME
                        ProteaseDBEntry pdb = new ProteaseDBEntry();
                        pdb.setEnzymename(string20);
                        cs.setProtease(pdb);
                        cs.setSubstrate(sdb);
                        System.out.println(string20);

                    } else {
                        if (string20.equalsIgnoreCase("autocatalysis")) {
                            string20 = string20.replaceAll("autocatalysis", string);
                            //CREATE A PROTEASE ENTRY THAT WILL COLLECT PROTEASE NAME
                            ProteaseDBEntry pdb = new ProteaseDBEntry();
                            pdb.setEnzymename(string20);
                            cs.setProtease(pdb);
                            cs.setSubstrate(sdb);
                            System.out.println(string20);

                        } else {

                            //CREATE A PROTEASE ENTRY THAT WILL COLLECT PROTEASE NAME
                            ProteaseDBEntry pdb = new ProteaseDBEntry();
                            pdb.setEnzymename(string20);
                            cs.setProtease(pdb);
                            cs.setSubstrate(sdb);
                            System.out.println(string20);
                        }
                    }


                    //2 INFO CSDB: PRINT AND SAVE P1 AA INTEGER
                    LinkedList<String> stringfromNodelist3 = l1.getStringfromNodelist(getNodeListbyXPathNoder3);
                    String P1 = null;
                    int intP1 = 0;
                    if (stringfromNodelist3.isEmpty()) {
                        NodeList getNodeListbyXPathNoder30 = XPathNoder30.getNodeListByXPath(xpathQueryNode30, n);
                        LinkedList<String> stringfromNodelist30 = l1.getStringfromNodelist(getNodeListbyXPathNoder30);
                        String string3 = stringfromNodelist30.getFirst();
                        P1 = string3;
                        System.out.println(string3);
                        intP1 = Integer.parseInt(P1);
                        cs.setP1(intP1);
                    } else {
                        String string3 = stringfromNodelist3.getFirst();
                        P1 = string3;
                        System.out.println(string3);
                        intP1 = Integer.parseInt(P1);
                        cs.setP1(intP1);
                    }

                    //3 INFO CSDB: PRINT AND SAVE P1PRIME AA INTEGER
                    LinkedList<String> stringfromNodelist4 = l1.getStringfromNodelist(getNodeListbyXPathNoder4);
                    String P1prime = null;
                    int intP1prime = 0;
                    if (stringfromNodelist4.isEmpty()) {
                        intP1prime = intP1 + 1;
                        System.out.println(intP1prime);
                        cs.setP1prime(intP1prime);

                    } else {
                        String string4 = stringfromNodelist4.getFirst();
                        P1prime = string4;
                        System.out.println(string4);
                        intP1prime = Integer.parseInt(P1prime);
                        cs.setP1prime(intP1prime);
                    }

                    //4 INFO CSDB: PRINT AND SAVE P1 AA LETTER
                    char aaP1 = string7.charAt(intP1 - 1);
                    System.out.println(aaP1);
                    cs.setAaP1(aaP1);

                    //5 INFO CSDB: PRINT AND SAVE P1PRIME AA LETTER
                    char aaP1prime = string7.charAt(intP1prime - 1);
                    System.out.println(aaP1prime);
                    cs.setAaP1prime(aaP1prime);

                    //ADD ALL DATA TO BIG FILE
                    alldata.add(cs);
                }





                //OLD FASHION STYLE!!!! 

                //                for (int j = 0;j<getNodeListByXPathNoder1.getLength(); j++){
                //                    Text text1 = (Text) getNodeListByXPathNoder1.item(j);
                //                    String textvalue1 = text1.getNodeValue();
                //                    System.out.println(textvalue1);
                //                  
                //                }
                //                
                //                for (int k = 0;k<getNodeListByXPathNoder2.getLength(); k++){
                //                    Node node2 = (Node) getNodeListByXPathNoder2.item(k);
                //                    String nodevalue2 = node2.getNodeValue();
                //                    nodevalue2 = nodevalue2.replaceAll("Cleavage; by ","");
                //                    System.out.println(nodevalue2);
                //                    
                //                    NodeList getNodeListByXPathNoder3 = XPathNoder3.getNodeListByXPath(xpathQueryNode3, getNodeListByXPath.item(k));
                //                    Node node3 = (Node) getNodeListByXPathNoder3.item(k);
                //                    String nodevalue3 = node3.getNodeValue();
                //                    System.out.println(nodevalue3);
                //                   
                //                    NodeList getNodeListByXPathNoder4 = XPathNoder4.getNodeListByXPath(xpathQueryNode4, getNodeListByXPath.item(k));
                //                    Node node4 = (Node) getNodeListByXPathNoder4.item(k);
                //                    String nodevalue4 = node4.getNodeValue();
                //                    System.out.println(nodevalue4);
                //                    
                //                    
                //                }
                //               
            }
            offset = offset + 100;
        }
        try {
            System.out.println("-----------------");
            csvWriter = new PrintStream("CSDB.csv");
            csvWriter.print("Uniprot Enzyme Name");
            csvWriter.print(",");
            csvWriter.print("Enzyme Name");
            csvWriter.print(",");
            csvWriter.print("Enzyme Accession");
            csvWriter.print(",");
            csvWriter.print("Enzyme EC Number");
            csvWriter.print(",");
            csvWriter.print("Protein Gene Name");
            csvWriter.print(",");
            csvWriter.print("Protein Name");
            csvWriter.print(",");
            csvWriter.print("Substrate list of Accession");
            csvWriter.print(",");
            csvWriter.print("Substrate Sequence");
            csvWriter.print(",");
            csvWriter.print("Substrate taxonomy");
            csvWriter.print(",");
            csvWriter.print("P1 #");
            csvWriter.print(",");
            csvWriter.print("P1' #");
            csvWriter.print(",");
            csvWriter.print("P1 aa");
            csvWriter.print(",");
            csvWriter.print("P1' aa");
            csvWriter.print(",");
            csvWriter.print("external link");
            csvWriter.print(",");
            csvWriter.print("PMID");
            csvWriter.print(",");
            csvWriter.print("Comment");
            csvWriter.print("\n");
            for (CleavageSiteDBEntry cleavageSiteDBEntry : alldata) {
                System.out.println(cleavageSiteDBEntry);
                csvWriter.print(cleavageSiteDBEntry.protease.getEnzymename());
                csvWriter.print(",");
                csvWriter.print("");
                csvWriter.print(",");
                csvWriter.print("");
                csvWriter.print(",");
                csvWriter.print("");
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.substrate.getSubstratename());
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.substrate.getSubstrateprotname());
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.substrate.getSubstrateacessionlist());
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.substrate.getSubstratesequence());
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.substrate.getSubstratetaxonomy());
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.getP1());
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.getP1prime());
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.getAaP1());
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.getAaP1prime());
                csvWriter.print(",");
                csvWriter.print(cleavageSiteDBEntry.substrate.getSubstrateurl());
                csvWriter.print(",");
                csvWriter.print("");
                csvWriter.print(",");
                csvWriter.print("");
                csvWriter.print("\n");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetUniprotProteaseDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            csvWriter.close();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GetUniprotProteaseDB JulieUniprot = new GetUniprotProteaseDB();
        // TODO code application logic here
    }
}
