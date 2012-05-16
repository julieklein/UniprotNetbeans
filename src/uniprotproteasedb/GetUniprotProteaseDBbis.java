/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uniprotproteasedb;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author julieklein
 */
public class GetUniprotProteaseDBbis {

    private static final Logger logger = Logger.getLogger(GetUniprotProteaseDBbis.class.getName());

    public static int getLineCount(String url) throws IOException {
        int linecounter = 0;
        BufferedReader urlReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        while (urlReader.readLine() != null) {
            linecounter++;
        }
        System.out.println(linecounter);
        return linecounter;
    }

    private String getSubstrategenename(NodeList entries, int i, SubstrateDBEntry sdb) {
        //GET SUBSTRATE GENE NAME using getInformation method
        LinkedList<String> genenamelist = getInformation("./gene/name[@type][1]/text()", entries.item(i));
        String genename = null;
        if (!genenamelist.isEmpty()) {
            genename = genenamelist.getFirst();
            System.out.println(genename);
            sdb.setSubstratename(genename);
        }
        return genename;
    }

    private void getSubstratepproteinname(NodeList entries, int i, SubstrateDBEntry sdb) {
        //GET SUBSTRATE PROTEIN NAME using getInformation method
        LinkedList<String> protnamelist = getInformation("./protein/recommendedName/fullName/text()", entries.item(i));
        String protname = null;
        if (!protnamelist.isEmpty()) {
            protname = protnamelist.getFirst();
            protname = protname.replaceAll(",", "");
            System.out.println(protname);
            sdb.setSubstrateprotname(protname);
        }
    }

    private String getSubstratesequence(NodeList entries, int i, SubstrateDBEntry sdb) {
        //GET PROTSEQUENCE using getInformation method
        String sequence = getInformation("./sequence/text()", entries.item(i)).getFirst();
        sequence = sequence.replaceAll("\n", "");
        sdb.setSubstratesequence(sequence);
        return sequence;
    }

    private void getSubstratetaxonomy(NodeList entries, int i, SubstrateDBEntry sdb) {
        //GET TAXONOMY using getInformation method
        String taxon = getInformation("./organism/dbReference/@id", entries.item(i)).getFirst();
        System.out.println(taxon);
        sdb.setSubstratetaxonomy(taxon);
    }

    private void getUrlandAccession(NodeList entries, int i, SubstrateDBEntry sdb) {
        //GET URL AND GET ACCESSION using getInformation method
        LinkedList<String> idlist = getInformation("./accession[1]/text()", entries.item(i));
        String id = idlist.getFirst();
        String csdburl = "http://www.uniprot.org/uniprot/" + id;
        System.out.println(csdburl);
        System.out.println(id);
        sdb.setSubstrateurl(csdburl);
        sdb.setSubstrateacessionlist(id);
    }

    private Document parseUniprot(String url) {
        ParseUniprotProt parser = new ParseUniprotProt();
        Document xml = parser.getXML(url);
        xml.getXmlVersion();
        return xml;
    }

    private NodeList getEntries(String query, Document xml) {
        XPathUniprot XPather = new XPathUniprot();
        NodeList entrylist = XPather.getNodeListByXPath(query, xml);
        return entrylist;
    }

    private LinkedList<String> getInformation(String query, Node i) {
        XPathNodeUniprot XPathNoder = new XPathNodeUniprot();
        NodeList entrynodelist = XPathNoder.getNodeListByXPath(query, i);
        Loop l1 = new Loop();
        LinkedList<String> information = l1.getStringfromNodelist(entrynodelist);
        return information;
    }

    private NodeList getCsdbentries(String query, Node i) {
        XPathNodeUniprot XPathNoder = new XPathNodeUniprot();
        NodeList csdbentries = XPathNoder.getNodeListByXPath(query, i);
        return csdbentries;
    }

    private BufferedReader createBufferedreader(String datafilename) throws FileNotFoundException {
        BufferedReader bReader = new BufferedReader(
                new FileReader(datafilename));
        return bReader;

    }
    
    private void populateHeaders(PrintStream csvWriter) throws FileNotFoundException{
        
            csvWriter.print("Natural Language Protease Name");
            csvWriter.print(",");
            csvWriter.print("Protease Symbol");
            csvWriter.print(",");
            csvWriter.print("Protease Accession");
            csvWriter.print(",");
            csvWriter.print("Protease EC Number");
            csvWriter.print(",");
            csvWriter.print("Substrate Gene Name");
            csvWriter.print(",");
            csvWriter.print("Substrate Name");
            csvWriter.print(",");
            csvWriter.print("Substrate Accession");
//            csvWriter.print(",");
//            csvWriter.print("Substrate Sequence");
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
    }

