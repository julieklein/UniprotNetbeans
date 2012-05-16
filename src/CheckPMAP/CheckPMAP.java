/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CheckPMAP;

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
import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author julieklein
 */
public class CheckPMAP {

    private BufferedReader createBufferedreader(String datafilename) throws FileNotFoundException {
        BufferedReader bReader = new BufferedReader(
                new FileReader(datafilename));
        return bReader;

    }

    private Document parseUniprot(String url) {
        ParseUniprot parser = new ParseUniprot();
        Document xml = parser.getXML(url);
        xml.getXmlVersion();
        return xml;
    }
//    private NodeList getUniprotentrycontent (String query, Document xml) {
//        XPathUniprot XPather = new XPathUniprot();
//        NodeList entrycontent = XPather.getNodeListByXPath(query, xml);
//        return entrycontent;
//    }

    private LinkedList<String> getSequence(String query, Document xml) {
        XPathNodeUniprot XPathNoder = new XPathNodeUniprot();
        NodeList uniprotentry = XPathNoder.getNodeListByXPath(query, xml);
        Loop l1 = new Loop();
        LinkedList<String> sequence = l1.getStringfromNodelist(uniprotentry);
        return sequence;
    }

    public CheckPMAP() throws Exception {

        PrintStream csvWriter = null;
        LinkedList<CuratedEntry> alldata = new LinkedList<CuratedEntry>();

        BufferedReader bReader = createBufferedreader("//Users/julieklein/Desktop/ProteasiX/PMAP/MARS2012/Total_NOTCurated.txt");
        String line;

        while ((line = bReader.readLine()) != null) {
            String splitarray[] = line.split("\t");
            CuratedEntry ce = new CuratedEntry();
            String substrateuniprot = getNonCuratedInformation(splitarray, ce);

            String P1number = splitarray[9];
            String P1primenumber = splitarray[10];
            String P1sequence = splitarray[11];
            String P1primesequence = splitarray[12];
            int P1position = Integer.parseInt(P1number);
            int P1primeposition = Integer.parseInt(P1primenumber);

            ce.setCsp1sequence(P1sequence);
            ce.setCsp1primesequence(P1primesequence);

            String curation = splitarray[16];


            if (curation.equalsIgnoreCase("-") && !substrateuniprot.equalsIgnoreCase("n.d.") && !P1sequence.equalsIgnoreCase("?") && !P1primesequence.equalsIgnoreCase("?")) {
                String cleavagesite = splitarray[13];

                if (cleavagesite.startsWith("-")) {
                    cleavagesite.replaceFirst("-", "");
                    if (cleavagesite.startsWith("-")) {
                        cleavagesite.replaceFirst("-", "");
                    }
                    if (cleavagesite.startsWith("-")) {
                        cleavagesite.replaceFirst("-", "");
                    }
                }
                String cleavagesitenodash = cleavagesite.replaceAll("-", "");
                String sequence = getSequence("/uniprot/entry/sequence/text()", parseUniprot("http://www.uniprot.org/uniprot/" + substrateuniprot + ".xml")).getFirst();
                sequence = sequence.replaceAll("\n", "");
                if (sequence.contains(cleavagesitenodash)) {
                    int positionoflargecleavagesite = sequence.indexOf(cleavagesite);
                    positionoflargecleavagesite = positionoflargecleavagesite + 1;
//                    System.out.println(positionoflargecleavagesite);
                    String cleavagesitesequence = P1sequence + P1primesequence;
                    int positionofsmallcleavagesite = cleavagesite.indexOf(cleavagesitesequence);
//                    positionofsmallcleavagesite = positionofsmallcleavagesite;
                    int newp1 = positionoflargecleavagesite + positionofsmallcleavagesite;
                    int newp1prime = newp1 + 1;
                    String newp1number = Integer.toString(newp1);
                    String newp1primenumber = Integer.toString(newp1prime);

                    if (newp1number.equalsIgnoreCase(P1number) && newp1primenumber.equalsIgnoreCase(P1primenumber)) {
                        ce.setCsp1position(P1position);
                        ce.setCsp1primeposition(P1primeposition);
                        System.out.println(P1position);
                        System.out.println(P1primeposition);
                        System.out.println(P1sequence);
                        System.out.println(P1primesequence);
                        ce.setComment("-");
                        System.out.println("-");
                        ce.setCuration(curation);
                        System.out.println(curation);
                    } else {
                        ce.setCsp1position(newp1);
                        ce.setCsp1primeposition(newp1prime);
                        System.out.println(newp1);
                        System.out.println(newp1prime);
                        ce.setCuration("Cleavage site curated based on Uniprot protein sequence");
                        System.out.println("Cleavage site curated based on Uniprot protein sequence");

                    }

                } else {
                    ce.setCsp1position(P1position);
                    ce.setCsp1primeposition(P1primeposition);
                    System.out.println(P1position);
                    System.out.println(P1primeposition);
                    System.out.println(P1sequence);
                    System.out.println(P1primesequence);
                    ce.setComment("Unmatched cleavage site");
                    System.out.println("Unmatched cleavage site");
                    ce.setCuration("Cleavage site discarded");
                    System.out.println("Cleavage site discarded");
                }
            } else {
                ce.setCsp1position(P1position);
                ce.setCsp1primeposition(P1primeposition);
                System.out.println(P1position);
                System.out.println(P1primeposition);
                System.out.println(P1sequence);
                System.out.println(P1primesequence);
                ce.setComment("-");
                System.out.println("-");
                ce.setCuration(curation);
                System.out.println(curation);
            }




//        ADD ALL DATA TO BIG FILE
            alldata.add(ce);
        }


        try {
            System.out.println("-----------------");
            csvWriter = new PrintStream("CheckPMAP.csv");
            csvWriter.print("PMAP Protease Name");
            csvWriter.print(",");
            csvWriter.print("Protease Symbol");
            csvWriter.print(",");
            csvWriter.print("Protease Accession");
            csvWriter.print(",");
            csvWriter.print("Protease EC Number");
            csvWriter.print(",");
            csvWriter.print("Protease taxonomy");
            csvWriter.print(",");
            csvWriter.print("Substrate Name");
            csvWriter.print(",");
            csvWriter.print("Substrate Symbol");
            csvWriter.print(",");
            csvWriter.print("Substrate Accession");
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
            csvWriter.print(",");
            csvWriter.print("Curation");
            csvWriter.print("\n");
            for (CuratedEntry ce : alldata) {
                //System.out.println(cleavageSiteDBEntry);
                csvWriter.print(ce.getProteasename());
                csvWriter.print(",");
                csvWriter.print(ce.getProteasesymbol());
                csvWriter.print(",");
                csvWriter.print(ce.getProteaseuniprot());
                csvWriter.print(",");
                csvWriter.print(ce.getProteasebrenda());
                csvWriter.print(",");
                csvWriter.print(ce.getProteasetaxon());
                csvWriter.print(",");
                csvWriter.print(ce.getSubstratename());
                csvWriter.print(",");
                csvWriter.print(ce.getSubstratesymbol());
                csvWriter.print(",");
                csvWriter.print(ce.getSubstrateuniprot());
                csvWriter.print(",");
                csvWriter.print(ce.getSubstratetaxon());
                csvWriter.print(",");
                csvWriter.print(ce.getCsp1position());
                csvWriter.print(",");
                csvWriter.print(ce.getCsp1primeposition());
                csvWriter.print(",");
                csvWriter.print(ce.getCsp1sequence());
                csvWriter.print(",");
                csvWriter.print(ce.getCsp1primesequence());
                csvWriter.print(",");
                csvWriter.print(ce.getExternallink());
                csvWriter.print(",");
                csvWriter.print(ce.getPmid());
                csvWriter.print(",");
                csvWriter.print(ce.getComment());
                csvWriter.print(",");
                csvWriter.print(ce.getCuration());
                csvWriter.print("\n");














            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CheckPMAP.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            csvWriter.close();
        }
    }

    private String getNonCuratedInformation(String[] splitarray, CuratedEntry ce) {
        String proteasename = splitarray[0];
        proteasename = proteasename.replaceAll("\"", "");
        String proteasesymbol = splitarray[1];
        proteasesymbol = proteasesymbol.replaceAll("sept-0", "SEPT");
        String proteaseuniprot = splitarray[2];
        String proteasebrenda = splitarray[3];
        String proteasetaxon = splitarray[4];
        ce.setProteasename(proteasename);
        ce.setProteasesymbol(proteasesymbol);
        ce.setProteaseuniprot(proteaseuniprot);
        ce.setProteasebrenda(proteasebrenda);
        ce.setProteasetaxon(proteasetaxon);
        System.out.println(proteasename);
        System.out.println(proteasesymbol);
        System.out.println(proteaseuniprot);
        System.out.println(proteasebrenda);
        System.out.println(proteasetaxon);
        String substratename = splitarray[5];
        substratename = substratename.replaceAll("\"", "");
        String substratesymbol = splitarray[6];
        substratesymbol = substratesymbol.replaceAll("sept-0", "SEPT");
        String substrateuniprot = splitarray[7];
        String substrateetaxon = splitarray[8];
        ce.setSubstratename(substratename);
        ce.setSubstratesymbol(substratesymbol);
        ce.setSubstrateuniprot(substrateuniprot);
        ce.setSubstratetaxon(substrateetaxon);
        System.out.println(substratename);
        System.out.println(substratesymbol);
        System.out.println(substrateuniprot);
        System.out.println(substrateetaxon);
        String url = splitarray[14];
        ce.setExternallink(url);
        System.out.println(url);
        String pmid = splitarray[15];
        ce.setPmid(pmid);
        System.out.println(pmid);

        return substrateuniprot;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        CheckPMAP JulieUniprot = new CheckPMAP();
        // TODO code application logic here
    }
}