    public GetUniprotProteaseDBbis() throws FileNotFoundException, IOException {


        //CREATE THE BIG RESULT TABLE
        LinkedList<CleavageSiteDBEntry> alldata = new LinkedList<CleavageSiteDBEntry>();


        //GET LINE COUNT using getLineCount method
        String UniprotIDListURL = "http://www.uniprot.org/uniprot/?query=%22cleavage%3B%22+organism%3A9606+reviewed%3Ayes&format=list";
        int lineCount = getLineCount(UniprotIDListURL);

        //OPEN THE WHILE LOOP TO RETRIEVE 100 ENTRIES/100 ENTRIES
        String UniprotURLdebut = "http://www.uniprot.org/uniprot/?query=%22cleavage%3B%22+organism%3A9606+reviewed%3Ayes&format=xml&limit=100&offset=";
        String UniprotURL = null;
        int offset = 0;
        while (offset < lineCount) {
            UniprotURL = UniprotURLdebut + offset;
            System.out.println(UniprotURL);


            //GET ENTRIES using getEntries method
            NodeList entries = getEntries("/uniprot/entry[./feature[@type='site'][contains(@description, 'Cleavage')]]", parseUniprot(UniprotURL));
            for (int i = 0; i < entries.getLength(); i++) {
               
                SubstrateDBEntry sdb = new SubstrateDBEntry();
                
                getUrlandAccession(entries, i, sdb);
                getSubstratepproteinname(entries, i, sdb);
                String genename = getSubstrategenename(entries, i, sdb);
                getSubstratetaxonomy(entries, i, sdb);
                String sequence = getSubstratesequence(entries, i, sdb);

                //GET INTO CSDB ENTRIES using getCsdbentries method
                NodeList csdbentries = getCsdbentries("./feature[@type='site'][contains(@description, 'Cleavage')]", entries.item(i));
                for (int j = 0; j < csdbentries.getLength(); j++) {

                    CleavageSiteDBEntry cs = new CleavageSiteDBEntry();
                    cs.setSubstrate(sdb);
                    Node n = csdbentries.item(j);

                    //GET CSDB NATURAL LANGUAGE DESCRIPTION using getInformation nmethod
                    LinkedList<String> descriptionlist = getInformation("./@description", n);
                    String description = descriptionlist.getFirst();
                    description = description.replaceAll("Cleavage; by ", "");
                    description = description.replaceAll("Cleavage, first; by ", "");
                    description = description.replaceAll("Cleavage, second; by ", "");
                    description = description.replaceAll(",", ";");
                    description = description.replaceAll("autolysis", genename);
                    description = description.replaceAll("autocatalysis", genename);
                    ProteaseDBEntry pdb = new ProteaseDBEntry();
                    pdb.setEnzymename(description);
                    cs.setProtease(pdb);
                    System.out.println(description);

                    
                    //SET n.d. BY DEFAULT ON CSDB SYMBOL, CSDB ACCESSION, AND CSDB EC NUMBER 
                    String symbolempty = "n.d.";
                    String accessionempty = "n.d.";
                    String ecnumberempty = "n.d.";
                    System.out.println(accessionempty);
                    System.out.println(ecnumberempty);
                    System.out.println(symbolempty);
                    pdb.setEnzymesymbol(symbolempty);
                    pdb.setEnzymeaccession(accessionempty);
                    pdb.setEnzymebrenda(ecnumberempty);
                    
                   

                    //OPEN NATURAL LANGUAGE TO SYMBOL LIBRAIRY 1 using createBufferedreader method    
                    BufferedReader bReader = createBufferedreader("//Users/julieklein/Desktop/ProteasiX/ProteaseLibrairy_NaturalLanguageToSymbol.txt");
                    String line;
                    String symbollib1 = null;
                    while ((line = bReader.readLine()) != null) {
                        String splitarray[] = line.split("\t");
                        String naturallanguage = splitarray[0];
                        naturallanguage = naturallanguage.replaceAll("\"","");

                        if (naturallanguage.equalsIgnoreCase(description)) {                            
                            symbollib1 = splitarray[1];
                            System.out.println(symbollib1);
                            pdb.setEnzymesymbol(symbollib1);

                            //OPEN NATURAL SYMBOL TO IDs LIBRAIRY 2 using createBufferedreader method   
                            BufferedReader bReader2 = createBufferedreader("//Users/julieklein/Desktop/ProteasiX/ProteaseLibrairy2_SymbolToIds.txt");
                            String line2;

                            while ((line2 = bReader2.readLine()) != null) {
                                String splitarray2[] = line2.split("\t");
                                String symbollib2 = splitarray2[0];

                                if (symbollib2.equalsIgnoreCase(symbollib1)) {
                                    String accession = splitarray2[1];
                                    String ecnumber = splitarray2[2];
                                    System.out.println(accession);
                                    System.out.println(ecnumber);
                                    pdb.setEnzymeaccession(accession);
                                    pdb.setEnzymebrenda(ecnumber);
                                    cs.setProtease(pdb);
                                    cs.setSubstrate(sdb);
                                    
                                }
                            }
                        }
                        
                    }
                    
                     //GET CSDB P1 INT AND CHAR using getInformation method           
                    LinkedList<String> p1intlist = getInformation("./location/begin/@position", n);
                    String p1int = null;
                    int intP1 = 0;
                    if (p1intlist.isEmpty()) {
                        LinkedList<String> positionlist = getInformation("./location//position/@position", n);
                        p1int = positionlist.getFirst();
                        System.out.println(p1int);
                        intP1 = Integer.parseInt(p1int);
                        cs.setP1Number(intP1);
                        char aaP1 = sequence.charAt(intP1 - 1);
                        System.out.println(aaP1);
                        cs.setP1Name(aaP1);
                    } else {
                        p1int = p1intlist.getFirst();
                        System.out.println(p1int);
                        intP1 = Integer.parseInt(p1int);
                        cs.setP1Number(intP1);
                        char aaP1 = sequence.charAt(intP1 - 1);
                        System.out.println(aaP1);
                        cs.setP1Name(aaP1);
                    }

                    //GET CSDB P1prime INT AND CHAR using getInformation method
                    LinkedList<String> p1primeintlist = getInformation("./location/end/@position", n);
                    String p1primeint = null;
                    int intP1prime = 0;
                    if (p1primeintlist.isEmpty()) {
                        intP1prime = intP1 + 1;
                        System.out.println(intP1prime);
                        cs.setP1primeNumber(intP1prime);
                        char aaP1prime = sequence.charAt(intP1prime - 1);
                        System.out.println(aaP1prime);
                        cs.setP1primeName(aaP1prime);

                    } else {
                        p1primeint = p1primeintlist.getFirst();
                        System.out.println(p1primeint);
                        intP1prime = Integer.parseInt(p1primeint);
                        cs.setP1primeNumber(intP1prime);
                        char aaP1prime = sequence.charAt(intP1prime - 1);
                        System.out.println(aaP1prime);
                        cs.setP1primeName(aaP1prime);
                    }
                    
                    
                    alldata.add(cs);
                }
            }
            offset = offset + 100;
        }
        PrintStream csvWriter = null;
        try {
            System.out.println("-----------------");
            csvWriter = new PrintStream("CSDB.csv");
            populateHeaders(csvWriter);
            for (CleavageSiteDBEntry cleavageSiteDBEntry : alldata) {
                populateData(csvWriter, cleavageSiteDBEntry);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetUniprotProteaseDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            csvWriter.close();
        }

    }

    private void populateData(PrintStream csvWriter, CleavageSiteDBEntry cleavageSiteDBEntry) {
        //System.out.println(cleavageSiteDBEntry);
        csvWriter.print(cleavageSiteDBEntry.protease.getEnzymename());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.protease.getEnzymesymbol());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.protease.getEnzymeaccession());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.protease.getEnzymebrenda());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.substrate.getSubstratename());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.substrate.getSubstrateprotname());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.substrate.getSubstrateacessionlist());
//                csvWriter.print(",");
//                csvWriter.print(cleavageSiteDBEntry.substrate.getSubstratesequence());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.substrate.getSubstratetaxonomy());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.getP1Number());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.getP1primeNumber());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.getP1Name());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.getP1primeName());
        csvWriter.print(",");
        csvWriter.print(cleavageSiteDBEntry.substrate.getSubstrateurl());
        csvWriter.print(",");
        csvWriter.print("");
        csvWriter.print(",");
        csvWriter.print("");
        csvWriter.print("\n");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        GetUniprotProteaseDBbis JulieUniprot = new GetUniprotProteaseDBbis();
        // TODO code application logic here
    }
}
